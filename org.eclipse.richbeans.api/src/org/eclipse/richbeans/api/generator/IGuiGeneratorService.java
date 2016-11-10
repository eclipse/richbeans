package org.eclipse.richbeans.api.generator;

/**
 * Interface for runtime generation of SWT GUIs from Java objects (usually beans)
 *
 * @author Colin Palmer
 */
public interface IGuiGeneratorService {

	/**
	 * Generate a GUI for the given bean.
	 * 
	 * @param bean the object to generate a GUI for
	 * @param parent the parent composite of the new control
	 * @return the GUI for the given object
	 */
	public <T> T generateGui(Object bean, T parent);

	/**
	 * Open a dialog to edit the given bean, and return when the dialog is closed. The bean will be edited in place,
	 * and no cancellation of edits is currently possible.
	 *
	 * @param bean the object to edit in the dialog
	 * @param parent the parent shell of the dialog, or <code>null</code> if none
	 * @param title the dialog's title, or <code>null</code> if none
	 * @return <code>void</code> so if a return type is added in future it will not break existing client code
	 */
	public <T> void openDialog(Object bean, T parent, String title);
}
