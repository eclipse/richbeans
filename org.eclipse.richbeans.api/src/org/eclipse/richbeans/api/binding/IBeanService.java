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
package org.eclipse.richbeans.api.binding;


public interface IBeanService {

	
	/**
	 * Create a controller for synching a bean tree with a user interface object.
	 * @param ui
	 * @param bean
	 * @return
	 */
	public IBeanController createController(Object ui, Object bean);


}
