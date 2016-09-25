package org.kay.contactsengine.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kay.contactsengine.CLIArguments;
import org.kay.contactsengine.engine.ContactsEngine;
import org.kay.contactsengine.exceptions.ContactDataAddException;
import org.kay.contactsengine.exceptions.EngineNotInitializedException;
import org.kay.contactsengine.formatters.ContactFormatter;
import org.kay.contactsengine.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.Setter;

public class AddContactAction implements Executor {
	private ContactsEngine contactsEngine;
	
	@Setter
	@Autowired
	private ContactFormatter contactFormatter;
	
	@Autowired
	public void setContactsEngine(final ContactsEngine contactsEngine) {
		this.contactsEngine = contactsEngine;
	}
	
	@Override
	public void execute(CLIArguments arguments) {
		String contactData = arguments.getContactData();
		
		if (null == contactData) {
			System.err.println("No contact data provided to add");
			//TODO exit
			return;
		}
		
		Contact newContacts[] = getContactFromJson(contactData);
		
		if (null == newContacts) {
			//TODO exit
			return;
		}
		
		List<Contact> failedContacts = new ArrayList<Contact>();
		
		for (Contact contact:newContacts) {
			try {
				contactsEngine.addContact(contact);
			} catch (ContactDataAddException e) {
				failedContacts.add(contact);
			} catch (EngineNotInitializedException e) {
				//TODO exit
				System.err.println("Contacts engine was not initialized");
				return;
			}
		}
		if (failedContacts.isEmpty()) {
			System.out.println("All contacts were added successfully!");
		} else {
			System.out.println("Some contacts could not be added:");
			displayfailedContacts(failedContacts);
		}
	}

	private void displayfailedContacts(@NonNull final List<Contact> failedContacts) {
		if (failedContacts.isEmpty()){
			return;
		}
		
		for (Contact contact:failedContacts) {
			System.out.println(contactFormatter.toFormat(contact));
		}		
	}

	private Contact[] getContactFromJson(String contactData) {
		ObjectMapper jsonMapper = new ObjectMapper();
		
		Contact contacts[];
		try {
			contacts = jsonMapper.readValue(contactData, Contact[].class);
		} catch (JsonParseException | JsonMappingException ex) {
			//TODO exit for all exceptions
			System.err.println("Invalid contact data format: " + contactData);
			return null;
		} catch (IOException e) {
			System.err.println("Unknown error while parsing contact data: " + contactData);
			return null;
		}
		return contacts;
	}

}
