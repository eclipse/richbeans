/*-
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.richbeans.widgets.table;

import java.util.function.Predicate;

/**
 * 
 * A view whose main contant is a SeriesTable may optionally implement this
 * interface. This allows other views to interact with the items in the view.
 * 
 * It is something of a back door and should be used with caution. Instead
 * normal RCP selection events should be used to communicate between views 
 * or a proper controller pattern. However in the cases where a series view
 * should be searched for and changed, this interface can be used to avoid
 * depending on the concrete part.
 * 
 * @author Matthew Gerring 
 *
 */
public interface SeriesItemView {

	/**
	 * Test if the items in the series table 
	 * @param class1
	 * @return
	 */
	<T> boolean isSeriesOf(Class<T> class1);

	/**
	 * Searchs for the first ISeriesItemDescriptor matching the defined predicate.
	 * @param predicate
	 * @return
	 */
	ISeriesItemDescriptor find(Predicate<ISeriesItemDescriptor> predicate);

}
