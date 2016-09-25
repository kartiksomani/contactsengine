/**
 * 
 */
package org.kay.contactsengine;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author Kartik
 *
 */
public class ContactsMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CLIArguments cliArguments = new CLIArguments();
		CmdLineParser cmdLineParser = new CmdLineParser(cliArguments);
		ContactsEngineExecutor executor = new ContactsEngineExecutor();
		try {
			cmdLineParser.parseArgument(args);
			executor.execute(cliArguments);
		} catch (CmdLineException e) {
			cmdLineParser.printUsage(System.out);
		}
	}

}
