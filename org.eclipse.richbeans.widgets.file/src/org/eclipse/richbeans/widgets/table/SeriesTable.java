/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.richbeans.widgets.table;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IInputSelectionProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.richbeans.widgets.Activator;
import org.eclipse.richbeans.widgets.internal.GridUtils;
import org.eclipse.richbeans.widgets.table.event.SeriesEventDelegate;
import org.eclipse.richbeans.widgets.table.event.SeriesItemListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * This class is a table of a series of operations.
 * 
 * The widget provides and add button which when pressed can add another
 * item into the table. You may extend this table to provide for instance
 * a table which edits series of mathematical functions.
 * 
 * @author Matthew Gerring
 *
 */
public class SeriesTable {

	// UI
	private TableViewer          tableViewer;
	private Label errorLabel;
	private Composite error;
	
	// Editing
	private SeriesEditingSupport editingSupport;
	
	// Validation
    private ISeriesValidator     validator;
    
    // Events
    private SeriesEventDelegate delegate;
    
    public SeriesTable() {
    	delegate = new SeriesEventDelegate();
    }
    
	/**
	 * Create the control for the table. The icon provider is checked for label and
	 * icon for the first, name column in the table. It must provide at least an
	 * icon for a given SeriesItem implementation. If it does not provide a label 
	 * then the 'getName()' method of SeriesItem is used.
	 * 
	 * Uses default switches of SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE
	 * 
	 * @param parent       - the SWT composite to add the table to.
	 * @param iconProvider - a provider which must at least give the icon for a given SeriesItem
	 */
	public void createControl(Composite parent, SeriesItemLabelProvider iconProvider) {
		
		createControl(parent, iconProvider, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
	}
			
	/**
	 * Create the control for the table. The icon provider is checked for label and
	 * icon for the first, name column in the table. It must provide at least an
	 * icon for a given SeriesItem implementation. If it does not provide a label 
	 * then the 'getName()' method of SeriesItem is used.
	 * 
	 * @param parent       - the SWT composite to add the table to.
	 * @param iconProvider - a provider which must at least give the icon for a given SeriesItem
	 * @param switches by defualt these are SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE
	 */   
	public void createControl(Composite parent, SeriesItemLabelProvider iconProvider, int switches) {

		tableViewer = new TableViewer(parent, switches) {
		    protected void inputChanged(Object input, Object oldInput) {
		    	super.inputChanged(input, oldInput);
		    	checkValid((List<ISeriesItemDescriptor>)input);
		    }
		};

		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer, new FocusCellOwnerDrawHighlighter(tableViewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				// TODO see AbstractComboBoxCellEditor for how list is made visible
				return super.isEditorActivationEvent(event)
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && (event.keyCode == KeyLookupFactory
								.getDefault().formalKeyLookup(
										IKeyLookup.ENTER_NAME)));
			}
		};

		TableViewerEditor.create(tableViewer, focusCellManager, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);


		tableViewer.setContentProvider(new SeriesContentProvider());
		
		this.error = new Composite(parent, SWT.NONE);
		error.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		error.setLayout(new GridLayout(2, false));
		error.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		Label errorIcon = new Label(error, SWT.NONE);
		errorIcon.setImage(Activator.getImageDesciptor("icons/error.png").createImage());
		errorIcon.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		errorIcon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		this.errorLabel = new Label(error, SWT.WRAP);
		errorLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		errorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		GridUtils.setVisible(error,         false);

		createColumns(iconProvider);
		
		//hook up delete key to remove from list
		tableViewer.getTable().addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					delete();
				}
			}
		});
		
	}
	
	
	protected void checkValid(List<ISeriesItemDescriptor> series) {
		
		if (series.contains(ISeriesItemDescriptor.INSERT)) {
			GridUtils.setVisible(error,         true);
			errorLabel.setText("Please choose an operation to insert");
			error.getParent().layout();
			return;
		}
		
		if (validator==null) return;
		final String errorMessage = validator.getErrorMessage(series);
		GridUtils.setVisible(error,         errorMessage!=null);
		errorLabel.setText(errorMessage!=null ? errorMessage : "");
		error.getParent().layout();
	}

	/**
	 * This method is called to create the columns of the table.
	 * The columns required may be overridden depending on the 
	 * objects defined by setInput(...) to provide additional 
	 * columns which are optionally editable.
	 * 
	 */
	protected void createColumns(SeriesItemLabelProvider iconProv) {
		createNameColumn("Name", iconProv);
	}

	/**
	 * May be null or empty array. Must be called to set content provider on table.
	 * Should be called after createControl(...)
	 * 
	 * @param input
	 */
	public void setInput(Collection<? extends ISeriesItemDescriptor> currentItems, ISeriesItemFilter content) {
		if (tableViewer.getInput() == null) { 
			editingSupport.setSeriesItemDescriptorProvider(content);
		}
		if (currentItems==null) currentItems = Collections.emptyList();
		tableViewer.setInput(currentItems);
	}
	
	/**
	 * Call to remove all items from the list.
	 */
	public void clear() {
		tableViewer.setInput(Collections.emptyList());
	}

	/**
	 * 
	 * @param name
	 * @param prov
	 */
	protected void createNameColumn(final String name, final SeriesItemLabelProvider orig) {
		
		TableViewerColumn nameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		nameColumn.getColumn().setWidth(300);
		nameColumn.getColumn().setMoveable(true);
		nameColumn.getColumn().setText(name);
		
		SeriesLabelProvider prov = new SeriesLabelProvider(orig);
		nameColumn.setLabelProvider(prov);

		this.editingSupport = new SeriesEditingSupport(tableViewer,  delegate,  prov);
		nameColumn.setEditingSupport(editingSupport);

	}

	
	public void dispose() {
		delegate.clear();
		tableViewer.getControl().dispose();
	}

	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	public IInputSelectionProvider getSelectionProvider() {
		return tableViewer;
	}

	public void setLockEditing(boolean checked) {
		SeriesContentProvider prov = (SeriesContentProvider)tableViewer.getContentProvider();
		prov.setLockEditing(checked);
		tableViewer.refresh();
	}

	public List<ISeriesItemDescriptor> getSeriesItems() {
		if (tableViewer==null) return Collections.emptyList();
		SeriesContentProvider prov = (SeriesContentProvider)tableViewer.getContentProvider();
		return prov.getSeriesItems();
	}

	/**
	 * Searchs for a matching ISeriesItemDescriptor in the list of descriptors
	 * @param predicate
	 * @return
	 */
	public ISeriesItemDescriptor find(Predicate<ISeriesItemDescriptor> predicate) {
		if (tableViewer==null) return null;
		return getSeriesItems().stream().filter(predicate::test).findFirst().orElse(null);
	}

	/**
	 * Add a new operation to the list.
	 */
	public void addNew() {
		addNewElement(false);
	}
	
	/**
	 * Add a new operation to the list.
	 */
	public void addNewBeforeSelected() {
		addNewElement(true);
	}
	
	private void addNewElement(boolean before){
		tableViewer.cancelEditing();
		
		int i = before ? 0 : 1;
		
		final ISeriesItemDescriptor selected = getSelected();
		if (selected!=null && !selected.equals(ISeriesItemDescriptor.NEW)) {
			final List<ISeriesItemDescriptor> items = getSeriesItems();
			final int index = items.indexOf(selected);
			items.add(index+i, ISeriesItemDescriptor.INSERT);
			tableViewer.setInput(items);
			tableViewer.editElement(ISeriesItemDescriptor.INSERT, 0);
			return;
		}

		tableViewer.editElement(ISeriesItemDescriptor.NEW, 0);
	}
	
    /**
     * Remove the selected item, if any.
     * @return true if something is selected and we deleted it.
     */
	public boolean delete() {
		
		final ISeriesItemDescriptor selected = getSelected();
		if (selected==null) return false;
		if (selected==ISeriesItemDescriptor.NEW) return false;
		
		SeriesContentProvider prov = (SeriesContentProvider)tableViewer.getContentProvider();
        boolean ok = prov.delete(selected);
        if (ok) delegate.fireItemRemoved(selected);
        return ok;
	}


	public ISeriesItemDescriptor getSelected() {
		final IStructuredSelection  sel      = (IStructuredSelection)tableViewer.getSelection();
		final ISeriesItemDescriptor selected = (ISeriesItemDescriptor)sel.getFirstElement();
		return selected;
	}


	public TableViewerColumn createColumn(String name, int mod, int width, final SeriesItemLabelProvider prov) {
		
		final TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.LEFT);
		col.getColumn().setWidth(width);
		col.getColumn().setMoveable(true);
		col.getColumn().setText(name);
		
		col.setLabelProvider(new DelegatingStyledCellLabelProvider(prov));
		return col;
	}

	public void setMenuManager(MenuManager rightClick) {
		tableViewer.getControl().setMenu(rightClick.createContextMenu(tableViewer.getControl()));
	}


	public ISeriesValidator getValidator() {
		return validator;
	}


	public void setValidator(ISeriesValidator validator) {
		this.validator = validator;
	}
	
	public DropTarget getDropTarget() {
		return new DropTarget(tableViewer.getControl(), DND.DROP_MOVE | DND.DROP_DEFAULT | DND.DROP_COPY);
	}
	
	public void refreshTable() {
		if (!tableViewer.getTable().isDisposed()){
			tableViewer.refresh();
			checkValid(getSeriesItems());
		}
		
	}
	
	public void addSelectionListener(ISelectionChangedListener listener){
		tableViewer.addSelectionChangedListener(listener);
	}
	
	public void removeSelectionListener(ISelectionChangedListener listener){
		tableViewer.removeSelectionChangedListener(listener);
	}


	public void setHeaderVisible(boolean b) {
		tableViewer.getTable().setHeaderVisible(b);
	}

	public void setLayoutData(GridData gridData) {
		tableViewer.getTable().setLayoutData(gridData);
	}
	
	public void deselectAll() {
		tableViewer.getTable().deselectAll();
	}

	public Control getControl() {
		return tableViewer.getTable();
	}
	
	public void addSeriesEventListener(SeriesItemListener listener) {
		delegate.addSeriesEventListener(listener);
	}
	
	public void removeSeriesEventListener(SeriesItemListener listener) {
		delegate.removeSeriesEventListener(listener);
	}

	public void setSelection(ISeriesItemDescriptor des) {
		tableViewer.setSelection(new StructuredSelection(des));
	}

}
