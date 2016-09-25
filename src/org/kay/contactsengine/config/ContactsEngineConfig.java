package org.kay.contactsengine.config;

import org.kay.contactsengine.formatters.ContactCSVFormatter;
import org.kay.contactsengine.formatters.ContactFormatter;
import org.kay.contactsengine.formatters.VCFFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactsEngineConfig {
	
	@Bean(name="CSV")
	public ContactFormatter csvContactFormatter() {
		return new ContactCSVFormatter();
	}
	
	@Bean(name="VCF")
	public ContactFormatter vcfContactFormatter() {
		return new VCFFormatter();
	}
}
