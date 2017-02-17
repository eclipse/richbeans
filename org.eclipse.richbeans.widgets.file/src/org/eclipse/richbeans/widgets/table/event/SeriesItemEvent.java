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

import java.util.EventObject;

import org.eclipse.richbeans.widgets.table.ISeriesItemDescriptor;


/**
 * 
 * The source is the ISeriesItemDescriptor so a called to getSeriesItemObject() is
 * required to get the object added/removed.
 * 
 * @author Matthew Gerring
 *
 */
public class SeriesItemEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5681385021149474679L;
	
	/**
	 * The source is the series item object
	 * 
	 * @param source
	 * @throws Exception 
	 */
	public SeriesItemEvent(ISeriesItemDescriptor descriptor) {
		super(descriptor);
	}

	public ISeriesItemDescriptor getDescriptor() {
		return (ISeriesItemDescriptor)getSource();
	}

}
