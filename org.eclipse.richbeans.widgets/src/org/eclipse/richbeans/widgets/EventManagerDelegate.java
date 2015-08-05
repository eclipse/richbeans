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

import org.eclipse.richbeans.api.event.BoundsEvent;
import org.eclipse.richbeans.api.event.BoundsListener;
import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.event.ValueListener;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.FieldComposite.NOTIFY_TYPE;

/**
 * A class used to manage events in wrapper classes.
 * <p>
 * This has some logic to only broadcast events if the parent is 'on'.
 */
public class EventManagerDelegate {

	private IFieldWidget parent;
	private EventListenersDelegate delegate;

	/**
	 * @param par
	 */
	public EventManagerDelegate(IFieldWidget par) {
		this.parent = par;
		delegate = new EventListenersDelegate();
	}

	/**
	 * Add a listener to be notified of the user entering new values into the widget.
	 * 
	 * @param l
	 */
	public void addValueListener(final ValueListener l) {
		delegate.addValueListener(l);
	}

	/**
	 * Remove a certain listener - can also use the clearListeners(...) method.
	 * 
	 * @param l
	 */
	public void removeValueListener(final ValueListener l) {
		delegate.removeValueListener(l);
	}

	/**
	 * Internal use only
	 * 
	 * @param evt
	 */
	public void notifyValueListeners(final ValueEvent evt) {

		if (!checkCanNotify())
			return;
		try {
			parent.off();
			delegate.notifyValueListeners(evt);
		} catch (Throwable ne) {
			// We log here, saves going to the eclipse log
			// if the listener throws back an exception.
			ne.printStackTrace();
			throw new RuntimeException(ne);
		} finally {
			parent.on();
		}
	}

	/**
	 * Internal use only
	 * 
	 * @param evt
	 */
	public void notifyBoundsProviderListeners(final ValueEvent evt) {

		if (!checkCanNotify())
			return;
		try {
			parent.off();
			delegate.notifyBoundsProviderListeners(evt);
		} catch (Throwable ne) {
			// We log here, saves going to the eclipse log
			// if the listener throws back an exception.
			ne.printStackTrace();
			throw new RuntimeException(ne);
		} finally {
			parent.on();
		}
	}

	private boolean checkCanNotify() {

		if (parent == null)
			return false;

		if (delegate!= null && !delegate.checkCanNotify())
			return false;

		final FieldComposite comp = parent instanceof FieldComposite ? (FieldComposite) parent : null;

		if (comp != null && comp.getNotifyType() != null && comp.getNotifyType() == NOTIFY_TYPE.ALWAYS) {
			// We are going to always tell listeners when value changed - even if we are off.
		} else {
			if (!parent.isOn())
				return false;
		}

		return true;
	}

	/**
	 * Add a listener to be notified of the user entering new values into the widget.
	 * 
	 * @param l
	 */
	public void addBoundsListener(final BoundsListener l) {
		delegate.addBoundsListener(l);
	}

	/**
	 * Remove a certain listener - can also use the clearListeners(...) method.
	 * 
	 * @param l
	 */
	public void removeBoundsListener(final BoundsListener l) {
		delegate.removeBoundsListener(l);
	}

	/**
	 * Internal use only
	 * 
	 * @param evt
	 */
	public void notifyBoundsListeners(final BoundsEvent evt) {
		if (!parent.isOn())
			return;
		try {
			parent.off();
			delegate.notifyBoundsListeners(evt);
		} catch (Throwable ne) {
			// We log here, saves going to the eclipse log
			// if the listener throws back an exception.
			ne.printStackTrace();
			throw new RuntimeException(ne);
		} finally {
			parent.on();
		}
	}

	public void dispose() {
		delegate.dispose();
		parent = null;
	}

}
