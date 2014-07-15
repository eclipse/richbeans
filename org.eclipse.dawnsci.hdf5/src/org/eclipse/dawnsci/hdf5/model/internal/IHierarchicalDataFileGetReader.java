package org.eclipse.dawnsci.hdf5.model.internal;

import org.eclipse.dawnsci.hdf5.IHierarchicalDataFile;
import org.eclipse.dawnsci.hdf5.model.IHierarchicalDataModel;

public interface IHierarchicalDataFileGetReader {
	/**
	 * Obtains the IHierarchicalDataFile that should be used by {@link IHierarchicalDataModel}
	 * @return a IHierarchicalDataFile
	 * @throws Exception if there was any problem obtaining the reader
	 */
	IHierarchicalDataFile getReader() throws Exception;
}
