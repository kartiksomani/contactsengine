package org.kay.contactsengine.exceptions;

import lombok.NonNull;

public class EngineNotInitializedException extends Exception {
	public EngineNotInitializedException() {
		super("Contact engine not initialized");
	}
}
