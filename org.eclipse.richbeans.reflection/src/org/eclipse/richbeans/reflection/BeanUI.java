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

package org.eclipse.richbeans.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.richbeans.api.beans.BeanProcessor;
import org.eclipse.richbeans.api.event.ValueListener;
import org.eclipse.richbeans.api.widget.IFieldCollection;
import org.eclipse.richbeans.api.widget.IFieldWidget;


/**
 * Class concerned with sending state between beans and UI. It does this through the IFieldWidget interface. Initially
 * the design was to have no IFieldWidget and for BeanUI to synchronize and object but this lead to a complex and
 * confused design of BeanUI but more importantly of RichBeanEditorPart implementations. Now a IFieldWidget must
 * existing for each field in the bean mapping to each widget.
 * 
 * @author Matthew Gerring
 */
class BeanUI {

//	private static Logger logger = LoggerFactory.getLogger(BeanUI.class);

	/**
	 * NOTE: The order of the arguments. The first object is the bean, the second object is the uiObject which we are
	 * going to look for getters and setters.
	 * 
	 * @param bean
	 * @param uiObject
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */

	public static void beanToUI(final Object bean, final Object uiObject) throws Exception {

		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object value,  IFieldWidget box) throws Exception {
				box.setFieldName(name);
				box.setValue(value);
			}
			@Override
			public boolean requireValue() {
				return true;
			}
		});
	}

	/**
	 * Call to fire all value listeners
	 * 
	 * @param bean
	 * @param uiObject
	 * @throws Exception
	 */
	public static void fireValueListeners(final Object bean, final Object uiObject) throws Exception {

		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) throws Exception {
				box.fireValueListeners();
			}
		});
	}

	/**
	 * Call to fire all value listeners
	 * 
	 * @param bean
	 * @param uiObject
	 * @throws Exception
	 */
	public static void fireBoundsUpdaters(final Object bean, final Object uiObject) throws Exception {

		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) throws Exception {
				box.fireBoundsUpdaters();
			}
		});
	}

	/**
	 * NOTE: The order of the arguments. The first object is the uiobject, the second object is the bean which we are
	 * going to set properties from the UI with.
	 * 
	 * @param bean
	 * @param uiObject
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void uiToBean(final Object uiObject, final Object bean) throws Exception {

		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) throws Exception {
				final Object ob = box.getValue();
				if (ob != null && !isNaN(ob) && !isInfinity(ob)) {
					setValue(bean, name, ob);
				}
			}
		});
	}

	/**
	 * Set the value of a single field specified by field name in the bean from the ui.
	 * 
	 * @param uiObject
	 * @param bean
	 * @param fieldName
	 * @throws Exception
	 */
	public static void uiToBean(final Object uiObject, final Object bean, final String fieldName) throws Exception {

		if (fieldName == null)
			throw new Exception("Null fieldName passed to uiToBean. Please set the field name.");

		final IFieldWidget box = BeanUI.getFieldWidget(fieldName, uiObject);
		if (box == null)
			return; // Not all properties have to be in the UI.

		if (!box.isActivated())
			return;
		final Object ob = box.getValue();
		if (ob != null && !isNaN(ob) && !isInfinity(ob)) {
			RichBeanUtils.setBeanValue(bean, fieldName, ob);
		} else {
			// Required to fix fields inside a list editor being edited to no value.
			if (ob != null) {
				final Method setter = bean.getClass().getMethod(RichBeanUtils.getSetterName(fieldName), ob.getClass());
				setter.invoke(bean, ob.getClass().cast(null));
			}
		}

	}

	private static boolean isInfinity(Object ob) {
		if (!(ob instanceof Double))
			return false;
		return Double.isInfinite(((Double) ob).doubleValue());
	}

	private static boolean isNaN(Object ob) {
		if (!(ob instanceof Double))
			return false;
		return Double.isNaN(((Double) ob).doubleValue());
	}

	public static void addValueListener(final Object bean, final Object uiObject, final ValueListener listener)
			throws Exception {

		addValueListener(bean, uiObject, listener, true);
	}
	/**
	 * Add a value listener for the UI objects, if that method exists.
	 * 
	 * @param bean
	 * @param uiObject
	 * @param listener
	 * @throws Exception
	 */
	public static void addValueListener(final Object bean, final Object uiObject, final ValueListener listener, final boolean recursive)
			throws Exception {

		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) throws Exception {
				if (!recursive && box instanceof IFieldCollection) return; // TODO Check that this is right.
				box.addValueListener(listener);
			}
		});
	}

	/**
	 * Removes a value listener for the UI objects, if that method exists.
	 * 
	 * @param bean
	 * @param uiObject
	 * @param listener
	 * @throws Exception
	 */
	public static void removeValueListener(final Object bean, final Object uiObject, final ValueListener listener)
			throws Exception {
		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object value,  IFieldWidget box) throws Exception {
				box.removeValueListener(listener);
			}
		});
	}

	/**
	 * Used to switch all ui controls on or off.
	 * 
	 * @param bean
	 * @param uiObject
	 * @param on
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void switchState(final Object bean, final Object uiObject, final boolean on) throws Exception {
		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) {
				if (on) {
					box.on();
				} else {
					box.off();
				}
			}
		});
	}

	/**
	 * Attempts to set any IFieldWidgets available from getter methods on.
	 * 
	 * @param uiObject
	 * @param on
	 */
	public static void switchState(Object uiObject, boolean on) throws Exception {
		final Method[] methods = uiObject.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method m = methods[i];
			if (m.getReturnType() != null && IFieldWidget.class.isAssignableFrom(m.getReturnType()) &&
				m.getName().startsWith("get") && m.getParameterTypes().length==0) {
				final Object ob = m.invoke(uiObject);
				if (ob instanceof IFieldWidget) {
					final IFieldWidget box = (IFieldWidget) ob;
					if (on) {
						box.on();
					} else {
						box.off();
					}
				}
			}
		}
	}

	/**
	 * @param bean
	 * @param uiObject
	 * @param isEnabled
	 * @throws Exception
	 */
	public static void setEnabled(final Object bean, final Object uiObject, final boolean isEnabled) throws Exception {
		BeanUI.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) {
				box.setEnabled(isEnabled);
			}
		});
	}

	public final static void notify(final Object bean, final Object uiObject, final BeanProcessor worker) throws Exception {

		final Map<String, String> properties = BeanUtils.describe(bean);
		final Iterator<String>   it         = properties.keySet().iterator();
		final Collection<String> names      = BeanUI.getEditingFields(bean, uiObject);
		
		while (it.hasNext()) {
			final String fieldName = it.next();
			if (names.contains(fieldName)) {					
				if (fieldName.equals("class")) continue;
				final IFieldWidget box = BeanUI.getFieldWidget(fieldName, uiObject);
				// NOTE non-IFieldWidget fields will be ignored.
				if (box != null) {
					final Object val = worker.requireValue() ? getValue(bean, fieldName) : null;
					worker.process(fieldName, val, box);
				}
			}
		}
	}

	// TODO remove duplication of getValue() and setValue() which are both here and in RichBeanUtils
	private static Object getValue(Object bean, String fieldName) throws Exception {
		final String getter = RichBeanUtils.getGetterName(fieldName);
		try {
		    return bean.getClass().getMethod(getter).invoke(bean);
		} catch (java.lang.NoSuchMethodException ne) {
			final String isser = RichBeanUtils.getIsserName(fieldName);
		    return bean.getClass().getMethod(isser).invoke(bean);
		}
	}
	
	private static void setValue(Object bean, String fieldName, Object ob) throws Exception {
		
		final String setter = RichBeanUtils.getSetterName(fieldName);
		
		Method method = null;
		try {
			method = bean.getClass().getMethod(setter, ob.getClass());
			
		} catch (java.lang.NoSuchMethodException ne) {
			
			final Class<?> clazz   = ob.getClass();
			try {

				if (Double.class.isAssignableFrom(clazz)) {
					method = bean.getClass().getMethod(setter, new Class[]{double.class});
				} else if (Float.class.isAssignableFrom(clazz)) {
					method = bean.getClass().getMethod(setter, new Class[]{float.class});
				} else if (Long.class.isAssignableFrom(clazz)) {
					method = bean.getClass().getMethod(setter, new Class[]{long.class});
				} else if (Integer.class.isAssignableFrom(clazz)) {
					method = bean.getClass().getMethod(setter, new Class[]{int.class});
				} else if (Boolean.class.isAssignableFrom(clazz)) {
					method = bean.getClass().getMethod(setter, new Class[]{boolean.class});
				}
			} catch (NoSuchMethodException nsm2) {
				method = bean.getClass().getMethod(setter, new Class[]{Number.class});
			}


			if (method==null) {
				final Method[] methods = bean.getClass().getMethods();
				for (Method m : methods) {
					if (m.getName().equals(setter) && m.getParameterTypes().length==1) {
						Class<?> type = m.getParameterTypes()[0];
						if (!type.isAssignableFrom(clazz)) {
							continue;
						}
						method = m;
						break;
					}
				}
			}

		}
		
		method.invoke(bean, ob);
	}

	/**
	 * Get the ui field out of the object container.
	 * 
	 * @param fieldName
	 * @param uiObject
	 * @return IFieldWidget or null if is not an IFieldWidget instance
	 * @throws Exception
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static IFieldWidget getFieldWidget(final String fieldName, final Object uiObject) throws Exception {
		final String methodName = RichBeanUtils.getGetterName(fieldName);
		final Method getter = uiObject.getClass().getMethod(methodName);
		final Object box = getter.invoke(uiObject);
		if (box instanceof IFieldWidget) {
			return (IFieldWidget) box;
		}
		return null;
	}

	/**
	 * Retrieves a list of fields which are both in the bean and being edited by the user.
	 * 
	 * @param editorBean
	 * @param editorUI
	 * @return list of fields
	 * @throws Exception
	 */
	public static List<String> getEditingFields(Object editorBean, Object editorUI) throws Exception {

		final Collection<String> fields = BeanUtils.describe(editorBean).keySet();
		final List<String> expressionFields = new ArrayList<String>(fields);
		expressionFields.remove("class");

		for (Iterator<String> it = expressionFields.iterator(); it.hasNext();) {
			String field = it.next();
			try {
				final IFieldWidget wid = BeanUI.getFieldWidget(field, editorUI);
				if (wid == null)
					it.remove();
			} catch (Exception ne) {
				it.remove();
			}
		}

		return expressionFields;
	}

}
