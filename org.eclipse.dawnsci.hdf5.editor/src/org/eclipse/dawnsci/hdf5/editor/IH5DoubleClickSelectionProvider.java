package org.eclipse.dawnsci.hdf5.editor;

import org.eclipse.jface.viewers.ISelection;

public interface IH5DoubleClickSelectionProvider  {

	/**
	 * We implement an overriding selection to transform a double click into firing
	 * some other kind of selection event.
	 * 
	 * @param selection
	 * @param filePath
	 * @return
	 */
	public ISelection getSelection(ISelection selection, String filePath) throws Exception;
}
