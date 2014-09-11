package org.eclipse.dawnsci.plotting.api.trace;

import java.util.List;

import uk.ac.diamond.scisoft.analysis.dataset.IDataset;

public interface IIsosurfaceTrace extends IImage3DTrace {

	
	/**
	 * Plot an isosurface on the plotting system.
	 * @param points      (float)
	 * @param textCoords  (float)
	 * @param faces       (int)
	 * @param axes        May be null.
	 */
	public void setData(final IDataset points, 
			            final IDataset textCoords, 
			            final IDataset faces, 
                        final List<? extends IDataset> axes);


	/**
	 * Default is CullFace.NONE
	 * @return
	 */
	public CullFace getCullFace();
	
	/**
	 * Default is CullFace.NONE
	 * @param culling
	 */
	public void setCullFace(CullFace culling);
	
	/**
	 * The RGB components of the material
	 * @return
	 */
	public int[] getMaterialRBG();
	
	/**
	 * The Opacity component of the material
	 * @return
	 */
	public double getMaterialOpacity();
	
	/**
     * @param red the red component, in the range {@code 0-255}
     * @param green the green component, in the range {@code 0-255}
     * @param blue the blue component, in the range {@code 0-255}
     * @param opacity the opacity component, in the range {@code 0.0-1.0}
     * @return the {@code Color}
     * @throws IllegalArgumentException if any value is out of range
	 */
	public void setMaterial(int red, int green, int blue, double opacity);
	
	/**
	private double scaleX = 400; 
	private double scaleY = 520;
	private double scaleZ = 0;
	private double translateX = 1;
	private double translateY = 1;
	private double translateZ = 1;
	**/

	public double getScaleX();

	public void setScaleX(double scaleX);

	public double getScaleY();

	public void setScaleY(double scaleY);

	public double getScaleZ();

	public void setScaleZ(double scaleZ);

	public double getTranslateX();

	public void setTranslateX(double translateX);

	public double getTranslateY();

	public void setTranslateY(double translateY);

	public double getTranslateZ();

	public void setTranslateZ(double translateZ);

	
	public enum CullFace {

	    /**
	     * Do not perform any face culling.
	     */
	    NONE,

	    /**
	     * Cull all back-facing polygons. BACK is defined as clockwise winding
	     * order.
	     */
	    BACK,

	    /**
	     * Cull all front-facing polygons. FRONT is defined as counter-clockwise
	     * winding order.
	     */
	    FRONT
	}
}
