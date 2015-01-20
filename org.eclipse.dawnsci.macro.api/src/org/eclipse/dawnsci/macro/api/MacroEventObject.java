package org.eclipse.dawnsci.macro.api;

import java.util.EventObject;

public class MacroEventObject extends EventObject {	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1373321229445040760L;

	private String pythonCommand;
	private String jythonCommand;
	private boolean isGen = true;
	private boolean isImmediate = false;

	public MacroEventObject(Object arg0) {
		super(arg0);
	}
	public MacroEventObject(Object arg0, String cmd) {
		this(arg0, cmd, cmd);
	}
	public MacroEventObject(Object arg0, String cmd, boolean immediate) {
		this(arg0, cmd, cmd);
		isImmediate = immediate;
	}
	public MacroEventObject(Object arg0, String pcmd, String jcmd) {
		super(arg0);
		pythonCommand=pcmd;
		jythonCommand=jcmd;
		isGen = false;
	}


	public String getPythonCommand() {
		if (pythonCommand!=null) return pythonCommand;
		if (getSource() instanceof IMacroCommandProvider) return ((IMacroCommandProvider)getSource()).getPythonCommand();
		return null;
	}

	public void setPythonCommand(String pythonCommand) {
		this.pythonCommand = pythonCommand;
	}

	public String getJythonCommand() {
		if (jythonCommand!=null) return jythonCommand;
		if (getSource() instanceof IMacroCommandProvider) return ((IMacroCommandProvider)getSource()).getJythonCommand();
		if (pythonCommand!=null) return pythonCommand;
		return null;
	}

	public void setJythonCommand(String jythonCommand) {
		this.jythonCommand = jythonCommand;
	}

	/**
	 * Override to stop the python in the command being auto-generated.
	 * @return
	 */
    public boolean isGeneratable() {
    	return isGen;
    }
    
    public String toString() {
    	return getPythonCommand();
    }
    

	public boolean isImmediate() {
		return isImmediate;
	}
	public void setImmediate(boolean isImmediate) {
		this.isImmediate = isImmediate;
	}

	public void prepend(String command) {
		if (!command.endsWith("\n")) command = command+"\n";
		setPythonCommand(command+getPythonCommand());
		setJythonCommand(command+getJythonCommand());
	}

	public void append(String command) {
		setPythonCommand(getPythonCommand()+"\n"+command);
		setJythonCommand(getJythonCommand()+"\n"+command);
	}

}
