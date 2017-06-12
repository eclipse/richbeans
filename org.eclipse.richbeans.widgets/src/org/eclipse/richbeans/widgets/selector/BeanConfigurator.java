package org.eclipse.richbeans.widgets.selector;

import java.util.List;

/**
 * A configurator may be set on a list editor to 
 * provide configuration of a bean when it is added.<p>
 * 
 * The configurator is a functional interface allowing beans when created and added to be
 * compared with existing values and changed. This allows checks to be done of the current
 * state of added items and the new item to be added with sensible defaults. <p>
 * 
 * For instance:<p>
 * <code>
     ListEditor<MyBean> myListEditor = new VerticalListEditor<>();
     // myListEditor...
     // MyBean bean, MyBean previous, List<MyBean> context
     myListEditor.setBeanConfigurator((bean, previous, context)->contiguous(bean, previous));
     
   </code><p>
   
   Here the contiguous(...) method checks that the values of bean are contiguous with previous
   and sets those values as the new bean is added.

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
	void configure(T bean, T previous, List<T> context);
}
