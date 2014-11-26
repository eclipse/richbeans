package org.eclipse.dawnsci.analysis.api.processing;

import org.eclipse.dawnsci.analysis.api.metadata.OriginMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a running mechanism for running a pipeline
 * @author fcp94556
 *
 */
public interface IOperationRunner {

	public static final Logger logger = LoggerFactory.getLogger(IOperationRunner.class);

	/**
	 * 
	 * @param context
	 * @param originMetadata
	 */
	public void init(IOperationContext context, OriginMetadata originMetadata);
	
	/**
	 * Execute the whole pipeline
	 */
	public void execute() throws Exception;
}
