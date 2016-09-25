package org.kay.contactsengine.formatters;

import java.util.ArrayList;
import java.util.List;

import org.kay.contactsengine.exceptions.InvalidContactFormatException;
import org.kay.contactsengine.model.Contact;

import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 
 * CSV formatter for contact objects
 * A row of CSV string should follow the following format:
 * 		firstname,lastname,email,phone
 * @author kartik
 *
 */
@NoArgsConstructor
public class ContactCSVFormatter implements ContactFormatter {

	@Override
	public String toFormat(@NonNull Contact contact) {
		StringBuilder csvRowBuilder = new StringBuilder();
		csvRowBuilder.append(contact.getFirstName());
		csvRowBuilder.append(",");
		csvRowBuilder.append(emptyIfNull(contact.getLastName()));
		csvRowBuilder.append(",");
		csvRowBuilder.append(emptyIfNull(contact.getEmail()));
		csvRowBuilder.append(",");
		csvRowBuilder.append(emptyIfNull(contact.getPhone()));
		return csvRowBuilder.toString();
	}
	
	private String emptyIfNull(final String value) {
		if (null == value) 
			return "";
		return value;
	}

	@Override
	public String toFormat(@NonNull final List<Contact> contacts) {
		StringBuilder rowBuilder = new StringBuilder(200);
		
		for (Contact contact:contacts) {
			rowBuilder.append(toFormat(contact));
			rowBuilder.append("\n");
		}
		return rowBuilder.toString();
	}

	@Override
	public List<Contact> toContacts(@NonNull final String str) throws InvalidContactFormatException {
		str.trim();
		String[] lines = str.split("\n");
		List<Contact> contacts = new ArrayList<Contact>();
		for (String line:lines) {
			Contact contact = toContact(line);
			if (null != contact) {
				contacts.add(contact);
			}
		}
		return contacts;
	}

	@Override
	public Contact toContact(@NonNull final String str) throws InvalidContactFormatException {
		String[] splitValues = str.split(",");
		
		if (splitValues.length < 4) {
			String exceptionMessage = "Required fields: First Name, Last Name, Email, Phone";
			throw new InvalidContactFormatException(exceptionMessage);
		}
		//TODO consider creating a mapper, so that format is not hardcoded
		String firstName = splitValues[0];
		String lastName = splitValues[1];
		String email = splitValues[2];
		String phone = splitValues[3];
		//TODO consider using a factory, so that validation is done
		Contact contact = new Contact(firstName,lastName,email,phone);
		return contact;
	}
}
