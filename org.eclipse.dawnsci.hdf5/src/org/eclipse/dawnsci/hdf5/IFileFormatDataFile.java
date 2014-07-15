package org.eclipse.dawnsci.hdf5;

import ncsa.hdf.object.FileFormat;

/**
 * Internal use only.
 * 
 * @author fcp94556
 *
 */
public interface IFileFormatDataFile extends IHierarchicalDataFile {

	public FileFormat getFileFormat();

}
