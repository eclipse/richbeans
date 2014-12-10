package org.eclipse.dawnsci.macro.api;

import java.util.EventListener;

public interface IMacroEventListener extends EventListener {

	/**
	 * Called when a macro event happened which could be recorded
	 * in the console or macro editor.
	 * 
	 * @param mevt
	 */
	void macroChangePerformed(MacroEventObject mevt);
	
	/**
	 * 
	 * @return true if the listener isDisposed.
	 * In theory listeners should remove themselves when they are disposed but
	 * just in case we force listeners to have a method to do this.
	 */
	boolean isDisposed();
}
