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
package org.eclipse.richbeans.api.beans;

import org.eclipse.richbeans.api.widget.IFieldWidget;

/**
 * 
 * A bean processor is used to execute vistor patterns on beans.
 * 
 * @author Matthew Gerring
 *
 */
public abstract class BeanProcessor {
	
	/**
	 * The visit method
	 * @param name
	 * @param value
	 * @param box
	 * @throws Exception
	 */
	public abstract void process(String name, Object value, IFieldWidget box) throws Exception;
	
	/**
	 * True if value required (slower). false by default.
	 * @return
	 */
	public boolean requireValue() {
		return false;
	}
}
