package org.eclipse.richbeans.api.generator;

import java.beans.PropertyChangeListener;

/**
 * Factory to create proxies for simple beans that implement 
 * Property change support so that other components can listen
 * for changes. 
 * 
 * @author Kevin Savage
 *
 * @param <T> The type to proxy
 */
public interface IListenableProxyFactory {
	public <S extends T, T> T createProxyFor(S original, Class<T> interfaceImplemented);
	
	/**
	 * used so the proxy knows about the methods that you need to implement for property change support
	 */
	public interface PropertyChangeInterface{
		public void addPropertyChangeListener(PropertyChangeListener listener);
		public void removePropertyChangeListener(PropertyChangeListener listener);
	}
}
