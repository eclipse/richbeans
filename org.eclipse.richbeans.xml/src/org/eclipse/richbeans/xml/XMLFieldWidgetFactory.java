/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.richbeans.xml;

import java.util.List;

import org.eclipse.richbeans.widgets.FieldComposite;
import org.eclipse.richbeans.widgets.file.FileBox;
import org.eclipse.richbeans.widgets.scalebox.ScaleBox;
import org.eclipse.richbeans.widgets.selector.GridListEditor;
import org.eclipse.richbeans.widgets.selector.VerticalListEditor;
import org.eclipse.richbeans.widgets.wrappers.BooleanWrapper;
import org.eclipse.richbeans.widgets.wrappers.ComboAndNumberWrapper;
import org.eclipse.richbeans.widgets.wrappers.ComboWrapper;
import org.eclipse.richbeans.widgets.wrappers.LabelWrapper;
import org.eclipse.richbeans.widgets.wrappers.PrintfWrapper;
import org.eclipse.richbeans.widgets.wrappers.RadioWrapper;
import org.eclipse.richbeans.widgets.wrappers.SpinnerWrapper;
import org.eclipse.richbeans.widgets.wrappers.TextWrapper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Convenience class for accessing the current recommended implementation
 * of IFieldWiget for a particular field.
 */
public class XMLFieldWidgetFactory {

	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createScaleBox(final Composite parent, final int style) {
		return new ScaleBox(parent, style);
	}
	
	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createFileBox(final Composite parent, final int style) {
		return new FileBox(parent, style);
	}
	
	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createBoolean(final Composite parent, final int style) {
		return new BooleanWrapper(parent, style);
	}

	/**
	 * @param parent
	 * @param style
	 * @param visibleChoices 
	 * @return IFieldWidget
	 */
	public static FieldComposite createComboAndNumber(final Composite parent, final int style, List<String> visibleChoices) {
		return new ComboAndNumberWrapper(parent, style, visibleChoices);
	}
	
	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createCombo(final Composite parent, final int style) {
		return new ComboWrapper(parent, style);
	}

	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createLabel(final Composite parent, final int style) {
		return new LabelWrapper(parent, style);
	}

	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createPrintf(final Composite parent, final int style) {
		return new PrintfWrapper(parent, style);
	}
	

	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createSpinner(final Composite parent, final int style) {
		return new SpinnerWrapper(parent, style);
	}


	/**
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createText(final Composite parent, final int style) {
		return new TextWrapper(parent, style);
	}

	/**
	 * @param parent
	 * @param style
	 * @param items 
	 * @return IFieldWidget
	 */
	public static Group createRadio(final Composite parent, final int style, final String[] items) {
		return new RadioWrapper(parent, style, items);
	}

	/**
	 * NOTE: The editor returned is not ready and requires further setup such as 
	 * providing the bean and the component to edit the bean.
	 * 
	 * @param parent
	 * @param style
	 * @return IFieldWidget
	 */
	public static FieldComposite createBeanList(final Composite parent, final int style) {
		return new VerticalListEditor(parent, style);
	}
	
	/**
	 * NOTE: The editor returned is not ready and requires further setup such as 
	 * providing the bean and the component to edit the bean.
	 * 
	 * @param parent
	 * @param style
	 * @param cols 
	 * @param rows 
	 * @return IFieldWidget
	 */
	public static FieldComposite createGirdList(final Composite parent, final int style, final int cols, final int rows) {
		return new GridListEditor(parent, style, cols, rows);
	}

}
