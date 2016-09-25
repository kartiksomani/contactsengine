package org.kay.contactsengine.actions;

import org.kay.contactsengine.CLIArguments;
import org.kay.contactsengine.engine.ContactsEngine;
import org.kay.contactsengine.exceptions.EngineNotInitializedException;
import org.kay.contactsengine.exceptions.FileException;
import org.kay.contactsengine.exceptions.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.NonNull;
import lombok.Setter;

public class ExportContactsAction implements Executor {
	@Autowired
	@Setter
	private ContactsEngine contactsEngine;
	
	@Override
	public void execute(@NonNull final CLIArguments arguments) {
		if (arguments.getFileName().isEmpty()) {
			System.err.println("No filename given for export");
			return; //TODO exit
		}
		
		if (arguments.getFormat().isEmpty()) {
			System.err.println("No format specified for export");
			return; //TODO exit
		}
		
		String filename = arguments.getFileName();
		String exportFormat = arguments.getFormat();
		boolean overwrite = arguments.isOverwriteExportTarget();
		try {
			contactsEngine.exportContacts(filename, exportFormat, overwrite);
			System.out.println("Contacts exported to file: " + filename);
		} catch (InvalidArgumentException e) {
			System.err.println(e);
		} catch (FileException e) {
			System.err.println(e);
		} catch (EngineNotInitializedException e) {
			//TODO exit
			System.err.println("Contacts engine was not initialized");
			return;
		}

	}

}
