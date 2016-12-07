package org.eclipse.richbeans.widgets.shuffle;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * A class for shuffling between lists
 * 
 * @author Matthew Gerring
 * @param T type of items in the ShuffleViewer, for instance String
 */
public class ShuffleViewer<T>  {

	private static final Logger logger = LoggerFactory.getLogger(ShuffleViewer.class);
	
	private ShuffleConfiguration<T> conf;
	private TableViewer fromTable, toTable;

	private Composite control;
	private List<IShuffleListener> shuffleListeners;

	public ShuffleViewer(ShuffleConfiguration<T> data) {
		this.conf = data;
	}
	
	/**
	 * Please call dispose which will clean up the listeners
	 * and should be treated as with an SWT Displose.
	 */
	public void dispose() {
		conf.clearListeners();
		control.dispose();
	}
	
	public Composite createPartControl(Composite parent) {
		
		this.control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout(3, false));
		control.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridUtils.removeMargins(control);
		
		this.fromTable = createTable(control, conf.getFromToolipText(), conf.getFromList(), "fromList", conf.getFromLabel(), false, conf.isFromReorder());		
		createButtons(control);
		this.toTable = createTable(control, conf.getToToolipText(), conf.getToList(), "toList", conf.getToLabel(), true, conf.isToReorder());		

		return control;
	}
	
	public void addShuffleListener(IShuffleListener l) {
		if (shuffleListeners==null) shuffleListeners = Collections.synchronizedList(new ArrayList<>(7));
		shuffleListeners.add(l);
	}
	
	public void removeShuffleListener(IShuffleListener l) {
		if (shuffleListeners==null) return;
		shuffleListeners.remove(l);
	}
	
	/**
	 * 
	 * @param evt
	 * @return items if one or more listeners set them, the items unchanged if no listener changed them.
	 */
	protected List<T> firePreShuffle(ShuffleEvent evt) {
		if (shuffleListeners==null) return (List<T>)evt.getItems();
		IShuffleListener[] la = shuffleListeners.toArray(new IShuffleListener[shuffleListeners.size()]);
		List<T> ret = (List<T>)evt.getItems();
		for (IShuffleListener iShuffleListener : la) {
			boolean ok = iShuffleListener.preShuffle(evt);
			if (!ok) throw new RuntimeException("Unable to call preshuffle, aborting event!");
			if (evt.isItemsSet()) ret = (List<T>)evt.getItems(); // Ret can be overwritten, Javadoc on IShuffleListener explains this.
		}
		return ret;
	}
	/**
	 * 
	 * @param evt
	 * @return items if one or more listeners set them, null if no listener changed them.
	 */
	protected void firePostShuffle(ShuffleEvent evt) {
		if (shuffleListeners==null) return;
		IShuffleListener[] la = shuffleListeners.toArray(new IShuffleListener[shuffleListeners.size()]);
		for (IShuffleListener iShuffleListener : la) iShuffleListener.postShuffle(evt);
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
		ShuffleBean<T> from = new ShuffleBean<T>(fromTable, "fromList", conf.getFromList(), ShuffleDirection.DELETE);
		ShuffleBean<T> to = new ShuffleBean<T>(toTable, "toList", conf.getToList(), ShuffleDirection.LEFT_TO_RIGHT);
		moveHorizontal(from, to);
	}

	private void moveLeft() {
		ShuffleBean<T> from = new ShuffleBean<T>(toTable, "toList", conf.getToList(), ShuffleDirection.DELETE);
		ShuffleBean<T> to = new ShuffleBean<T>(fromTable, "fromList", conf.getFromList(), ShuffleDirection.RIGHT_TO_LEFT);
		moveHorizontal(from, to);
	}

	private void moveHorizontal(ShuffleBean<T> abean, ShuffleBean<T> bbean) {
		
		T sel = getSelection(abean.getTable());
		if (sel==null) return;
		
		List<T> a = abean.getList();
		int aindex = a.indexOf(sel);
		List<T> rem = new ArrayList<>(a);
		rem.remove(sel);
		
		List<T> add = new ArrayList<>(bbean.getList());
		T existing = getSelection(bbean.getTable());
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
			rem = firePreShuffle(new ShuffleEvent(this, abean.getDirection(), rem));
			RichBeanUtils.setBeanValue(conf, abean.getName(), rem); // Set the value of the rem list and property name 'aName' in object conf
			firePostShuffle(new ShuffleEvent(this, abean.getDirection(), rem));
			
			add = firePreShuffle(new ShuffleEvent(this, bbean.getDirection(), add));
			RichBeanUtils.setBeanValue(conf, bbean.getName(), add); // Set the value of the add list and property name 'bName' in object conf
	
			bbean.getTable().setSelection(new StructuredSelection(sel));
			if (rem.size()>0) {
				aindex--;
				if (aindex<0) aindex=0;
				abean.getTable().setSelection(new StructuredSelection(rem.get(aindex)));
			}
			firePostShuffle(new ShuffleEvent(this, bbean.getDirection(), add));
			
		} catch (Exception ne) {
			logger.error("Cannot set the values after move!", ne);
		}
	}
	
	
	private T getSelection(TableViewer viewer) {
		ISelection sel = viewer.getSelection();
		if (sel instanceof StructuredSelection) {
			return (T)((StructuredSelection)sel).getFirstElement();
		}
		return null;
	}


	private final TableViewer createTable(Composite parent, 
			                              String tooltip, 
			                              List<T> items, 
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
        		ShuffleDirection direction = propName.startsWith("from") ? ShuffleDirection.LEFT_DOWN : ShuffleDirection.RIGHT_DOWN;
        		moveVertical(ret, propName, 1, direction);
        	}
        });
		
		Button up = new Button(buttons, SWT.ARROW | SWT.UP);
		up.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		up.setEnabled(false);
		up.setLayoutData(gd);	
		up.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
           		ShuffleDirection direction = propName.startsWith("from") ? ShuffleDirection.LEFT_UP : ShuffleDirection.RIGHT_UP;
        		moveVertical(ret, propName, -1, direction);
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

	private void moveVertical(TableViewer viewer, String propName, int moveAmount, ShuffleDirection direction) {
		
		try {
			T item = getSelection(viewer);
			if (item==null) return;
	
			List<T> items = new ArrayList<T>((Collection<T>)RichBeanUtils.getBeanValue(conf, propName));
			final int index = items.indexOf(item);
			item = items.remove(index);
	
			int newIndex = index + moveAmount;
			if (newIndex<0) newIndex = 0;
			if (newIndex>items.size()) newIndex = items.size();
			items.add(newIndex, item);
	
			items = firePreShuffle(new ShuffleEvent(this, direction, items));
			RichBeanUtils.setBeanValue(conf, propName, items);
			viewer.setSelection(new StructuredSelection(item));
			firePostShuffle(new ShuffleEvent(this, direction, items));
			
		} catch (Exception ne) {
			logger.error("Problem moving veritcally!", ne);
		}
	}
	
	public Control getControl() {
		return control;
	}

	public void setLayoutData(GridData data) {
		control.setLayoutData(data);
	}

	public void setFocus() {
		fromTable.getControl().setFocus();
	}

	/**
	 * Sets this menu on the 
	 * @param rightClick
	 */
	public void setMenu(MenuManager rightClick) {
		control.setMenu(rightClick.createContextMenu(control));
		fromTable.getControl().setMenu(rightClick.createContextMenu(fromTable.getControl()));
		toTable.getControl().setMenu(rightClick.createContextMenu(toTable.getControl()));
	}

	/**
	 * 
	 * @return the mutable model, changing items in the config causes the UI to change
	 */
	public ShuffleConfiguration getShuffleConfiguration() {
		return conf;
	}
}
