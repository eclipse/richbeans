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

package org.eclipse.richbeans.generator.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.eclipse.richbeans.generator.ListenableProxyFactory.PropertyChangeInterface;
import org.eclipse.richbeans.generator.ListenableProxyInvocationHandler;
import org.junit.Test;

public class ListenableProxyInvocationHandlerTest {
	@Test
	public void testCallsUnderlyingMethod(){
		TestExample example = mock(TestExample.class);

		TestExample proxy = createProxy(example, new PropertyChangeSupport(example));

		proxy.setName("bob");
		verify(example).setName("bob");
	}

	@Test
	public void testCallsUnderlyingMethodAndReturnsValue(){
		TestExample example = mock(TestExample.class);
		when(example.getName()).thenReturn("bob");

		TestExample proxy = createProxy(example, new PropertyChangeSupport(example));

		assertThat(proxy.getName(), is("bob"));
	}

	@Test
	public void testFiresEventForSetters(){
		TestExample example = new TestExampleImpl("bob", true);
		PropertyChangeSupport propertyChangeSupport = mock(PropertyChangeSupport.class);

		TestExample proxy = createProxy(example, propertyChangeSupport);

		proxy.setName("joe");

		assertThat(example.getName(), is("joe"));
		verify(propertyChangeSupport).firePropertyChange("name", "bob", "joe");
	}

	@Test
	public void testFiresEventForBooleanSettersWhereGetterStartsWithIs(){
		TestExample example = new TestExampleImpl("bob", false);
		PropertyChangeSupport propertyChangeSupport = mock(PropertyChangeSupport.class);

		TestExample proxy = createProxy(example, propertyChangeSupport);

		proxy.setAwesome(true);

		assertThat(example.isAwesome(), is(true));
		verify(propertyChangeSupport).firePropertyChange("awesome", Boolean.FALSE, Boolean.TRUE);
	}

	@Test
	public void testAddsListenersToChangeSupport(){
		TestExample example = mock(TestExample.class);
		PropertyChangeSupport propertyChangeSupport = mock(PropertyChangeSupport.class);
		PropertyChangeListener listener = mock(PropertyChangeListener.class);

		TestExample proxy = createProxy(example, propertyChangeSupport);

		((PropertyChangeInterface)proxy).addPropertyChangeListener(listener);

		verify(propertyChangeSupport).addPropertyChangeListener(listener);
	}

	@Test
	public void testRemovesListenersFromChangeSupport(){
		TestExample example = mock(TestExample.class);
		PropertyChangeSupport propertyChangeSupport = mock(PropertyChangeSupport.class);
		PropertyChangeListener listener = mock(PropertyChangeListener.class);

		TestExample proxy = createProxy(example, propertyChangeSupport);

		((PropertyChangeInterface)proxy).removePropertyChangeListener(listener);

		verify(propertyChangeSupport).removePropertyChangeListener(listener);
	}

	@Test
	public void testDeligatesToOtherMethods(){
		TestExample example = mock(TestExample.class);

		createProxy(example, mock(PropertyChangeSupport.class)).doSomething();

		verify(example).doSomething();
	}

	@SuppressWarnings("unchecked")
	private <T> T createProxy(T example, PropertyChangeSupport propertyChangeSupport) {
		InvocationHandler handler = new ListenableProxyInvocationHandler<T>(example, propertyChangeSupport);
		return (T) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[]{TestExample.class, PropertyChangeInterface.class},
				handler
			);
	}

	public interface TestExample{
		public String getName();
		public void setName(String name);
		public void doSomething();
		public boolean isAwesome();
		public void setAwesome(boolean awesome);
	}

	public class TestExampleImpl implements TestExample{
		private String name;
		private boolean awesome;

		public TestExampleImpl(String name, boolean awesome) {
			this.name = name;
			this.awesome = awesome;
		}
		@Override
		public String getName() {
			return name;
		}
		@Override
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public void doSomething() {
			// ironically do nothing
		}
		@Override
		public boolean isAwesome() {
			return awesome;
		}
		@Override
		public void setAwesome(boolean awesome) {
			this.awesome = awesome;
		}
	}
}
