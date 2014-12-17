/*-
 * Copyright 2014 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.plotting.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadSafeObject {
	
	private static Logger logger = LoggerFactory.getLogger(ThreadSafeObject.class);

	protected Object delegate;
	public ThreadSafeObject(Object delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * Calls method in a SWT thread safe way.
	 * @param methodName
	 * @param args
	 */
	protected Object call(final String methodName, final Object... args) {
		
		@SuppressWarnings("rawtypes")
		final Class[] classes = args!=null ? new Class[args.length] : null;
		if (classes!=null) {
			for (int i = 0; i < args.length; i++) classes[i]=args[i].getClass();
		}
		return call(methodName, classes, args);
	}
	
	/**
	 * Calls method in a SWT thread safe way.
	 * @param methodName
	 * @param args
	 */
	protected Object call(final String methodName, @SuppressWarnings("rawtypes") final Class[] classes, final Object... args) {
		
		final List<Object> ret = new ArrayList<Object>(1);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
				    Method method = delegate.getClass().getMethod(methodName, classes);
				    Object val    = method.invoke(delegate, args);
				    ret.add(val);
				} catch (Exception ne) {
					logger.error("Cannot execute "+methodName+" with "+args, ne);
				}
			}
		});
		return ret.get(0);
	}

	public static String getMethodName ( StackTraceElement ste[] ) {  
		   
	    String methodName = "";  
	    boolean flag = false;  
	   
	    for ( StackTraceElement s : ste ) {  
	   
	        if ( flag ) {  
	   
	            methodName = s.getMethodName();  
	            break;  
	        }  
	        flag = s.getMethodName().equals( "getStackTrace" );  
	    }  
	    return methodName;  
	}
}
