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
 * This interface defines optional features that can be applied to a ListEditor. 
 */
public interface ListEditorUI {

	/**
	 * @param listEditor the ListEditor making the notification
	 */
	public void notifySelected(ListEditor listEditor);
	
	/**
	 * @param listEditor the ListEditor making the request
	 * @return true/false if delete is possible
	 */
	public boolean isDeleteAllowed(ListEditor listEditor);
	
	/**
	 * @param listEditor the ListEditor making the request
	 * @return true/false if add is possible
	 */
	public boolean isAddAllowed(ListEditor listEditor);
	
	/**
	 * @param listEditor the ListEditor making the request
	 * @return true/false if reorder is possible
	 */
	public boolean isReorderAllowed(ListEditor listEditor);

}
