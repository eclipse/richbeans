package org.eclipse.richbeans.reflection;

import org.eclipse.richbeans.api.reflection.IBeanController;
import org.eclipse.richbeans.api.reflection.IBeanService;

/**
 * Implementation of IBeanService which can be retrieved either by
 * OSGi (preferable) or a static method. This service is not a singleton.
 * 
 * @author Matthew Gerring
 *
 */
public class BeanService implements IBeanService {
	
	static {
		System.out.println("Creating BeanService");
	}
	
	private static BeanService instance;
	
	/**
	 * To be used for testing classes that do not have 
	 * OSGi available and therefore get a static instance.
	 * 
	 * Normally: inject service using OSGi.
	 * 
	 * @return
	 */
	public static BeanService getInstance() {
		if (instance == null) instance = new BeanService();
		return instance;
	}

	@Override
	public IBeanController createController(Object ui, Object bean) {
		return new BeanController(ui, bean);
	}

}
