package cmx.vincenzo;




import cmx.vincenzo.GreetingTaska;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreetingTaskaTest {
	
	public Project project;
    private GreetingTaska task;

    @BeforeEach
    void setup() {
        project = ProjectBuilder.builder().build();
        task = project.getTasks().create("testGreeting", GreetingTaska.class);
    }

    @Test
    void defaultMessage_isSetCorrectly() {
        assertEquals("Hello from GreetingTaska!", task.getMessage());
    }

    @Test
    void customMessage_canBeSet() {
        task.setMessage("Messaggio di test");
        assertEquals("Messaggio di test", task.getMessage());
    }
	
	@Test
    void greetAction_doesNotThrow() {
        task.setMessage("Test greet action");
        task.greet(); // esegue l'azione direttamente (non tramite Gradle build lifecycle)
		
    }
}