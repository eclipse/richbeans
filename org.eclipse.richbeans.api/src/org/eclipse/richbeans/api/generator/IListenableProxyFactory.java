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
package org.eclipse.richbeans.api.generator;

import java.beans.PropertyChangeListener;

/**
 * Factory to create proxies for simple beans that implement 
 * Property change support so that other components can listen
 * for changes. 
 * 
 * @author Kevin Savage
 *
 * @param <T> The type to proxy
 */
public interface IListenableProxyFactory {
	public <S extends T, T> T createProxyFor(S original, Class<T> interfaceImplemented);
	
	/**
	 * used so the proxy knows about the methods that you need to implement for property change support
	 */
	public interface PropertyChangeInterface{
		public void addPropertyChangeListener(PropertyChangeListener listener);
		public void removePropertyChangeListener(PropertyChangeListener listener);
	}
}
