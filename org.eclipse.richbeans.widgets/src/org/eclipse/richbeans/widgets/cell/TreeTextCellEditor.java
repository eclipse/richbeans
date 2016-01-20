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

package org.eclipse.richbeans.widgets.cell;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Matthew Gerring
 *
 */
public class TreeTextCellEditor extends TextCellEditor {

	/**
	 * @param parent
	 * @param flags
	 */
	public TreeTextCellEditor(Composite parent, int flags) {
		super(parent,flags);
	}

	/**
	 * @param listener
	 */
	public void addVerifyListener(final VerifyListener listener) {
		text.addVerifyListener(listener);
	}
	/**
	 * @param listener
	 */
	public void removeVerifyListener(final VerifyListener listener) {
		text.removeVerifyListener(listener);
	}

}

	