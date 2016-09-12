/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.richbeans.widgets.cell;

import java.text.MessageFormat;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CComboCellEditor extends AppliableCellEditor {

	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;

	/**
	 * The zero-based index of the selected item.
	 */
	int selection;

	/**
	 * The custom combo box control.
	 */
	CCombo comboBox;

	/**
	 * Default ComboBoxCellEditor style
	 */
	protected static final int defaultStyle = SWT.NONE;

	/**
	 * Creates a new cell editor with no control and no st of choices.
	 * Initially, the cell editor has no cell validator.
	 *
	 * @since 2.1
	 * @see CellEditor#setStyle
	 * @see CellEditor#create
	 * @see ComboBoxCellEditor#setItems
	 * @see CellEditor#dispose
	 */
	public CComboCellEditor() {
		setStyle(defaultStyle);
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of
	 * choices and parented under the given control. The cell editor value is
	 * the zero-based index of the selected item. Initially, the cell editor has
	 * no cell validator and the first item in the list is selected.
	 *
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 */
	public CComboCellEditor(Composite parent, String[] items) {
		this(parent, items, defaultStyle);
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of
	 * choices and parented under the given control. The cell editor value is
	 * the zero-based index of the selected item. Initially, the cell editor has
	 * no cell validator and the first item in the list is selected.
	 *
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the list of strings for the combo box
	 * @param style
	 *            the style bits
	 * @since 2.1
	 */
	public CComboCellEditor(Composite parent, String[] items, int style) {
		super(parent, style);
		setItems(items);
	}

	/**
	 * Returns the list of choices for the combo box
	 *
	 * @return the list of choices for the combo box
	 */
	public String[] getItems() {
		return this.items;
	}

	/**
	 * Sets the list of choices for the combo box
	 *
	 * @param items
	 *            the list of choices for the combo box
	 */
	public void setItems(String[] items) {
		this.items = items;
		populateComboBoxItems();
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	@Override
	protected Control createControl(Composite parent) {

		comboBox = new CCombo(parent, getStyle());
		comboBox.setFont(parent.getFont());
		comboBox.setEditable(false);

		populateComboBoxItems();

		comboBox.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			@Override
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});

		comboBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				selection = comboBox.getSelectionIndex();
			}
		});

		comboBox.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		comboBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				CComboCellEditor.this.focusLost();
			}
		});
		return comboBox;
	}
	
	public void activate() {
		//comboBox.setListVisible(true);
	}

	public void setFocus() {
		super.setFocus();
		comboBox.setListVisible(true);
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method returns the zero-based index
	 * of the current selection.
	 *
	 * @return the zero-based index of the current selection wrapped as an
	 *         <code>Integer</code>
	 */
	@Override
	protected Object doGetValue() {
		return new Integer(selection);
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	@Override
	protected void doSetFocus() {
		comboBox.setFocus();
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method sets the minimum width of the
	 * cell. The minimum width is 10 characters if <code>comboBox</code> is
	 * not <code>null</code> or <code>disposed</code> else it is 60 pixels
	 * to make sure the arrow button and some text is visible. The list of
	 * CCombo will be wide enough to show its longest item.
	 */
	@Override
	public LayoutData getLayoutData() {
		LayoutData layoutData = super.getLayoutData();
		if ((comboBox == null) || comboBox.isDisposed()) {
			layoutData.minimumWidth = 60;
		} else {
			// make the comboBox 10 characters wide
			GC gc = new GC(comboBox);
			layoutData.minimumWidth = (gc.getFontMetrics()
					.getAverageCharWidth() * 10) + 10;
			gc.dispose();
		}
		return layoutData;
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method accepts a zero-based index of
	 * a selection.
	 *
	 * @param value
	 *            the zero-based index of the selection wrapped as an
	 *            <code>Integer</code>
	 */
	@Override
	protected void doSetValue(Object value) {
		if (!(value instanceof Integer)) return;
		selection = ((Integer) value).intValue();
		comboBox.select(selection);
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	protected void populateComboBoxItems() {
		if (comboBox != null && items != null) {
			comboBox.removeAll();
			for (int i = 0; i < items.length; i++) {
				comboBox.add(items[i], i);
			}

			setValueValid(true);
			selection = 0;
		}
	}

	@Override
	public void applyEditorValueAndDeactivate() {
		// must set the selection before getting value
		selection = comboBox.getSelectionIndex();
		applyEditorValueAndDeactivate(selection);
	}
	/**
	 * Applies the currently selected value and deactivates the cell editor
	 */
	public void applyEditorValueAndDeactivate(int selection) {
		
		this.selection = selection;
		Object newValue = doGetValue();
		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);

		if (!isValid) {
			// Only format if the 'index' is valid
			if (items.length > 0 && selection >= 0 && selection < items.length) {
				// try to insert the current value into the error message.
				setErrorMessage(MessageFormat.format(getErrorMessage(),
						new Object[] { items[selection] }));
			} else {
				// Since we don't have a valid index, assume we're using an
				// 'edit'
				// combo so format using its text value
				setErrorMessage(MessageFormat.format(getErrorMessage(),
						new Object[] { comboBox.getText() }));
			}
		}

		fireApplyEditorValue();
		deactivate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	@Override
	protected void focusLost() {
		if (isActivated()) {
			applyEditorValueAndDeactivate();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\t') { // tab key
			applyEditorValueAndDeactivate();
		}
	}

	public CCombo getCombo() {
		return comboBox;
	}
	protected int getDoubleClickTimeout() {
		return 0;
	}
}
