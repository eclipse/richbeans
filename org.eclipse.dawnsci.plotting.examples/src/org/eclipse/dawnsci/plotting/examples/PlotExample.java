package org.eclipse.dawnsci.plotting.examples;

import org.eclipse.dawnsci.analysis.api.io.ILoaderService;
import org.eclipse.dawnsci.plotting.api.IPlottingService;
import org.eclipse.dawnsci.plotting.api.IPlottingSystem;
import org.eclipse.ui.part.ViewPart;

public abstract class PlotExample extends ViewPart {

	protected ILoaderService  service;
	protected IPlottingSystem system;

	public PlotExample() {
		
		// A service for loading data from any data file format.
		service = (ILoaderService)Activator.getService(ILoaderService.class);
		
		final IPlottingService pservice = (IPlottingService)Activator.getService(IPlottingService.class);
		try {
			this.system = pservice.createPlottingSystem();
		} catch (Exception ne) {
			ne.printStackTrace(); // Or your favourite logging.
		}
			
	}
	
	protected abstract String getFileName();

	@Override
	public void dispose() {
		super.dispose();
		system.dispose();
	}
	
	@Override
	public Object getAdapter(Class clazz) {
		if (system.getAdapter(clazz)!=null) return system.getAdapter(clazz);
		return super.getAdapter(clazz);
	}

	@Override
	public void setFocus() {
		system.setFocus();
	}

}
