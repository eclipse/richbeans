/*-
 * Copyright © 2016 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package org.eclipse.richbeans.generator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ListenableProxyFactory<T> {
	private final Class<T> clazz;

	public ListenableProxyFactory(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public T createProxyFor(T original) {
		PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(original);
		InvocationHandler handler = new ListenableProxyInvocationHandler<T>(original, propertyChangeSupport);
		return (T) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[]{clazz, PropertyChangeInterface.class},
				handler
			);
	}

	public interface PropertyChangeInterface{
		public void addPropertyChangeListener(PropertyChangeListener listener);
		public void removePropertyChangeListener(PropertyChangeListener listener);
	}
}
