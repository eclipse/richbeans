package org.eclipse.richbeans.api.reflection;


public interface IBeanService {

	
	/**
	 * Create a controller for synching a bean tree with a user interface object.
	 * @param ui
	 * @param bean
	 * @return
	 */
	public IBeanController createController(Object ui, Object bean);


}
