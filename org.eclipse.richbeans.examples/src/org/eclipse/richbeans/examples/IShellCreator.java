/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
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
