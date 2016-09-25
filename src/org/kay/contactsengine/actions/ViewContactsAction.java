package org.kay.contactsengine.actions;

import java.util.List;

import org.kay.contactsengine.CLIArguments;
import org.kay.contactsengine.engine.ContactsEngine;
import org.kay.contactsengine.exceptions.ContactsDataAccessException;
import org.kay.contactsengine.exceptions.EngineNotInitializedException;
import org.kay.contactsengine.formatters.ContactFormatter;
import org.kay.contactsengine.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;

public class ViewContactsAction implements Executor {

	@Autowired
	@Setter
	private ContactsEngine contactsEngine;
	
	@Autowired
	@Setter
	private ContactFormatter contactFormatter;
	
	@Override
	public void execute(CLIArguments arguments) {
		List<Contact> allContacts = null;
		try {
			allContacts = contactsEngine.getAllContacts();
		} catch (ContactsDataAccessException e) {
			System.err.println("Could not access the database");
			return;
		} catch (EngineNotInitializedException e) {
			//TODO exit
			System.err.println("Contacts engine was not initialized");
			return;
		}

		if (allContacts.isEmpty()) {
			System.out.println("No contacts found");
			return;
		}
		for (Contact contact:allContacts) {
			System.out.println(contactFormatter.toFormat(contact));
		}
	}

}
