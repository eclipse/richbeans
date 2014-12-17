package org.eclipse.dawnsci.macro.api;

import java.util.Arrays;

public abstract class AbstractMacroGenerator<T> {

	/**
	 * Looks at the class of the source of this event 
	 * and tries to see if there is a standard macro.
	 * 
	 * Basically sets the python and jython commands on the event
	 * if the source generates commands.
	 * 
	 * @param evt
	 * @return
	 */
	public MacroEventObject generate(MacroEventObject evt) {
		
        final Object source = evt.getSource();        
        String cmd = getPythonCommand((T)source);
        if (cmd!=null) evt.setPythonCommand(cmd);
        
        cmd = getJythonCommand((T)source);
        if (cmd!=null) evt.setJythonCommand(cmd);
       
        return evt;
	}
	
	/**
	 * Get the specific python command for an object or null if one cannot be generated.
	 * @param source
	 * @return
	 */
	public abstract String getPythonCommand(T source);
	
	/**
	 * Get the specific jython command for an object or null if one cannot be generated.
	 * @param source
	 * @return
	 */
	public abstract String getJythonCommand(T source);
	

}
