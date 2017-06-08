package org.eclipse.richbeans.widgets.selector;

/**
 * A configurator may be set on a list editor to 
 * provide configuration of a bean when it is added.
 * 
 * 
 * 
 * @author Matthew Gerring
 *
 */
@FunctionalInterface
public interface BeanConfigurator<T> {

	/**
	 * 
	 * @param bean     - the bean we will add (created via a no-argument constructor)
	 * @param previous - the previous bean in the list of beans or null
	 * @param context  - the parent bean which contains the list of bean which we are editing
	 */
	void configure(T bean, T previous, Object context);
}
