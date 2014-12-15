package org.eclipse.dawnsci.macro.api;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractMacroGenerator {

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
        String cmd = getPythonCommand(source);
        if (cmd!=null) evt.setPythonCommand(cmd);
        
        cmd = getJythonCommand(source);
        if (cmd!=null) evt.setJythonCommand(cmd);
       
        return evt;
	}
	
	/**
	 * Get the specific python command for an object or null if one cannot be generated.
	 * @param source
	 * @return
	 */
	public abstract String getPythonCommand(Object source);
	
	/**
	 * Get the specific jython command for an object or null if one cannot be generated.
	 * @param source
	 * @return
	 */
	public abstract String getJythonCommand(Object source);
	
	/**
	 * Deals with primitive arrays
	 * @param value
	 */
	protected String toPythonString(Object value) {
		
		if (value==null) return null;
		
        if (value instanceof String) {
        	return "'"+(String)value+"'";
        	
        } else if (value instanceof Boolean) {
        	return ((Boolean)value).booleanValue() ? "True" : "False";
        	
        } else if (value instanceof short[]) {
        	return Arrays.toString((short[])value);
        	
        } else if  (value instanceof int[]) {
        	return Arrays.toString((int[])value);
        	
        } else if  (value instanceof long[]) {
        	return Arrays.toString((long[])value);
        	
        } else if  (value instanceof char[]) {
        	return Arrays.toString((char[])value);
        	
        } else if  (value instanceof float[]) {
        	return Arrays.toString((float[])value);
        	
        } else if  (value instanceof double[]) {
        	return Arrays.toString((double[])value);
        	
        } else if  (value instanceof boolean[]) {
        	return Arrays.toString((boolean[])value);
        	
        } else if  (value instanceof byte[]) {
        	return Arrays.toString((byte[])value);
        	
        } else if  (value instanceof Object[]) {
        	return Arrays.toString((Object[])value);
        }
        
        return value.toString();
	}

}
