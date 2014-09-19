/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.plotting.api.region;

import org.eclipse.jface.action.IAction;

/**
 * An action for creating a region
 * @author fcp94556
 *
 */
public interface IRegionAction extends IAction {
	
	/**
	 * The user object which will be set on the region when it
	 * is created.
	 * @param userObject
	 */
    public Object getUserObject();

	/**
	 * The user object which will be set on the region when it
	 * is created.
	 * @param userObject
	 */
    public void setUserObject(Object userObject);
}
