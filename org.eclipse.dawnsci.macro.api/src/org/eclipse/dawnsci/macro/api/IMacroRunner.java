package org.eclipse.dawnsci.macro.api;

/**
 * Interface for running macros in the workbench. Normally the runner of the macros
 * is DAWN and the caller is an interpreter like Jython or Python.
 * 
 * The runner creates a local jython interpreter to accept the commands from the external
 * interpreter and execute them in the workbench.
 * 
 * For instance the external interpreter may wish to send a command to set the title of a plot.
 * 
 * Generally the macro runner is published on RMI.
 * 
 * @author fcp94556
 *
 */
public interface IMacroRunner {

	/**
	 * Execute a string command
	 * @param s
	 * @throws Exception 
	 */
	void exec(String s) throws Exception;

	
}
