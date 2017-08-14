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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
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
	private List<IShuffleListener<T>> shuffleListeners;

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
	
	public void addShuffleListener(IShuffleListener<T> l) {
		if (shuffleListeners==null) shuffleListeners = Collections.synchronizedList(new ArrayList<>(7));
		shuffleListeners.add(l);
	}
	
	public void removeShuffleListener(IShuffleListener<T> l) {
		if (shuffleListeners==null) return;
		shuffleListeners.remove(l);
	}
	
	/**
	 * 
	 * @param evt
	 * @return items if one or more listeners set them, the items unchanged if no listener changed them.
	 */
	protected List<T> firePreShuffle(ShuffleEvent<T> evt) {
		if (shuffleListeners==null) return (List<T>)evt.getItems();
		@SuppressWarnings("unchecked")
		IShuffleListener<T>[] la = shuffleListeners.toArray(new IShuffleListener[shuffleListeners.size()]);
		List<T> ret = (List<T>)evt.getItems();
		for (IShuffleListener<T> iShuffleListener : la) {
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
	protected void firePostShuffle(ShuffleEvent<T> evt) {
		if (shuffleListeners==null) return;
		@SuppressWarnings("unchecked")
		IShuffleListener<T>[] la = shuffleListeners.toArray(new IShuffleListener[shuffleListeners.size()]);
		for (IShuffleListener<T> iShuffleListener : la) iShuffleListener.postShuffle(evt);
	}

	private final Composite createButtons(Composite content) {
		
        final Composite ret = new Composite(content, SWT.NONE);
        ret.setLayout(new GridLayout(1, false));
        ret.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
        ret.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridUtils.removeMargins(ret, 0, 20);

        final Button rightButton = new Button(ret, SWT.ARROW |SWT.RIGHT);
        rightButton.setToolTipText("Move item right");
        rightButton.setEnabled(conf.getFromList().size()>0);
        rightButton.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveRight();
        	}
        });
        rightButton.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 50;
        rightButton.setLayoutData(gd);
        PropertyChangeListener pcRight = evt -> {
			final boolean enabled = evt.getNewValue() instanceof List && ((List<?>)evt.getNewValue()).size() > 0;
			fromTable.getControl().getDisplay().syncExec(() -> rightButton.setEnabled(enabled));
        };
        conf.addPropertyChangeListener("fromList", pcRight);
        
        final Button leftButton = new Button(ret, SWT.ARROW |SWT.LEFT);
        leftButton.setToolTipText("Move item left");
        leftButton.setEnabled(conf.getToList().size()>0);
        leftButton.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		moveLeft();
        	}
        });
        leftButton.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        leftButton.setLayoutData(gd);
        PropertyChangeListener pcLeft = evt -> {
			final boolean enabled = evt.getNewValue() instanceof List && ((List<?>)evt.getNewValue()).size() > 0;
			fromTable.getControl().getDisplay().syncExec(() -> leftButton.setEnabled(enabled));
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

	private void moveHorizontal(ShuffleBean<T> from, ShuffleBean<T> to) {
		// TODO handle multi-selection
		@SuppressWarnings("unchecked")
		final List<T> itemsToMove = (List<T>) from.getTable().getStructuredSelection().toList();
		if (itemsToMove.isEmpty()) return;
		
		// copy the 'from' list and remove the selected item
		final List<T> fromList = from.getList();
		final int fromIndex = fromList.indexOf(itemsToMove.get(0));
		List<T> newFromList = new ArrayList<>(fromList);
		newFromList.removeAll(itemsToMove);

		// insert the item to the 'to' list after the selected item in that list
		List<T> newToList = new ArrayList<>(to.getList());
		@SuppressWarnings("unchecked")
		final T insertAfter = (T) to.getTable().getStructuredSelection().getFirstElement();
		if (insertAfter == null) {
			newToList.addAll(itemsToMove);
		} else {
			int toIndex = newToList.indexOf(insertAfter) + 1;
			newToList.addAll(toIndex, itemsToMove);
		}
		
		try {
			newFromList = firePreShuffle(new ShuffleEvent<T>(this, from.getDirection(), newFromList));
			// Set the value of the rem list and property name 'aName' in object conf
			RichBeanUtils.setBeanValue(conf, from.getName(), newFromList);
			firePostShuffle(new ShuffleEvent<T>(this, from.getDirection(), newFromList));
			
			newToList = firePreShuffle(new ShuffleEvent<T>(this, to.getDirection(), newToList));
			// Set the value of the add list and property name 'bName' in object conf
			RichBeanUtils.setBeanValue(conf, to.getName(), newToList);
	
			// set the selection in the 'to' list to the inserted item
			to.getTable().setSelection(new StructuredSelection(itemsToMove));
			
			// if the 'from' list is not empty, selected the previous item
			if (!newFromList.isEmpty()) {
				final int fromNewSelectionIndex = Math.max(0, fromIndex - 1);
				final T newSelectedItem = newFromList.get(fromNewSelectionIndex);
				from.getTable().setSelection(new StructuredSelection(newSelectedItem));
			}
			firePostShuffle(new ShuffleEvent<T>(this, to.getDirection(), newToList));
			
		} catch (Exception ne) {
			logger.error("Cannot set the values after move!", ne);
		}
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
				
		TableViewer tableViewer = new TableViewer(content, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));	
		((Table)tableViewer.getControl()).setToolTipText(tooltip); // NOTE This can get clobbered if we used tooltips inside the table.
		
		final ShuffleContentProvider<T> prov = new ShuffleContentProvider<T>(conf, propName, selectFromEnd);
		tableViewer.setContentProvider(prov);
		
		tableViewer.setInput(items);
		
		Composite buttons = new Composite(content, SWT.NONE);
		buttons.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		buttons.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		buttons.setLayout(new GridLayout(2, true));
		GridUtils.removeMargins(buttons, 20, 0);
		
		Button downButton = new Button(buttons, SWT.ARROW | SWT.DOWN);
		downButton.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		downButton.setEnabled(false);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 50;
		downButton.setLayoutData(gd);
		downButton.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
        		ShuffleDirection direction = propName.startsWith("from") ? ShuffleDirection.LEFT_DOWN : ShuffleDirection.RIGHT_DOWN;
        		moveVertical(tableViewer, propName, 1, direction);
        	}
        });
		
		Button upButton = new Button(buttons, SWT.ARROW | SWT.UP);
		upButton.setBackground(content.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		upButton.setEnabled(false);
		upButton.setLayoutData(gd);	
		upButton.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent e) {
           		ShuffleDirection direction = propName.startsWith("from") ? ShuffleDirection.LEFT_UP : ShuffleDirection.RIGHT_UP;
        		moveVertical(tableViewer, propName, -1, direction);
        	}
        });
		if (!allowReorder) { // This cannot be changed later
			downButton.setVisible(false);
			upButton.setVisible(false);
		}
		
        PropertyChangeListener pcLeft = evt -> {
			final boolean enabled = evt.getNewValue() instanceof List && ((List<?>) evt.getNewValue()).size() > 1;
			fromTable.getControl().getDisplay().syncExec(() -> {
				downButton.setEnabled(enabled);
				upButton.setEnabled(enabled);
			});
        };
        conf.addPropertyChangeListener(propName, pcLeft);

		return tableViewer;
	}

	private void moveVertical(TableViewer viewer, String propName, int moveAmount, ShuffleDirection direction) {
		
		try {
			// TODO handle multi-selection
			@SuppressWarnings("unchecked")
			T item = (T) viewer.getStructuredSelection().getFirstElement();
			if (item==null) return;
	
			@SuppressWarnings("unchecked")
			List<T> items = new ArrayList<T>((Collection<T>)RichBeanUtils.getBeanValue(conf, propName));
			final int index = items.indexOf(item);
			item = items.remove(index);
	
			int newIndex = index + moveAmount;
			if (newIndex<0) newIndex = 0;
			if (newIndex>items.size()) newIndex = items.size();
			items.add(newIndex, item);
			if (items.equals(RichBeanUtils.getBeanValue(conf, propName))) {
				// The list is the same
				return;
			}
	
			items = firePreShuffle(new ShuffleEvent<T>(this, direction, items));
			RichBeanUtils.setBeanValue(conf, propName, items);
			viewer.setSelection(new StructuredSelection(item));
			firePostShuffle(new ShuffleEvent<T>(this, direction, items));
			
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
	public ShuffleConfiguration<T> getShuffleConfiguration() {
		return conf;
	}
}
