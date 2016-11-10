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

package org.eclipse.richbeans.api.widget;

/**
 * An interface used to mark a widget as providing expression evaluation.
 * 
 * A sub-set of all IFieldWidgets will also be IExpressionWidget
 */
public interface IExpressionWidget extends IFieldWidget {

	/**
	 * Set the manager for expressions.
	 * @param man
	 */
	public void setExpressionManager(IExpressionManager man);
	
	/**
	 * Sets the displayed value for the expression. Used as short
	 * cut for updating the value, saves expensive recalculation
	 * cycle in RichBeanEditor. Usually called from IExpressionManager
	 * when precedents change.
	 * 
	 * @param value
	 */
	public void setExpressionValue(double value);
	
	/**
	 * Called to return the main control used by the widget.
	 * 
	 * This control will have content proposals added to it if the control
	 * is a type that the IExpressionManager recognises as possible to have
	 * content proposals.
	 */
	public <T> T getControl();
	
	/**
	 * This method returns false if the string entered is definitely a number.
	 * For instance a double value or a double value and a unit.
	 * 
	 * Otherwise it returns true.
	 * 
	 * @param value
	 * @return false if number
	 */
	public boolean isExpressionParseRequired(final String value);
	
	/**
	 * Returns false if the box does not currently allow expressions.
	 */
	public boolean isExpressionAllowed();

}
