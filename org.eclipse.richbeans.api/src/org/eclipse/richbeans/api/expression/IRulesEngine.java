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
package org.eclipse.richbeans.api.expression;

import java.util.Collection;

/**
 * Used to evaluate user interface rules as expressions.
 * @author Matthew Gerring
 *
 */
public interface IRulesEngine {

	/**
	 * 
	 * @param expression
	 * @throws Exception
	 */
	void createExpression(String expression) throws Exception;

	/**
	 * 
	 * @return
	 */
	Collection<String> getLazyVariableNamesFromExpression();

	/**
	 * 
	 * @param name
	 * @param value
	 */
	<T> void addLoadedVariable(String name, T value);

	/**
	 * 
	 * @return
	 */
	<T> T evaluate();


}
