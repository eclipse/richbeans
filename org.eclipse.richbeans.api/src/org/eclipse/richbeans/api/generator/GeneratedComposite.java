package org.eclipse.richbeans.api.generator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class GeneratedComposite extends Composite {

	public GeneratedComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Finds the child Control with the given name.
	 */
	public abstract <T extends Control> T getControl(String name);
}
