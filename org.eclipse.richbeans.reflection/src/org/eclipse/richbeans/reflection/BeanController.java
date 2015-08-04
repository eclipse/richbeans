/*
 * Copyright (c) 2015 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.richbeans.reflection;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.richbeans.api.beans.BeanProcessor;
import org.eclipse.richbeans.api.event.ValueListener;
import org.eclipse.richbeans.api.reflection.IBeanController;
import org.eclipse.richbeans.api.widget.IFieldWidget;

/**
 * This class acts as a non-static wrapper around BeanUI, removing the necessity
 * to pass the bean and UI objects to all method calls. Once this class has been
 * instantiated with UI and bean objects, all underlying calls to BeanUI should
 * have the arguments in the correct order, making it much simpler to use the
 * BeanUI methods correctly.
 * 
 * @author Colin Palmer
 */
class BeanController implements IBeanController {

	private final Object ui;
	private final Object bean;

	/**
	 * Create a new BeanController with the given UI and bean objects. Note the
	 * correct order of the arguments!
	 */
	public BeanController(Object ui, Object bean) {
		this.ui = ui;
		this.bean = bean;
	}

	public Object getUi() {
		return ui;
	}

	public Object getBean() {
		return bean;
	}

	/**
	 * Send the bean values to the UI
	 * <p>
	 * Code calling this method should normally call switchUIOff() first and
	 * switchUIOn() afterwards to avoid an infinite loop of value changed events
	 */
	public void beanToUI() throws Exception {
		BeanUI.beanToUI(bean, ui);
	}

	/**
	 * Send the UI values to the bean
	 */
	public void uiToBean() throws Exception {
		BeanUI.uiToBean(ui, bean);
	}

	/**
	 * Send the value of a single named field from the UI to the bean
	 */
	public void uiToBean(String fieldName) throws Exception {
		BeanUI.uiToBean(ui, bean, fieldName);
	}

	/**
	 * Recursively switch all UI elements on
	 */
	public void switchState(boolean on) throws Exception {
		BeanUI.switchState(ui, on);
	}
	
	/**
	 * Recursively set the enabled state of all UI elements
	 */
	public void setUIEnabled(boolean isEnabled) throws Exception {
		BeanUI.setEnabled(bean, ui, isEnabled);
	}

	/**
	 * Fire value listeners on all UI elements
	 */
	public void fireValueListeners() throws Exception {
		BeanUI.fireValueListeners(bean, ui);
	}

	/**
	 * Fire bounds updaters on all UI elements
	 */
	public void fireBoundsUpdaters() throws Exception {
		BeanUI.fireBoundsUpdaters(bean, ui);
	}

	/**
	 * Add a value listener to all UI elements
	 */
	public void addValueListener(ValueListener listener) throws Exception {
		BeanUI.addValueListener(bean, ui, listener);
	}

	/**
	 * Remove a value listener from all UI elements
	 */
	public void removeValueListener(ValueListener listener) throws Exception {
		BeanUI.removeValueListener(bean, ui, listener);
	}

	/**
	 * Get the IFieldWidget associated with a single field name
	 */
	public IFieldWidget getFieldWidget(String fieldName) throws Exception {
		return BeanUI.getFieldWidget(fieldName, ui);
	}

	/**
	 * Get a list of all fields which are in the bean and have an associated UI
	 * element
	 */
	public List<String> getEditingFields() throws Exception {
		return BeanUI.getEditingFields(bean, ui);
	}	

	/**
	 * Holds the cached existing widgets for editing fields in beans.
	 */
	private Map<String, IFieldWidget> cachedWidgets;


	/**
	 * Attempts to retrieve the widget for editing the given field. The widget may not have been created yet, in which
	 * case returns null.
	 * 
	 * @param beanClasses
	 * @param fieldName
	 * @return IFieldWidget
	 */
	@Override
	public IFieldWidget getBeanField(String fieldName, final Class<? extends Object>... beanClasses) {

		fieldName = fieldName.substring(0, 1).toLowerCase(Locale.US) + fieldName.substring(1);

		for (int i = 0; i < beanClasses.length; i++) {
			final String id = beanClasses[i].getName() + ":" + fieldName;
			final IFieldWidget box = (cachedWidgets != null) ? cachedWidgets.get(id) : null;
			if (box != null) return box;
		}
		return null;
	}

	

	/**
	 * You can record widgets associated with editing a particular bean field here. They are then available to be
	 * listened to by other parts of the user interface. However all the field editors are recorded in a map so care
	 * should be taken (memeory leak etc). Editors of lists like BeanListEditor have the same field to edit multiple
	 * objects. So although you could add a listener to the field you cannot be sure which actual XML entry it is
	 * currently dealing with.
	 * 
	 * @param bean
	 * @param uiObject
	 * @throws Exception
	 */
	@Override
	public void recordBeanFields() throws Exception {

		BeanUI.notify(bean, ui, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) throws Exception {
				addBeanField(name, box);
			}
		});
		if (cachedWidgets!=null)    cachedWidgets.clear();
		if (waitingListeners!=null) waitingListeners.clear();
	}


	/**
	 * You can add a field associated with a bean (even if it is viewing a property and not actually editing one). All
	 * editing fields are added through reflection with setBeanFields(...) however some are not fields and still can be
	 * listened to.
	 * 
	 * @param beanClazz
	 * @param fieldName
	 * @param box
	 */
	@Override
	public void addBeanField(String fieldName, final IFieldWidget box) {
		fieldName = fieldName.substring(0, 1).toLowerCase(Locale.US) + fieldName.substring(1);
		if (cachedWidgets == null)
			cachedWidgets = new ConcurrentHashMap<String, IFieldWidget>(89);
		final String id = bean.getClass().getName() + ":" + fieldName;
		cachedWidgets.put(id, box);
		if (waitingListeners != null) {
			final Collection<ValueListener> listeners = waitingListeners.get(id);
			if (listeners != null) {
				for (ValueListener valueListener : listeners)
					box.addValueListener(valueListener);
				waitingListeners.remove(id);
			}
		}
	}

	
	/**
	 * Because of Lazy initiation some fields that will exist may not exists at the point at which we wish to listen to
	 * them. Therefore a queue of listeners is kept. These are added to the widget and removed from the queue when and
	 * if the widget is created.
	 */
	private Map<String, Collection<ValueListener>> waitingListeners;

	/**
	 * Adds a value listener for the given class and field. Throws an exception if the class and field is not recorded
	 * as having a UI editor at the moment.
	 * 
	 * @param beanClass
	 * @param fieldName
	 * 
	 * @param listener
	 */
	@Override
	public void addBeanFieldValueListener(String fieldName, final ValueListener listener) {
		
		fieldName = fieldName.substring(0, 1).toLowerCase(Locale.US) + fieldName.substring(1);
		final String id = bean.getClass().getName() + ":" + fieldName;
		final IFieldWidget box = (cachedWidgets != null) ? cachedWidgets.get(id) : null;
		if (box == null) {
			if (waitingListeners == null)
				waitingListeners = new ConcurrentHashMap<String, Collection<ValueListener>>(31);
			Collection<ValueListener> listeners = waitingListeners.get(id);
			if (listeners == null) {
				listeners = new HashSet<ValueListener>(3);
				waitingListeners.put(id, listeners);
			}
			listeners.add(listener);
			return;
		}
		box.addValueListener(listener);
	}

	
    @Override
	public void dispose() throws Exception {
		BeanUI.notify(bean, ui, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) {
				box.dispose();
			}
		});
	}

}
