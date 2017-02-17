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

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.richbeans.widgets.table.ISeriesItemDescriptor;

public class SeriesEventDelegate {

	
	private Collection<SeriesItemListener> listeners;
	
	public void addSeriesEventListener(SeriesItemListener listener) {
		if (listeners==null) listeners = new LinkedHashSet<>(7);
		listeners.add(listener);
	}
	
	public void removeSeriesEventListener(SeriesItemListener listener) {
		if (listeners==null) return;
		listeners.remove(listener);
	}

	public void fireItemAdded(ISeriesItemDescriptor descriptor) {
		if (listeners==null) return;
		final SeriesItemEvent evt = new SeriesItemEvent(descriptor);
		SeriesItemListener[] ls = listeners.toArray(new SeriesItemListener[listeners.size()]);
		for (SeriesItemListener listener : ls) listener.itemAdded(evt);
	}

	public void fireItemRemoved(ISeriesItemDescriptor descriptor) {
		if (listeners==null) return;
		final SeriesItemEvent evt = new SeriesItemEvent(descriptor);
		SeriesItemListener[] ls = listeners.toArray(new SeriesItemListener[listeners.size()]);
		for (SeriesItemListener listener : ls) listener.itemRemoved(evt);
	}

	public void clear() {
		if (listeners==null) return;
		listeners.clear();
	}

}
