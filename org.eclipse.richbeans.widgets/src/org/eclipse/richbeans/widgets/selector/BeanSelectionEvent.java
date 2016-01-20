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

import java.util.EventObject;


/**
 *
 */
public class BeanSelectionEvent extends EventObject {

	private Object selectedBean;
	private int    selectionIndex;

	/**
	 * @param source
	 * @param selectionIndex
	 */
	public BeanSelectionEvent(Object source, final int selectionIndex, final Object selectedBean) {
		super(source);
		this.selectionIndex = selectionIndex;
		this.selectedBean   = selectedBean;
	}

	/**
	 * @return Returns the selectedBean.
	 */
	public Object getSelectedBean() {
		return selectedBean;
	}

	/**
	 * @param selectedBean The selectedBean to set.
	 */
	public void setSelectedBean(Object selectedBean) {
		this.selectedBean = selectedBean;
	}

	/**
	 * @return Returns the selectionIndex.
	 */
	public int getSelectionIndex() {
		return selectionIndex;
	}

	/**
	 * @param selectionIndex The selectionIndex to set.
	 */
	public void setSelectionIndex(int selectionIndex) {
		this.selectionIndex = selectionIndex;
	}

}
