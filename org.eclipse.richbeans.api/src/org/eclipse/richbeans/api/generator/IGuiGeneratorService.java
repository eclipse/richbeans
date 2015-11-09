package org.eclipse.richbeans.api.generator;

/**
 * 
 * We would like to make the API bundle have no SWT dependency.
 */
public interface IGuiGeneratorService {

	//TODO FIXME I should have added generics when I changed this code Colin, apologies
	// This would mirror the other services, malcolm, remote dataset, scanning etc.
	// Suggested design:
    /**
     * IGuiGeneratorService  service = ... // OSGi
     * IGenerator<Composite> gen     = service.createGenerator(...); // An SWT generator but a swing or javascript one might be added later.
     * 
     * Composite comp = gen.generateGui(bean, parent);
     * 
     *  Then createGenerator(...) would generate any toolkit and generics would take care of the declaration.
     */
	
	/**
	 * TODO
	 * 
	 * @param bean
	 * @param parent
	 * @return
	 */
	// TODO decide if we also want to pass a style parameter
	public Object generateGui(Object bean, Object parent);
}
