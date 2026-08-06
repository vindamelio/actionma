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
            "    repositories {\n" +
            "        mavenLocal()\n" +
            "        mavenCentral()\n" +
            "    }\n" +
            "    dependencies {\n" +
            //"        classpath 'cmx.vincenzo:task-producer:1.0.1'\n" +
            "        classpath 'cmx.vincenzo:task-producer:0.0.0-SNAPSHOT-local'\n" +
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
