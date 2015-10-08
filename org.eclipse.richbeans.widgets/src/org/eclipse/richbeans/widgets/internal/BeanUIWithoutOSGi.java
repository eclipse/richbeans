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

package org.eclipse.richbeans.widgets.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.richbeans.api.beans.BeanProcessor;
import org.eclipse.richbeans.api.beans.BeansFactory;
import org.eclipse.richbeans.api.event.ValueListener;
import org.eclipse.richbeans.api.widget.IFieldCollection;
import org.eclipse.richbeans.api.widget.IFieldWidget;


/**
 * Class concerned with sending state between beans and UI. It does this through the IFieldWidget interface. Initially
 * the design was to have no IFieldWidget and for BeanUI to synchronize and object but this lead to a complex and
 * confused design of BeanUI but more importantly of RichBeanEditorPart implementations. Now a IFieldWidget must
 * existing for each field in the bean mapping to each widget.
 * 
 * This class is a workaround for using the widgets in non-OSGi mode. It is being phased out.
 * 
 * @author Matthew Gerring
 * 
 * @deprecated This class will shortly be removed when the widgets are made more widgety and less rich beany.
 */
public class BeanUIWithoutOSGi {

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

		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
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

		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
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

		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) throws Exception {
				box.fireBoundsUpdaters();
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

		final IFieldWidget box = BeanUIWithoutOSGi.getFieldWidget(fieldName, uiObject);
		if (box == null)
			return; // Not all properties have to be in the UI.

		if (!box.isActivated())
			return;
		final Object ob = box.getValue();
		if (ob != null && !isNaN(ob) && !isInfinity(ob)) {
			BeansFactory.setBeanValue(bean, fieldName, ob);
		} else {
			// Required to fix fields inside a list editor being edited to no value.
			if (ob != null) {
				final Method setter = bean.getClass().getMethod(BeansFactory.getSetterName(fieldName), ob.getClass());
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

	public static void addValueListener(final Object bean, final Object uiObject, final ValueListener listener) throws Exception {

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

		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
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
		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
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
		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
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
		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) {
				box.setEnabled(isEnabled);
			}
		});
	}

	public static void dispose(final Object bean, final Object uiObject) throws Exception {
		BeanUIWithoutOSGi.notify(bean, uiObject, new BeanProcessor() {
			@Override
			public void process(String name, Object unused,  IFieldWidget box) {
				box.dispose();
			}
		});
	}

	public final static void notify(final Object bean, final Object uiObject, final BeanProcessor worker) throws Exception {

		final Map<String, String> properties = BeanUtils.describe(bean);
		final Iterator<String>   it         = properties.keySet().iterator();
		final Collection<String> names      = BeanUIWithoutOSGi.getEditingFields(bean, uiObject);
		
		while (it.hasNext()) {
			final String fieldName = it.next();
			if (names.contains(fieldName)) {					
				if (fieldName.equals("class")) continue;
				final IFieldWidget box = BeanUIWithoutOSGi.getFieldWidget(fieldName, uiObject);
				// NOTE non-IFieldWidget fields will be ignored.
				if (box != null) {
					final Object val = worker.requireValue() ? getValue(bean, fieldName) : null;
					worker.process(fieldName, val, box);
				}
			}
		}
	}

	private static Object getValue(Object bean, String fieldName) throws Exception {
		final String getter = BeansFactory.getGetterName(fieldName);
		try {
		    return bean.getClass().getMethod(getter).invoke(bean);
		} catch (java.lang.NoSuchMethodException ne) {
			final String isser = BeansFactory.getIsserName(fieldName);
		    return bean.getClass().getMethod(isser).invoke(bean);
		}
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
		final String methodName = BeansFactory.getGetterName(fieldName);
		final Method getter = uiObject.getClass().getMethod(methodName);
		final Object box = getter.invoke(uiObject);
		if (box instanceof IFieldWidget) {
			return (IFieldWidget) box;
		}
		return null;
	}

	/**
	 * Method name spelling was corrected to getFieldWidget(). This method with
	 * the old name exists only to avoid breaking existing code and should be
	 * removed once all references to the mis-spelled name have been corrected
	 */
	@Deprecated
	public static IFieldWidget getFieldWiget(final String fieldName, final Object uiObject) throws Exception {
		return getFieldWidget(fieldName, uiObject);
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
				final IFieldWidget wid = BeanUIWithoutOSGi.getFieldWidget(field, editorUI);
				if (wid == null)
					it.remove();
			} catch (Exception ne) {
				it.remove();
			}
		}

		return expressionFields;
	}
}
