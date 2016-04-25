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
package org.eclipse.richbeans.widgets.decorator;

import org.eclipse.swt.widgets.Text;

public class FileDecorator extends RegexDecorator {

	public FileDecorator(Text text) {
		this(text, createRegex(isWindowsOS()));
	}
	
	/**
	 * 
	 * @param text
	 * @param iswindows false for linux and MacOS paths without the drive letter.
	 */
	public FileDecorator(Text text, boolean iswindows) {
		this(text, createRegex(iswindows));
	}
	
	public FileDecorator(Text text, String regex) {
		super(text, regex);
		setAllowInvalidValues(true);
	}

	private static String createRegex(boolean isWin) {
		return isWin ? "([a-zA-Z]\\:)\\\\[a-zA-Z_0-9\\\\\\-]+" : "\\/[a-zA-Z_0-9\\/-]+";
	}

	static private boolean isWindowsOS() {
		return (System.getProperty("os.name").indexOf("Windows") == 0);
	}

	public boolean isError() {
        if (allowInvalidValues) return false;
        return super.isError();
	}
}
