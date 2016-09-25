package org.kay.contactsengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.kay.contactsengine.actions.Executor;
import org.kay.contactsengine.config.ExecutorConfig;
import org.kay.contactsengine.engine.ContactsEngine;

public class ContactsEngineExecutor {
	private ContactsEngine contactsEngine;
	private ApplicationContext exectorContext;
	
	public ContactsEngineExecutor() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(ExecutorConfig.class);
		context.refresh();
		this.exectorContext = context;		
	}
	
	@Autowired
	public void setContactsEngine(ContactsEngine contactsEngine) {
		this.contactsEngine = contactsEngine;
	}
	
	public void execute(final CLIArguments cliArguments) {
		Executor commandExecutor = (Executor)exectorContext.getBean(cliArguments.getAction().name());

		if (null == commandExecutor) {
			//TODO exit
			System.err.println("Unavailable functionality : " + cliArguments.getAction().name());
			return;
		}
		
		commandExecutor.execute(cliArguments);
	}
	
}
