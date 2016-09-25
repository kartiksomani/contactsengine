package org.kay.contactsengine.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.kay.contactsengine.CLIArguments;
import org.kay.contactsengine.engine.ContactsEngine;
import org.kay.contactsengine.exceptions.ContactDeleteException;
import org.kay.contactsengine.exceptions.ContactUpdateException;
import org.kay.contactsengine.exceptions.EngineNotInitializedException;
import org.kay.contactsengine.formatters.ContactFormatter;
import org.kay.contactsengine.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.Setter;

public class SearchContactAction implements Executor {
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
		// TODO Auto-generated method stub
		if (null != arguments.getSearchField()) {
			if (null != arguments.getSearchValue()){
				List<Contact> foundContacts;
				try {
					foundContacts = contactsEngine.searchContact(arguments.getSearchField().name(),
							arguments.getSearchValue());
				} catch (EngineNotInitializedException e) {
					//TODO exit
					System.err.println("Contacts engine was not initialized");
					return;
				}
				if (null == foundContacts) {
					System.err.println("Invalid search field: " + arguments.getSearchField().name());
					return;
				}
				if (foundContacts.isEmpty()) {
					System.out.println("No matching contacts found");
					return;
				}
				printContacts(foundContacts);
				
				if (arguments.isRemoveContacts()) {
					removeContacts(foundContacts);
				} else if(arguments.isUpdateContacts()) {
					updateContacts(foundContacts,arguments.getContactData());
				}
			}
		}
	}
	
	private void updateContacts(@NonNull final List<Contact> foundContacts, 
			final String contactData) {
		if (1 < foundContacts.size()) {
			System.err.println("Only one contact can be updated");
			return;
		}
		
		System.out.println("Do you want to continue with the update?");
		Scanner in = new Scanner(System.in);
		String response = in.nextLine();
		
		if (!response.equals("yes")) {
			return;
		}
		ObjectMapper jsonMapper = new ObjectMapper();
		Contact newContact;
		try {
			newContact = jsonMapper.readValue(contactData,Contact.class);
		} catch (JsonParseException | JsonMappingException e1) {
			System.err.println("Invalid contact data");
			return;
		} catch (IOException e1) {
			System.err.println("Unknown error while reading contact data");
			return;
		}
		try {
			contactsEngine.updateContact(foundContacts.get(0),newContact);
			System.out.println("Contact updated!");
		} catch (ContactUpdateException e) {
			System.err.println("Could not update contact!");
			return;
		} catch (EngineNotInitializedException e) {
			//TODO exit
			System.err.println("Contacts engine was not initialized");
			return;
		}
	}

	private void removeContacts(@NonNull final List<Contact> foundContacts) {
		System.out.println("Are you sure you want to remove the contact(s) (yes/no)?");
		String response;
		Scanner in = new Scanner(System.in);
		response = in.nextLine();
		List<Contact> undeletedContacts = new ArrayList<Contact>();
		
		if (!response.equals("yes")) {
			return;
		}
		for (Contact foundContact:foundContacts) {
			try {
				contactsEngine.deleteContact(foundContact);
			} catch (ContactDeleteException e) {
				undeletedContacts.add(foundContact);
			} catch (EngineNotInitializedException e) {
				//TODO exit
				System.err.println("Contacts engine was not initialized");
				return;
			}
		}

		in.close();

		if (undeletedContacts.isEmpty()){
			System.out.println("Deleted all given contacts!");
		} else {
			System.out.println("Could not delete the following contacts:");

			for (Contact undeletedContact:undeletedContacts) {
				System.out.println(contactFormatter.toFormat(undeletedContact));
			}
		}
	}


	public void printContacts(final List<Contact> contacts) {
		for (Contact contact:contacts) {
			System.out.println(contactFormatter.toFormat(contact));
		}
	}

}
