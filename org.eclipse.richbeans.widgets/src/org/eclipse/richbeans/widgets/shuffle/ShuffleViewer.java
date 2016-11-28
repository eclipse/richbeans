package org.eclipse.richbeans.widgets.shuffle;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * A class for shuffling between lists
 * 
 * @author Matthew Gerring
 *
 */
public class ShuffleViewer  {

	private static final Logger logger = LoggerFactory.getLogger(ShuffleViewer.class);
	
	private ShuffleConfiguration conf;
	private TableViewer fromTable, toTable;

	public ShuffleViewer(ShuffleConfiguration data) {
		this.conf = data;
	}
	
	/**
	 * Please call dispose which will clean up the listeners
	 * and should be treated as with an SWT Displose.
	 */
	public void dispose() {
		conf.clearListeners();
	}
	
	public Composite createPartControl(Composite parent) {
		
		final Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout(3, false));
		content.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridUtils.removeMargins(content);
		
		this.fromTable = createTable(content, conf.getFromToolipText(), conf.getFromList(), "fromList", conf.getFromLabel(), false, conf.isFromReorder());		
		createButtons(content);
		this.toTable = createTable(content, conf.getToToolipText(), conf.getToList(), "toList", conf.getToLabel(), true, conf.isToReorder());		

		return content;
	}

	private final Composite createButtons(Composite content) {
		
        final Composite ret = new Composite(content, SWT.NONE);
        ret.setLayout(new GridLayout(1, false));
        ret.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
        ret.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridUtils.removeMargins(ret, 0, 20);

        final Button right = new Button(ret, SWT.ARROW |SWT.RIGHT);
        right.setToolTipText("Move item right");
        right.setEnabled(conf.getFromList().size()>0);
        right.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveRight();
        	}
        });
        right.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 50;
        right.setLayoutData(gd);
        PropertyChangeListener pcRight = evt -> {
			final boolean enabled = evt.getNewValue()!=null && evt.getNewValue() instanceof List && ((List)evt.getNewValue()).size()>0;
			fromTable.getControl().getDisplay().syncExec(() -> right.setEnabled(enabled));
        };
        conf.addPropertyChangeListener("fromList", pcRight);
        
        final Button left = new Button(ret, SWT.ARROW |SWT.LEFT);
        left.setToolTipText("Move item left");
        left.setEnabled(conf.getToList().size()>0);
        left.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveLeft();
        	}
        });
        left.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        left.setLayoutData(gd);
        PropertyChangeListener pcLeft = evt -> {
			final boolean enabled = evt.getNewValue()!=null && evt.getNewValue() instanceof List && ((List)evt.getNewValue()).size()>0;
			fromTable.getControl().getDisplay().syncExec(() -> left.setEnabled(enabled));
        };
        conf.addPropertyChangeListener("toList", pcLeft);
   
        return ret;
	}

	private void moveRight() {
		moveHorizontal(fromTable, toTable, "fromList", conf.getFromList(), "toList", conf.getToList());
	}

	private void moveLeft() {
		moveHorizontal(toTable, fromTable, "toList", conf.getToList(), "fromList", conf.getFromList());
	}

	private void moveHorizontal(TableViewer aTable, TableViewer bTable, String aName, List<Object> a, String bName, List<Object> b) {
		
		Object sel = getSelection(aTable);
		if (sel==null) return;
		
		int aindex = a.indexOf(sel);
		List<Object> rem = new ArrayList<>(a);
		rem.remove(sel);
		
		List<Object> add = new ArrayList<>(b);
		Object existing = getSelection(bTable);
		int bindex = add.size();
		if (existing!=null) {
			bindex = add.indexOf(existing);
		}
		try {
		    add.add(bindex+1, sel);
		} catch (IndexOutOfBoundsException ne) {
			add.add(sel);
		}
		
		try {
			RichBeanUtils.setBeanValue(conf, aName, rem); // Set the value of the rem list and property name 'aName' in object conf
			RichBeanUtils.setBeanValue(conf, bName, add); // Set the value of the add list and property name 'bName' in object conf
	
			bTable.setSelection(new StructuredSelection(sel));
			if (rem.size()>0) {
				aindex--;
				if (aindex<0) aindex=0;
				aTable.setSelection(new StructuredSelection(rem.get(aindex)));
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


	private final TableViewer createTable(Composite parent, 
			                              String tooltip, 
			                              List<Object> items, 
			                              String propName,
			                              String slabel,
			                              boolean selectFromEnd, 
			                              boolean allowReorder) {
		
		Composite content = new Composite(parent, SWT.NONE);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
		content.setLayout(new GridLayout(1, false));
		content.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		
		if (slabel!=null) {
			final Label label = new Label(content, SWT.NONE);
			label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));	
			label.setText(slabel);
			label.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		}
				
		TableViewer ret = new TableViewer(content, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		ret.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
		((Table)ret.getControl()).setToolTipText(tooltip); // NOTE This can get clobbered if we used tooltips inside the table.
		
		final ShuffleContentProvider prov = new ShuffleContentProvider(conf, propName, selectFromEnd);
		ret.setContentProvider(prov);
		
		ret.setInput(items);
		
		Composite buttons = new Composite(content, SWT.NONE);
		buttons.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		buttons.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		buttons.setLayout(new GridLayout(2, true));
		GridUtils.removeMargins(buttons, 20, 0);
		
		Button down = new Button(buttons, SWT.ARROW | SWT.DOWN);
		down.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		down.setEnabled(false);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 50;
		down.setLayoutData(gd);
		down.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveVertical(ret, propName, 1);
        	}
        });
		
		Button up = new Button(buttons, SWT.ARROW | SWT.UP);
		up.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		up.setEnabled(false);
		up.setLayoutData(gd);	
		up.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveVertical(ret, propName, -1);
        	}
        });
		if (!allowReorder) { // This cannot be changed later
			down.setVisible(false);
			up.setVisible(false);
		}
		
        PropertyChangeListener pcLeft = evt -> {
			final boolean enabled = evt.getNewValue()!=null && evt.getNewValue() instanceof List && ((List)evt.getNewValue()).size()>1;
			fromTable.getControl().getDisplay().syncExec(() -> {
				down.setEnabled(enabled);
				up.setEnabled(enabled);
			});
        };
        conf.addPropertyChangeListener(propName, pcLeft);


		return ret;
	}

	private void moveVertical(TableViewer viewer, String propName, int moveAmount) {
		
		try {
			Object item = getSelection(viewer);
			if (item==null) return;
	
			List<Object> items = new ArrayList<>((List<Object>)RichBeanUtils.getBeanValue(conf, propName));
			final int index = items.indexOf(item);
			item = items.remove(index);
	
			int newIndex = index + moveAmount;
			if (newIndex<0) newIndex = 0;
			if (newIndex>items.size()) newIndex = items.size();
			items.add(newIndex, item);
	
			RichBeanUtils.setBeanValue(conf, propName, items);
			viewer.setSelection(new StructuredSelection(item));
		} catch (Exception ne) {
			logger.error("Problem moving veritcally!", ne);
		}
	}
}
