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

package org.eclipse.richbeans.widgets.selector;


/**
 * Convenience class that maintains defaults for how classes in the ListEditor
 * use ListEditorUI Interface
 */
public class ListEditorUIAdapter implements ListEditorUI {
	
	private static ListEditorUIAdapter defaultAdapter;
	
	/**
	 * Return instance of ListEditorUI implementing the default values
	 * @return instance of ListEditorUI implementing the default values
	 */
	public static ListEditorUIAdapter getDefault() {
		if (defaultAdapter == null)
			defaultAdapter = new ListEditorUIAdapter();
		return defaultAdapter;
	}
	
	@Override
	public boolean isAddAllowed(ListEditor listEditor) {
		return true;
	}

	@Override
	public boolean isDeleteAllowed(ListEditor listEditor) {
		return true;
	}

	@Override
	public boolean isReorderAllowed(ListEditor listEditor) {
		return true;
	}

	@Override
	public void notifySelected(ListEditor listEditor) {
		// default is to do nothing on notification
	}

}
