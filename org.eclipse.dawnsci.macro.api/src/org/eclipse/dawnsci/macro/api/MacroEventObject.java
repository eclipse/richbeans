package org.eclipse.dawnsci.macro.api;

import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacroEventObject extends EventObject {	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1373321229445040760L;

	private String pythonCommand;
	private String jythonCommand;

	public MacroEventObject(Object arg0) {
		super(arg0);
	}
	public MacroEventObject(Object arg0, String cmd) {
		super(arg0);
		pythonCommand=cmd;
		jythonCommand=cmd;
	}
	public MacroEventObject(Object arg0, String pcmd, String jcmd) {
		super(arg0);
		pythonCommand=pcmd;
		jythonCommand=jcmd;
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
		return null;
	}

	public void setJythonCommand(String jythonCommand) {
		this.jythonCommand = jythonCommand;
	}

	public String getLegalName(String setName) {
		if (setName.endsWith("/"))   setName = setName.substring(0,setName.length()-1);
		if (setName.indexOf('/')>-1) setName = setName.substring(setName.lastIndexOf('/'));
		
		setName = setName.replaceAll(" ", "_");
		setName = setName.replaceAll("[^a-zA-Z0-9_]", "");
		final Matcher matcher = Pattern.compile("(\\d+)(.+)").matcher(setName);
		if (matcher.matches()) {
			setName = matcher.group(2);
		}
		
		if (Pattern.compile("(\\d+)").matcher(setName).matches()) {
			setName= "x"+setName;
		}
		return setName;
	}

    
}
