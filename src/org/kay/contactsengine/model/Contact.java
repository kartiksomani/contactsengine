/**
 * 
 */
package org.kay.contactsengine.model;

import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Kartik
 *
 */
@NoArgsConstructor
public class Contact {
	private String firstName;
	private String lastName;
	private long id;
	private String phone;
	private String email;
	
	public Contact(@NonNull final String firstName,@NonNull final  String lastName,
			@NonNull final String email,@NonNull final  String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
