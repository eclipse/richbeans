package org.eclipse.richbeans.widgets.shuffle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.richbeans.api.reflection.RichBeanUtils;
import org.eclipse.richbeans.widgets.internal.GridUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * A class for shuffling between lists
 * 
 * @author Matthew Gerring
 *
 */
public class ShuffleViewer implements PropertyChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(ShuffleViewer.class);
	
	private ShuffleConfiguration conf;
	private TableViewer fromTable, toTable;
	private Map<String, Widget> buttons;

	public ShuffleViewer(ShuffleConfiguration data) {
		this.conf = data;
		conf.addPropertyChangeListener(this);
		this.buttons = new HashMap<>(3);
	}
	
	/**
	 * Please call dispose which will clean up the listeners
	 * and should be treated as with an SWT Displose.
	 */
	public void dispose() {
		conf.removePropertyChangeListener(this);
	}
	
	public Composite createPartControl(Composite parent) {
		
		final Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout(3, false));
		GridUtils.removeMargins(content);
		
		this.fromTable = createTable(content, conf.getFromToolipText(), conf.getFromList(), "fromList");		
		createButtons(content);
		this.toTable = createTable(content, conf.getToToolipText(), conf.getToList(), "toList");		

		return content;
	}

	private final Composite createButtons(Composite content) {
		
        final Composite ret = new Composite(content, SWT.NONE);
        ret.setLayout(new RowLayout(SWT.VERTICAL));
        new Label(ret, SWT.NONE);
        final Button right = new Button(ret, SWT.ARROW |SWT.RIGHT);
        right.setEnabled(conf.getFromList().size()>0);
        right.setToolTipText("Move item right");
        right.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveRight();
        	}
        });
        buttons.put("fromList", right);
        
        new Label(ret, SWT.NONE);
        final Button left = new Button(ret, SWT.ARROW |SWT.LEFT);
        left.setToolTipText("Move item left");
        left.setEnabled(conf.getToList().size()>0);
        left.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveLeft();
        	}
        });
        buttons.put("toList", left);
        
        new Label(ret, SWT.NONE);
      
        return ret;
	}

	private void moveRight() {
		move(fromTable, toTable, "fromList", conf.getFromList(), "toList", conf.getToList());
	}

	private void moveLeft() {
		move(toTable, fromTable, "toList", conf.getToList(), "fromList", conf.getFromList());
	}

	private void move(TableViewer aTable, TableViewer bTable, String aName, List<Object> a, String bName, List<Object> b) {
		
		Object sel = getSelection(aTable);
		if (sel==null) return;
		
		int index = a.indexOf(sel);
		List<Object> rem = new ArrayList<>(a);
		rem.remove(sel);
		
		List<Object> add = new ArrayList<>(b);
		add.add(sel);
		
		try {
			RichBeanUtils.setBeanValue(conf, aName, rem);
			RichBeanUtils.setBeanValue(conf, bName, add);
	
			bTable.setSelection(new StructuredSelection(sel));
			if (rem.size()>0) {
				index--;
				if (index<0) index=0;
				aTable.setSelection(new StructuredSelection(rem.get(index)));
			}
		} catch (Exception ne) {
			logger.error("Cannot set the values after move!", ne);
		}
	}
	
	
	private Object getSelection(TableViewer viewer) {
		ISelection sel = viewer.getSelection();
		if (sel instanceof StructuredSelection) {
			return ((StructuredSelection)sel).getFirstElement();
		}
		return null;
	}


	private final TableViewer createTable(Composite parent, String tooltip, List<Object> items, String propName) {
		
		TableViewer ret = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		ret.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
		((Table)ret.getControl()).setToolTipText(tooltip); // NOTE This can get clobbered if we used tooltips inside the table.
		
		final ShuffleContentProvider prov = new ShuffleContentProvider(conf, propName);
		ret.setContentProvider(prov);
		
		ret.setInput(items);
		
		return ret;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (buttons.containsKey(evt.getPropertyName())) {
			Button widget = (Button)buttons.get(evt.getPropertyName());
			final boolean enabled = evt.getNewValue()!=null && evt.getNewValue() instanceof List && ((List)evt.getNewValue()).size()>0;
			fromTable.getControl().getDisplay().syncExec(() -> widget.setEnabled(enabled));
		}
	}
}
