package org.eclipse.richbeans.widgets.shuffle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

class ShuffleContentProvider implements IStructuredContentProvider, PropertyChangeListener{

	private List<Object> items;
	
	private final ShuffleConfiguration conf;
	private final String propName;
	    
	private Viewer viewer;
	
	public ShuffleContentProvider(ShuffleConfiguration conf, String propName) {
		this.conf     = conf;
		this.propName = propName;
		conf.addPropertyChangeListener(propName, this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		viewer.getControl().getDisplay().syncExec(() -> viewer.setInput(evt.getNewValue()));
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
