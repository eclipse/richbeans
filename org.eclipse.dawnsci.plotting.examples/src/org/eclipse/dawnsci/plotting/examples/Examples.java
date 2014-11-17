package org.eclipse.dawnsci.plotting.examples;

import org.eclipse.dawnsci.analysis.api.dataset.IDatasetMathsService;
import org.eclipse.dawnsci.analysis.api.io.ILoaderService;
import org.eclipse.dawnsci.plotting.api.IPlottingService;
import org.eclipse.dawnsci.plotting.api.histogram.IPaletteService;
import org.osgi.service.component.ComponentContext;
/**
 * Class to inject OSGI services to.
 * @author fcp94556
 *
 */
public class Examples {
	
	private IDatasetMathsService datasetMathsService;
	private ILoaderService       loaderService;
	private IPlottingService     plottingService;
	private IPaletteService      paletteService;
	
	public IPaletteService getPaletteService() {
		return paletteService;
	}
	public void setPaletteService(IPaletteService paletteService) {
		this.paletteService = paletteService;
	}
	public IDatasetMathsService getDatasetMathsService() {
		return datasetMathsService;
	}
	public void setDatasetMathsService(IDatasetMathsService datasetMathsService) {
		this.datasetMathsService = datasetMathsService;
	}
	public ILoaderService getLoaderService() {
		return loaderService;
	}
	public void setLoaderService(ILoaderService loaderService) {
		this.loaderService = loaderService;
	}
	public IPlottingService getPlottingService() {
		return plottingService;
	}
	public void setPlottingService(IPlottingService plottingService) {
		this.plottingService = plottingService;
	}
	
	private static Examples current;

	public void start(ComponentContext context) {
		current = this;
	}
	
	public void stop() {
		current = null;
	}

	public static Examples getCurrent() {
		return current;
	}
}
