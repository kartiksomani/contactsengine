/**
 * 
 */
package org.kay.contactsengine.exceptions;

import java.util.List;

import org.kay.contactsengine.model.Contact;

import lombok.Getter;
import lombok.NonNull;

/**
 * This is an exception which is thrown on adding a new contact, and if that contact already exists
 * @author Kartik
 *
 */
public class ContactDataAddException extends Exception {
	@Getter
	private List<Contact> failedContacts;
	
	public ContactDataAddException(@NonNull final String message, @NonNull final List<Contact> failedContacts) {
		super(message);
		this.failedContacts = failedContacts;
	}
}
