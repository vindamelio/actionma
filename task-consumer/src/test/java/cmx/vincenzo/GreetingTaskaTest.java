package cmx.vincenzo;


import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreetingTaskaTest {
	

    private GreetingTaska task;

    @BeforeEach
    void setup() {
        Project project = ProjectBuilder.builder().build();
        task = project.getTasks().create("testGreeting", GreetingTaska.class);
    }

    @Test
    void defaultMessage_isSetCorrectly() {
        assertEquals("Hello from GreetingTaska!", task.getMessage().get());
    }

    @Test
    void customMessage_canBeSet() {
        task.getMessage().set("Messaggio di test");
        assertEquals("Messaggio di test", task.getMessage().get());
    }
	
	@Test
    void greetAction_doesNotThrow() {
        task.getMessage().set("Test greet action");
        task.greet(); // esegue l'azione direttamente (non tramite Gradle build lifecycle)
		
    }
}