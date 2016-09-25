package org.kay.contactsengine.actions;

import org.kay.contactsengine.CLIArguments;
import org.kay.contactsengine.engine.ContactsEngine;
import org.kay.contactsengine.exceptions.ContactDataAddException;
import org.kay.contactsengine.exceptions.FileException;
import org.kay.contactsengine.exceptions.InvalidArgumentException;
import org.kay.contactsengine.exceptions.InvalidContactFormatException;
import org.kay.contactsengine.formatters.ContactFormatter;
import org.kay.contactsengine.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.NonNull;
import lombok.Setter;

public class ImportContactsAction implements Executor {
	@Autowired
	@Setter
	private ContactsEngine contactsEngine;
	
	@Autowired
	@Setter
	private ContactFormatter consoleContactFormatter;
	
	@Override
	public void execute(@NonNull final CLIArguments arguments) {
		String importFormat = arguments.getFormat();
		
		if (importFormat.isEmpty()) {
			System.err.println("No import format specified (--format)");
			return; //TODO exit
		}
		
		String importFile = arguments.getFileName();
		
		if (importFile.isEmpty()) {
			System.err.println("No filename specified for import (--filename)");
			return; //TODO exit
		}
		
		try {
			contactsEngine.importContacts(importFormat, importFile);
			System.out.println("Contacts Imported!");
		} catch (ContactDataAddException addExp) {
			System.err.println("Failed to add some contacts:");
			//TODO write to a log file or something
			for(Contact failedContact:addExp.getFailedContacts()) {
				System.out.println(consoleContactFormatter.toFormat(failedContact));
			}
		} catch (FileException | InvalidArgumentException 
				| InvalidContactFormatException e) {
			System.err.println(e);
		}
	}

}
