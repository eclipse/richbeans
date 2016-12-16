/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.richbeans.widgets;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	private static BundleContext context;

	@Override
	public void start(BundleContext c) throws Exception {
		context = c;
	}

	@Override
	public void stop(BundleContext c) throws Exception {
		context = null;
	}

	public static Image getImage(String iconPath) {
		ImageDescriptor des = getImageDesciptor(iconPath);
		return des.createImage();
	}

	public static ImageDescriptor getImageDesciptor(String iconPath) {

		if (context==null) { // Testing
			final File file = new File("../org.eclipse.richbeans.widgets/"+iconPath);
			final ImageData data = file.exists() ? new ImageData(file.getAbsolutePath()) : new ImageData(16, 16, 24, new PaletteData(0xff0000, 0x00ff00, 0x0000ff));
			return new ImageDescriptor() {				
				@Override
				public ImageData getImageData() {
					return data;
				}
			};
		}
		
		Bundle bundle = context.getBundle();

		// look for the image (this will check both the plugin and fragment
		// folders
		URL fullPathString = bundle.getEntry(iconPath);
		if (fullPathString == null) {
			try {
				fullPathString = new URL(iconPath);
			} catch (MalformedURLException e) {
				return null;
			}
		}

		return ImageDescriptor.createFromURL(fullPathString);
	}

	public static <T> T getService(Class<T> serviceClass) {
		ServiceReference<T> ref = context.getServiceReference(serviceClass);
		return context.getService(ref);
	}
	
	public static Object getService(String serviceClass) {
		ServiceReference<?> ref = context.getServiceReference(serviceClass);
		return context.getService(ref);
	}

}
