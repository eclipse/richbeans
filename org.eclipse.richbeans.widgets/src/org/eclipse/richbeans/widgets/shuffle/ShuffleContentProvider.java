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
	
	public ShuffleContentProvider(ShuffleConfiguration conf, String propName) {
		this.conf     = conf;
		this.propName = propName;
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
				viewer.setSelection(new StructuredSelection(items.get(0)));
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
