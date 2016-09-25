package org.kay.contactsengine;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import lombok.Data;
import lombok.Getter;

@Data
public class CLIArguments {
	static enum Action {
		SEARCH,
		ADD,
		IMPORT,
		EXPORT,
		VIEW				
	}
	
	public static enum SearchField {
		FIRSTNAME,
		LASTNAME,
		EMAIL,
		PHONE
	}
	
	@Argument(required=true,index=0,usage="Action to be performed")
	private Action action;
	
	@Option(name="-f",aliases={"--searchField"},metaVar="VALUE",usage="Field to be searched to find the contact")
	private SearchField searchField;	
	
	@Option(name="-v",aliases={"--searchValue"},metaVar="VALUE",usage="Value of the field to be searched")
	private String searchValue;
	
	@Option(name="-d",aliases={"--contactData"},metaVar="VALUE",usage="Data of the contact to be inserted in JSON format")
	private String contactData;
	
	@Option(name="-r",metaVar="VALUE",usage="Delete the contacts found")
	private boolean removeContacts;
	
	@Option(name="-u",metaVar="VALUE",usage="Update the contact found (works only if one contact is found)")
	private boolean updateContacts;
	
	@Option(name="--format",metaVar="VALUE",usage="File Format")
	private String format;
	
	@Option(name="--filename",metaVar="VALUE",usage="File name")
	private String fileName;
	
	@Option(name="--overwrite",metaVar="VALUE",usage="Overwrite existing file while exporting")
	private boolean overwriteExportTarget;
	
}
