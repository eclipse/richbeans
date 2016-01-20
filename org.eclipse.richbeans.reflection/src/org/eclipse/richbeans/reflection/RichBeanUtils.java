package org.eclipse.richbeans.reflection;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Static utility methods for working with beans.
 *
 * @deprecated New code should use the more standard tools available in {@link org.apache.commons.beanutils.BeanUtils}
 *             or the <code>java.beans</code> package (such as {@link java.beans.Introspector})
 */
@Deprecated
public class RichBeanUtils {

	/**
	 * There must be a smarter way of doing this i.e. a JDK method I cannot find. However it is one line of Java so
	 * after spending some time looking have coded self.
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String getSetterName(final String fieldName) {
		if (fieldName == null) return null;
		return getName("set", fieldName);
	}

	/**
	 * There must be a smarter way of doing this i.e. a JDK method I cannot find. However it is one line of Java so
	 * after spending some time looking have coded self.
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String getGetterName(final String fieldName) {
		if (fieldName == null)
			return null;
		return getName("get", fieldName);
	}

	public static String getIsserName(final String fieldName) {
		if (fieldName == null)
			return null;
		return getName("is", fieldName);
	}

	private static String getName(final String prefix, final String fieldName) {
		return prefix + getFieldWithUpperCaseFirstLetter(fieldName);
	}

	public static String getFieldWithUpperCaseFirstLetter(final String fieldName) {
		return fieldName.substring(0, 1).toUpperCase(Locale.US) + fieldName.substring(1);
	}

	/**
	 * Method gets value out of bean using reflection.
	 * 
	 * @param bean
	 * @param fieldName
	 * @return value
	 * @throws Exception
	 */
	// TODO could probably be replaced by org.apache.commons.beanutils.BeanUtils.getProperty()
	public static Object getBeanValue(final Object bean, final String fieldName) throws Exception {
		final String getterName = getGetterName(fieldName);
		final Method method = bean.getClass().getMethod(getterName);
		return method.invoke(bean);
	}

	/**
	 * Changes a value on the given bean using reflection
	 * 
	 * @param bean
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	// TODO could probably be replaced by org.apache.commons.beanutils.BeanUtils.setProperty()
	public static void setBeanValue(final Object bean, final String fieldName, final Object value) throws Exception {
		final String setterName = getSetterName(fieldName);
		
		// Faster to deal with primitives within an exception
		boolean tryDirectValue = true;
		try {
			if (value != null) {
				if (value instanceof Number) {
		
					Number number = (Number)value;
					final Method method = bean.getClass().getMethod(setterName, getPrimitiveClass(number));
					method.invoke(bean, value); // auto-boxing should sort this
					return;
					
				} if (value instanceof Boolean) {
					final Method method = bean.getClass().getMethod(setterName, boolean.class);
					method.invoke(bean, ((Boolean)value).booleanValue());
					return;
				}
			}
			
			tryDirectValue = false;
			Method method=null;
			if (value != null) {
				try {
				    method = bean.getClass().getMethod(setterName, value.getClass());
				} catch (NoSuchMethodException ne) {
					
					// Look for interfaces.
					final Class<?>[] interfaces = value.getClass().getInterfaces();
					if (interfaces!=null) for (Class<?> class1 : interfaces) {
						try {
							method = bean.getClass().getMethod(setterName, class1);
						} catch (NoSuchMethodException neOther) {
							continue;
						}
						if (method!=null) break;
					}
					if (method==null) {
						method = bean.getClass().getMethod(setterName, Object.class);
					}
				}
				
			} else {
				method = bean.getClass().getMethod(setterName, Object.class);
			}
			if (method!=null) method.invoke(bean, value);
	
		} catch (NoSuchMethodException ne) {
			
			// Happens when they implemented with the object not the primitive value.
			if (tryDirectValue) {
				final Method method;
				if (value != null) {
					method = bean.getClass().getMethod(setterName, value.getClass());
				} else {
					method = bean.getClass().getMethod(setterName, Object.class);
				}
				method.invoke(bean, value);
			}
		}
	}

	/**
	 * There is a better way to do this but cannot find it again
	 *  // TODO Use factory method for getting primitive type
	 * @param number
	 * @return
	 */
	private static Class<?> getPrimitiveClass(Number number) {
		if (number instanceof Double) {
			return double.class;
			
		} else if (number instanceof Float) {
			return float.class;
	
		} else if (number instanceof Integer) {
			return int.class;
			
		} else if (number instanceof Long) {
			return long.class;
			
		} else if (number instanceof Short) {
			return short.class;
	
		} else if (number instanceof Byte) {
			return byte.class;
		}
		
		return null;
	}
}
