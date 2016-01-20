package org.eclipse.richbeans.api.generator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Interface for runtime generation of SWT GUIs from Java objects (usually beans)
 *
 * @author Colin Palmer
 */
public interface IGuiGeneratorService {

	/**
	 * Generate a GUI for the given bean
	 * 
	 * @param bean An object to generate a GUI for
	 * @param parent A composite which will be the parent of the new control
	 * @return The new GUI
	 */
	public Control generateGui(Object bean, Composite parent);
}
