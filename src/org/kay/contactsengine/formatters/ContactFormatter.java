package org.kay.contactsengine.formatters;

import java.util.List;

import org.kay.contactsengine.exceptions.InvalidContactFormatException;
import org.kay.contactsengine.model.Contact;

public interface ContactFormatter {
	//TODO better name, this suggests that argument should be a format
	public String toFormat(final Contact contact);

	public String toFormat(final List<Contact> contacts);

	public List<Contact> toContacts(final String str) throws InvalidContactFormatException;
	
	public Contact toContact(final String str) throws InvalidContactFormatException;
}
