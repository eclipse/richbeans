package org.eclipse.dawnsci.macro.api;

/**
 * A generic MacroEventObject to represent deleting an object
 * in python, setting it to None.
 * 
 * @author fcp94556
 *
 */
public class DeleteEventObject extends MacroEventObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9140611001543637314L;

	public DeleteEventObject(Object source, String name) {
		super(source);
		String cmd = getLegalName(name)+" = None";
		setPythonCommand(cmd);
	}
	
	/**
	 * Override to stop the python in the command being auto-generated.
	 * @return
	 */
    public boolean isGeneratable() {
    	return false;
    }

}
