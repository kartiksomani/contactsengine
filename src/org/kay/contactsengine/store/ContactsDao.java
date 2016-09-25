/**
 * 
 */
package org.kay.contactsengine.store;

import java.util.List;

import org.kay.contactsengine.exceptions.ContactDataAddException;
import org.kay.contactsengine.exceptions.ContactDeleteException;
import org.kay.contactsengine.exceptions.ContactUpdateException;
import org.kay.contactsengine.exceptions.ContactsDataAccessException;
import org.kay.contactsengine.model.Contact;
import org.springframework.dao.DataAccessException;

/**
 * @author Kartik
 *
 */
public interface ContactsDao {
	public void addContact(final Contact contact) throws ContactDataAddException;
	public List<Contact> search(final String field,final String searchValue);
	public void deleteContact(final Contact contact) throws ContactDeleteException;
	public List<Contact> getAllContacts() throws ContactsDataAccessException;
	public void updateContact(Contact oldContact, Contact newContact) throws ContactUpdateException;
	public List<Contact> getAllContacts(int batchSize, int offset);
	public void addContacts(List<Contact> importedContacts) throws ContactDataAddException;
}
