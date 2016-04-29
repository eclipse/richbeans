package org.eclipse.richbeans.api.generator;

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
}
