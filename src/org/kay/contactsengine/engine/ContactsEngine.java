package org.kay.contactsengine.engine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.kay.contactsengine.config.ContactsEngineConfig;
import org.kay.contactsengine.exceptions.ContactDataAddException;
import org.kay.contactsengine.exceptions.ContactDeleteException;
import org.kay.contactsengine.exceptions.ContactUpdateException;
import org.kay.contactsengine.exceptions.ContactsDataAccessException;
import org.kay.contactsengine.exceptions.EngineNotInitializedException;
import org.kay.contactsengine.exceptions.FileException;
import org.kay.contactsengine.exceptions.InvalidArgumentException;
import org.kay.contactsengine.exceptions.InvalidContactFormatException;
import org.kay.contactsengine.formatters.ContactFormatter;
import org.kay.contactsengine.model.Contact;
import org.kay.contactsengine.store.ContactsDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.NonNull;
import lombok.Setter;

/**
 * This class is the actual engine that manages various calls to fetch/manage contacts
 * 
 * @author Kartik
 *
 */
public class ContactsEngine {

	@Setter
	@Autowired
	private ContactsDao contactsDao;

	private AnnotationConfigApplicationContext context;
	private boolean initialized = false;
		
	public void Init() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(ContactsEngineConfig.class);
		context.refresh();
		this.context = context;
		this.initialized = true;
	}
	
	public List<Contact> searchContact(@NonNull final String searchField,@NonNull final String searchValue) 
			throws EngineNotInitializedException {
		if (!initialized) {
			throw new EngineNotInitializedException();
		}
		
		Field contactFields[] = Contact.class.getDeclaredFields();
		for (Field contactField:contactFields) {
			if (searchField.equalsIgnoreCase(contactField.getName())) {
				return contactsDao.search(searchField,searchValue);
			}
		}
		return null;
	}
	
	public void addContact(final Contact contact) throws ContactDataAddException, EngineNotInitializedException {
		if (!initialized) {
			throw new EngineNotInitializedException();
		}
		
		contactsDao.addContact(contact);
	}
	
	public void deleteContact(final Contact contact) throws ContactDeleteException, EngineNotInitializedException{
		if (!initialized) {
			throw new EngineNotInitializedException();
		}
		
		contactsDao.deleteContact(contact);
	}

	public List<Contact> getAllContacts() throws ContactsDataAccessException, EngineNotInitializedException {
		if (!initialized) {
			throw new EngineNotInitializedException();
		}
		
		return contactsDao.getAllContacts();
	}

	public void updateContact(Contact oldContact, Contact newContact) throws ContactUpdateException, EngineNotInitializedException {
		if (!initialized) {
			throw new EngineNotInitializedException();
		}
		
		contactsDao.updateContact(oldContact,newContact);
		
	}

	public void exportContacts(@NonNull final String filename,@NonNull final String exportFormat,
			final boolean overwrite) throws InvalidArgumentException, FileException, EngineNotInitializedException {
		if (!initialized) {
			throw new EngineNotInitializedException();
		}
		ContactFormatter exportFormatter = null;
		
		try {
			exportFormatter = context.getBean(exportFormat, ContactFormatter.class);
		} catch (BeansException ex) {
			throw new InvalidArgumentException("Invalid format given:" + exportFormat);
		}
		
		exportContactsToFile(filename,exportFormatter, overwrite);
	}

	private void exportContactsToFile(@NonNull final String filename,
			@NonNull final ContactFormatter exportFormatter, final boolean overwrite) throws FileException {
		List<Contact> contacts;
		int batchSize = 2; //TODO should not be hard coded
		int offset = 0;
		File file = new File(filename);
		
		if (file.exists() && !overwrite) {
			throw new FileException("File already exists:" + filename);
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			while (!(contacts = contactsDao.getAllContacts(batchSize,offset)).isEmpty()) {
				offset += contacts.size();
				String exportStr = exportFormatter.toFormat(contacts);
				fos.write(exportStr.getBytes());				
			}
		} catch (FileNotFoundException e) {
			throw new FileException("Cannot access output file:" + filename);
		} catch (IOException ex) {
			throw new FileException("Cannot write to output file:" + filename);
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				throw new FileException("Unable to close output file:" + filename);
			}
		}
	}

	public void importContacts(@NonNull final String importFormat,@NonNull final String importFile) throws InvalidArgumentException, InvalidContactFormatException, ContactDataAddException, FileException {
		ContactFormatter importFormatter;
		try {
			importFormatter = context.getBean(importFormat,ContactFormatter.class);
		} catch (BeansException ex){
			throw new InvalidArgumentException("Invalid format specified for import");
		}
		
		String importData = readFileContents(importFile);
		List<Contact> importedContacts = importFormatter.toContacts(importData);
		contactsDao.addContacts(importedContacts);
	}

	private String readFileContents(@NonNull final String importFile) throws FileException {
		StringBuilder importContentsBuilder = new StringBuilder();
		try {
			BufferedReader fileBufferedReader = new BufferedReader(new FileReader(importFile));
			String line;
			
			while (null != (line = fileBufferedReader.readLine())) {
				importContentsBuilder.append(line);
				importContentsBuilder.append(System.lineSeparator());
			}
		} catch (FileNotFoundException e) {
			throw new FileException("Import file missing");
		} catch (IOException e) {
			throw new FileException("Error while reading the import file");
		}
		
		return importContentsBuilder.toString();
	}
}
