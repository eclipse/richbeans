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
package org.eclipse.richbeans.binding;

import org.eclipse.richbeans.api.reflection.IBeanController;
import org.eclipse.richbeans.api.reflection.IBeanService;

/**
 * Implementation of IBeanService which can be retrieved either by
 * OSGi (preferable) or a static method. This service is not a singleton.
 * 
 * @author Matthew Gerring
 *
 */
public class BeanService implements IBeanService {
	
	static {
		System.out.println("Creating BeanService");
	}
	
	private static BeanService instance;
	
	/**
	 * To be used for testing classes that do not have 
	 * OSGi available and therefore get a static instance.
	 * 
	 * Normally: inject service using OSGi.
	 * 
	 * @return
	 */
	public static BeanService getInstance() {
		if (instance == null) instance = new BeanService();
		return instance;
	}

	@Override
	public IBeanController createController(Object ui, Object bean) {
		return new BeanController(ui, bean);
	}

}
