package org.eclipse.richbeans.examples;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Used for testing
 * 
 * @author Matthew Gerring
 *
 */
public interface IShellCreator {
	
	Shell createShell(Display display) throws Exception;

}
