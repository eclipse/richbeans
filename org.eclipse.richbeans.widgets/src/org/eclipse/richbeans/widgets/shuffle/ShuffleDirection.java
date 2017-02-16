/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.richbeans.widgets.shuffle;

public enum ShuffleDirection {

	/**
	 * Used for the event where a list has had an item removed from it
	 * because it is being shuffled elsewhere
	 */
	DELETE,
	
	/**
	 * Event where an item is shuffled from left to right
	 */
	LEFT_TO_RIGHT,

	/**
	 * Event where an item is shuffled from right to left
	 */
	RIGHT_TO_LEFT, 
	
	/**
	 * Move an item up in the left list
	 */
	LEFT_UP, 
	
	/**
	 * Move an item down in the left list
	 */
	LEFT_DOWN, 
	
	/**
	 * Move an item up in the right list
	 */
	RIGHT_UP, 
	
	/**
	 * Move an item down in the right list
	 */
	RIGHT_DOWN;
}
