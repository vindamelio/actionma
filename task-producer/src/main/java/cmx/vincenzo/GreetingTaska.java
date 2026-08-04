package cmx.vincenzo;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

public class GreetingTaska extends DefaultTask {
	
	private String message = "Hello from GreetingTaska!";

    @Input
    @Optional
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @TaskAction
    public void greet() {
        System.out.println(message);
    }	
	
	
}