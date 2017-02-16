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
package org.eclipse.richbeans.widgets.table.event;

import java.util.EventListener;

public interface SeriesItemListener extends EventListener {

	/**
	 * Called when a new item is added.
	 * @param evt
	 */
	void itemAdded(SeriesItemEvent evt);

	/**
	 * Called when an existing item is removed
	 * @param evt
	 */
	void itemRemoved(SeriesItemEvent evt);
}
