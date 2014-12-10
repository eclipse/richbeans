package org.eclipse.dawnsci.macro.api;

/**
 * Implement this interface and then fire events using the IMacroService.publish(..)
 * to allow pydev consoles and script files to record your actions when using the GUI.
 * 
 * @author fcp94556
 *
 */
public interface IMacroCommandProvider {

	/**
	 * Implement to provide the python to do this event in 
	 * python console or script.
	 * @return
	 */
	public String getPythonCommand();
	
	/**
	 * Implement to provide the jython to do this event 
	 * in jython console or script.
	 * @return
	 */
	public String getJythonCommand();
}
