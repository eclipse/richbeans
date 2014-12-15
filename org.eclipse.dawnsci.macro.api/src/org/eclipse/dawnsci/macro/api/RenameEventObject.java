package org.eclipse.dawnsci.macro.api;

/**
 * A generic MacroEventObject to represent renaming an object
 * 
 * Can be used whereever a newName replaces an old name, works
 * in python and jython.
 * 
 * @author fcp94556
 *
 */
public class RenameEventObject extends MacroEventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3255570751427163372L;

	public RenameEventObject(Object source, String newName, String oldName) {
		super(source);
		String cmd = createCommand(newName, oldName);
		setPythonCommand(cmd);
	}

	private String createCommand(String newName, String oldName) {
		StringBuilder buf = new StringBuilder(MacroUtils.getLegalName(newName));
		buf.append(" = ");
		buf.append(MacroUtils.getLegalName(oldName));
		buf.append("\n");
		buf.append(MacroUtils.getLegalName(oldName));
		buf.append(" = None");
		return buf.toString();
	}

	/**
	 * Override to stop the python in the command being auto-generated.
	 * @return
	 */
    public boolean isGeneratable() {
    	return false;
    }

}
