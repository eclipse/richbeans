/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.richbeans.widgets.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Matthew Gerring
 *
 */
public class SWTUtils {

	/**
	 * This method will centre a Shell on the primary view device.
	 * 
	 * The method will show the shell in the thread which calls the method
	 * and not return until the user closes the shell.
	 * 
	 * @param shell
	 */
	public static final void showCenteredShell(final Shell shell) {

		final Display display  = shell.getDisplay();
		final Monitor primary  = shell.getDisplay().getPrimaryMonitor();
		
		final Rectangle bounds = primary.getBounds();
		final Rectangle rect   = shell.getBounds();

		final int x = bounds.x + (bounds.width - rect.width) / 2;
		final int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) shell.getDisplay().sleep();
		}

		display.dispose();
	}

}

