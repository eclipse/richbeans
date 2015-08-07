package org.eclipse.richbeans.api.generator;

import org.eclipse.swt.widgets.Composite;

public interface IGuiGeneratorService {

	/**
	 * TODO
	 * 
	 * @param bean
	 * @param parent
	 * @return
	 */
	// TODO decide if we also want to pass a style parameter
	public Composite generateGui(Object bean, Composite parent);
}
