/**
 * 
 */
package org.kay.contactsengine.store.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.kay.contactsengine.exceptions.ContactDataAddException;
import org.kay.contactsengine.exceptions.ContactDeleteException;
import org.kay.contactsengine.exceptions.ContactUpdateException;
import org.kay.contactsengine.exceptions.ContactsDataAccessException;
import org.kay.contactsengine.model.Contact;
import org.kay.contactsengine.store.ContactsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import lombok.NonNull;
/**
 * This class implements the dao functions for SQL based databases
 * @author Kartik
 *
 */
public class SQLTemplate implements ContactsDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	
	private class ContactRowMapper implements RowMapper<Contact> {

		@Override
		public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
			Contact contact = new Contact();
			contact.setEmail(rs.getString("email"));
			contact.setFirstName(rs.getString("firstname"));
			contact.setLastName(rs.getString("lastname"));
			contact.setPhone(rs.getString("phone"));
			contact.setId(rs.getLong("id"));
			return contact;
		}
		
	}
	
	@Autowired
	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public List<Contact> search(final String field,final String value) {
		String sql = "Select * from contacts where " + field + " like '%" + value + "%'";
		List<Contact> contacts = jdbcTemplate.query(sql,new ContactRowMapper());
		return contacts;
	}

	@Override
	public void addContact(Contact contact) throws ContactDataAddException {
		String sql = "insert into contacts(firstname,lastname,phone,email) values (?,?,?,?)";
		try {
		this.jdbcTemplate.update(sql,contact.getFirstName(),contact.getLastName(),contact.getPhone(),
				contact.getEmail());
		} catch (DataAccessException ex){
			throw new ContactDataAddException("Unable to add contact data",
					Collections.singletonList(contact));
		}		
	}

	@Override
	public void deleteContact(@NonNull final Contact contact) throws ContactDeleteException {
		String sql = "delete from contacts where id=" + contact.getId();
		try {
			jdbcTemplate.update(sql);
		} catch (DataAccessException ex) {
			throw new ContactDeleteException();
		}
		
		
	}

	@Override
	public List<Contact> getAllContacts() throws ContactsDataAccessException {
		String sql = "select * from contacts";
		List<Contact> contacts = jdbcTemplate.query(sql,new ContactRowMapper());
		return contacts;		
	}

	@Override
	public void updateContact(Contact oldContact, Contact newContact) throws ContactUpdateException {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update contacts");
		sqlBuilder.append(" set ");
		
		//TODO will using ?? here work?
		if (null != newContact.getFirstName()) {
			sqlBuilder.append("firstName='");
			sqlBuilder.append(newContact.getFirstName());
			sqlBuilder.append("',");
		}
		if (null != newContact.getLastName()) {
			sqlBuilder.append("lastName='");
			sqlBuilder.append(newContact.getLastName());
			sqlBuilder.append("',");
		}
		if (null != newContact.getPhone()) {
			sqlBuilder.append("phone='");
			sqlBuilder.append(newContact.getPhone());
			sqlBuilder.append("',");
		}
		if (null != oldContact.getEmail()) {
			sqlBuilder.append("email='");
			sqlBuilder.append(newContact.getEmail());
			sqlBuilder.append("'");
		}
		
		sqlBuilder.append(" where id=");
		sqlBuilder.append(oldContact.getId());
		
		try {
			this.jdbcTemplate.update(sqlBuilder.toString());
		} catch (DataAccessException ex) {
			throw new ContactUpdateException();
		}		
	}

	@Override
	public List<Contact> getAllContacts(final int batchSize,final int offset) {
		String sql = "select * from contacts limit " + batchSize + " offset " + offset;
		List<Contact> contacts = jdbcTemplate.query(sql,new ContactRowMapper());
		return contacts;
	}

	@Override
	public void addContacts(@NonNull final List<Contact> contacts) throws ContactDataAddException {
		List<Contact> failedContacts = new ArrayList<Contact>();
		for (Contact contact:contacts) {
			try {
				addContact(contact);
			} catch (ContactDataAddException ex) {
				failedContacts.add(contact);
			}
		}
		
		if (!failedContacts.isEmpty()) {
			throw new ContactDataAddException("Failed to add some contacts",failedContacts);
		}
	}
}
