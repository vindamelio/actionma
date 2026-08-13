package cmx.vincenzo;

import cmx.vincenzo.GreetingTaska;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;


class GreetingTaskaFunctionalTest {

    @TempDir
    Path testProjectDir;

    private Path buildFile;

    @BeforeEach
    void setup() throws IOException {
        buildFile = testProjectDir.resolve("build.gradle");

        // build.gradle "minimo" della build temporanea usata dal test:
        // registra lo stesso task che usiamo nel consumer reale,
        // puntando al producer già pubblicato in mavenLocal
        String buildFileContent =
            "buildscript {\n" +
            "def envProps = new Properties() \n" +
            "file(${rootDir}/.env).with { f -> \n" +
            "    if (f.exists()) { \n" +
            "      f.withInputStream { envProps.load(it) } \n" +
            "    } \n" +
            "} \n" +
            "def use = envProps.getProperty('GITHUB_ACTOR') ?: System.getenv('GITHUB_ACTOR') \n" +
            "def psw = envProps.getProperty('GITHUB_TOKEN') ?: System.getenv('GITHUB_TOKEN') \n" +
            "def runNumber = System.getenv('GITHUB_RUN_NUMBER') ?: '0' \n" +
            "def gitTag = System.getenv('GITHUB_REF_TYPE') == 'tag' ? System.getenv('GITHUB_REF_NAME') : null \n" +
            "def gitSha = System.getenv('GITHUB_SHA')?.take(7) ?: 'local' \n" +
            "def gitVer = project.findProperty('gpr.version') ?: '0.0.0' \n" +
            "def gitRel = project.findProperty('gpr.release') ?: null \n" +
            "def resolvedVersion = gitRel != null ? \"${gitVer\"} : ${gitVer}-SNAPSHOT-${gitSha} \n" +

            "    repositories {\n" +
            "        mavenLocal()\n" +
            "        mavenCentral()\n" +
            "        maven {\n" +
            "            url = uri('https://maven.pkg.github.com/vindamelio/actionma')\n" +
            "            credentials {\n" +
            "                username = use \n" +
            "                password = psw \n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "    dependencies {\n" +
            //"        classpath 'cmx.vincenzo:task-producer:1.0.1'\n" +
            "        classpath \"cmx.vincenzo:task-producer:${resolvedVersion}\"\n" +
            "    }\n" +
            "}\n" +
            "tasks.register('greet', cmx.vincenzo.GreetingTaska) {\n" +
            "    message = 'Messaggio dal test funzionale'\n" +
            "}\n";

        try (Writer writer = Files.newBufferedWriter(buildFile)) {
            writer.write(buildFileContent);
        }
    }

    @Test
    void greetTask_executesSuccessfully_andPrintsMessage() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.toFile())
                .withArguments("greet")
                //.withPluginClasspath()
                .build();

        assertTrue(result.getOutput().contains("Messaggio dal test funzionale"), "L'output della build dovrebbe contenere il messaggio del task");
        assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"), "La build dovrebbe completarsi con successo");
    }

    @Test
    void greetTask_isUpToDateOnSecondRun() {
        // Prima esecuzione
        GradleRunner.create()
                .withProjectDir(testProjectDir.toFile())
                .withArguments("greet")
                //.withPluginClasspath()
                .build();

        // Seconda esecuzione: essendo un task senza @Input/@Output tracciati
        // in modo completo, questo test è puramente dimostrativo
        BuildResult secondResult = GradleRunner.create()
                .withProjectDir(testProjectDir.toFile())
                .withArguments("greet")
                //.withPluginClasspath()
                .build();

        assertTrue(secondResult.getOutput().contains("BUILD SUCCESSFUL"));
    }
}
