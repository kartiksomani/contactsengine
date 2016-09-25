package org.kay.contactsengine.exceptions;

import lombok.NonNull;

public class InvalidArgumentException extends Exception {

	public InvalidArgumentException(@NonNull final String string) {
		super(string);
	}

}
