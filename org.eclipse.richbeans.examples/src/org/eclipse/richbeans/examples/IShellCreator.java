package org.eclipse.richbeans.examples;

import org.eclipse.richbeans.widgets.util.SWTUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Used for testing
 * 
 * @author Matthew Gerring
 *
 */
public interface IShellCreator {
	
	/**
	 * Creates a display and opens the centered shell produced by createShell(...) 
	 * @throws Exception
	 */
	default void open() throws Exception {
		IShellCreator runner = getClass().newInstance();
        Shell shell = runner.createShell(Display.getDefault());
		SWTUtils.showCenteredShell(shell);
	}
	
	/**
	 * Implement the shell to be shown with the example.
	 * @param display
	 * @return
	 * @throws Exception
	 */
	Shell createShell(Display display) throws Exception;

}
