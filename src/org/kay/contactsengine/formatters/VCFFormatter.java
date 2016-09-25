package org.kay.contactsengine.formatters;

import java.util.ArrayList;
import java.util.List;

import org.kay.contactsengine.exceptions.InvalidContactFormatException;
import org.kay.contactsengine.model.Contact;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.io.chain.ChainingTextStringParser;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.StructuredName;
import lombok.NonNull;

public class VCFFormatter implements ContactFormatter {

	@Override
	public String toFormat(Contact contact) {
		VCard vcard = new VCard();
		StructuredName structuredName = new StructuredName();
		structuredName.setFamily(contact.getLastName());
		structuredName.setGiven(contact.getFirstName());
		vcard.setStructuredName(structuredName);
		vcard.setFormattedName(contact.getFirstName() + " " + contact.getLastName());
		if (!contact.getEmail().isEmpty()) {
			vcard.addEmail(contact.getEmail(), EmailType.HOME);
		}
		
		if (!contact.getPhone().isEmpty()) {
			vcard.addTelephoneNumber(contact.getPhone(),TelephoneType.HOME);
		}
		
		String vcardStr = Ezvcard.write(vcard).version(VCardVersion.V4_0).go();;
		return vcardStr;
	}

	@Override
	public String toFormat(List<Contact> contacts) {
		StringBuilder vcardStrBuilder = new StringBuilder();
		
		for (Contact contact:contacts) {
			String vcardStr = toFormat(contact);
			vcardStrBuilder.append(vcardStr);
		}
		return vcardStrBuilder.toString();
	}

	@Override
	public List<Contact> toContacts(@NonNull final String str) throws InvalidContactFormatException {
		List<VCard> vcards = Ezvcard.parse(str).all();
		List<Contact> contacts = new ArrayList<Contact>();
		for (VCard vcard:vcards) {
			Contact contact = new Contact();
			String firstName = vcard.getStructuredName().getGiven();
			String lastName = vcard.getStructuredName().getFamily();
			contact.setFirstName(firstName);
			contact.setLastName(lastName);

			if (!vcard.getEmails().isEmpty()) {
				String email = vcard.getEmails().get(0).getValue();
				contact.setEmail(email);
			}
			if (!vcard.getTelephoneNumbers().isEmpty()) {
				String phone = vcard.getTelephoneNumbers().get(0).getText();
				contact.setPhone(phone);
			}
			contacts.add(contact);
		}
		return contacts;
	}

	@Override
	public Contact toContact(@NonNull final String str) throws InvalidContactFormatException {
		VCard contactCard = Ezvcard.parse(str).first();
		Contact contact = new Contact();
		String firstName = contactCard.getStructuredName().getGiven();
		String lastName = contactCard.getStructuredName().getFamily();
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		
		if (!contactCard.getEmails().isEmpty()) {
			String email = contactCard.getEmails().get(0).getValue();
			contact.setEmail(email);
		}
		if (!contactCard.getTelephoneNumbers().isEmpty()) {
			String phone = contactCard.getTelephoneNumbers().get(0).getText();
			contact.setPhone(phone);
		}
		return contact;
	}

}
