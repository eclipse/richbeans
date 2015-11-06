package org.eclipse.richbeans.api.generator;

/**
 * 
 * We would like to make the API bundle have no SWT dependency.
 */
public interface IGuiGeneratorService {

	/**
	 * TODO
	 * 
	 * @param bean
	 * @param parent
	 * @return
	 */
	// TODO decide if we also want to pass a style parameter
	public Object generateGui(Object bean, Object parent) throws Exception;
}
