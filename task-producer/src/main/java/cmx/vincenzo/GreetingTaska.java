package cmx.vincenzo;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

public abstract class GreetingTaska extends DefaultTask {
	
	// Getter astratto: Gradle genera l'implementazione a runtime.
    // Nessun campo, nessun setter manuale.
    @Input
    @Optional
    public abstract Property<String> getMessage();

    public GreetingTaska() {
        getMessage().convention("Hello from GreetingTaska!");
    }

    @TaskAction
    public void greet() {
        System.out.println(getMessage().get());
    }
	
	
}