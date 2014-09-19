/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.plotting.api.trace;

import java.util.EventObject;

import org.eclipse.swt.graphics.PaletteData;

/**
 * Event used for palette changes, including change of Palette Data.
 * @author Matthew Gerring
 *
 */
public class PaletteEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5574618484168809185L;

	protected IPaletteTrace trace;
	protected PaletteData paletteData;
	
	public PaletteEvent(Object source, PaletteData paletteData) {
		super(source);
		this.trace       = (IPaletteTrace)source;
		this.paletteData = paletteData;
	}

	public IPaletteTrace getTrace() {
		return trace;
	}

	public void setTrace(IImageTrace trace) {
		this.trace = trace;
	}

	/**
	 * May be null!
	 * @return
	 */
	public PaletteData getPaletteData() {
		return paletteData;
	}

	public void setPaletteData(PaletteData paletteData) {
		this.paletteData = paletteData;
	}

	public IImageTrace getImageTrace() {
		return (IImageTrace)trace;
	}

}
