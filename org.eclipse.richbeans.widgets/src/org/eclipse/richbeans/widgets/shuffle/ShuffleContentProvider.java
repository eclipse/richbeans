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
package org.eclipse.richbeans.widgets.shuffle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

class ShuffleContentProvider implements IStructuredContentProvider, PropertyChangeListener{

	private List<Object> items;
	
	private final ShuffleConfiguration conf;
	private final String propName;
	    
	private Viewer viewer;

	private boolean selectEnd;
	
	public ShuffleContentProvider(ShuffleConfiguration conf, String propName, boolean selectEnd) {
		this.conf     = conf;
		this.propName = propName;
		this.selectEnd =selectEnd;
		conf.addPropertyChangeListener(propName, this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		viewer.getControl().getDisplay().syncExec(() -> {
			ISelection sel = viewer.getSelection();
			Object item = sel!=null && sel instanceof StructuredSelection ? ((StructuredSelection)sel).getFirstElement() : null;
			viewer.setInput(evt.getNewValue());
			if (item!=null && items.contains(item)) {
				viewer.setSelection(new StructuredSelection(item));
			} else if (!items.isEmpty()){
				if (selectEnd) {
				    viewer.setSelection(new StructuredSelection(items.get(items.size()-1)));
				} else {
					viewer.setSelection(new StructuredSelection(items.get(0)));
				}
			}
		});
	}

	@Override
	public void dispose() {
		conf.removePropertyChangeListener(propName, this);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		this.items = (List<Object>)newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (items==null) return new Object[]{""};
		return items.toArray(new Object[items.size()]);
	}

}
