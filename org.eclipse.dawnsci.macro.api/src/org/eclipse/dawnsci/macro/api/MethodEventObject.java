package org.eclipse.dawnsci.macro.api;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Parses the calling method from the stack and creates a python command
 * of the form:
 * 
 *          varName.methodName(pythonified arguements)
 *  
 *  for instance:
 *  		if (mservice!=null) mservice.publish(new MethodEventObject("ps", this, "fred"));
 *  
 *  from a method called 'setTitle' (in any class)
 *
 *  gives:
 *          ps.setTitle("fred") as the python command.
 *          
 * The method name is parsed out of the thread stack as the method before the
 * constructor of this class.
 * 
 * You may also specify the method name directly using the second constructor
 * This aviods any stack parsing.
 *  
 * @author fcp94556
 *
 */
public class MethodEventObject extends MacroEventObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2951162101084315254L;

	/**
	 * Gets the method name from the stack.
	 * @param varName
	 * @param source
	 * @param args
	 */
	public MethodEventObject(String varName, Object source, Object... args) {
		
		this(varName, getCallingMethodName(Thread.currentThread().getStackTrace()), source, args);
	}
	
	/**
	 * Specifies the method name to call on the object varName.
	 * @param varName
	 * @param methodName
	 * @param source
	 * @param args
	 */
	public MethodEventObject(String varName, String methodName, Object source, Object... args) {
		super(source);
		setPythonCommand(varName+"."+methodName+"("+getPythonArgs(args)+")");
	}


	private String getPythonArgs(Object[] args) {
		
		if (args==null || args.length<1) return "";
		StringBuilder buf = new StringBuilder();
		for (Iterator<Object> it = Arrays.asList(args).iterator(); it.hasNext();) {
			Object next = it.next();
			buf.append(MacroUtils.toPythonString(next));
			if (it.hasNext()) buf.append(", ");
		}
		return buf.toString();
	}

	private static String getCallingMethodName( StackTraceElement ste[] ) {  
		   
	    String methodName = "";  
	    boolean flag = false;  
	   
	    for ( StackTraceElement s : ste ) {  
	   
	        if ( flag ) {  
	   
	            methodName = s.getMethodName();  
	            break;  
	        }  
	        flag = s.getMethodName().equals( "<init>" );  
	    }  
	    return methodName;  
	}

    public boolean isGeneratable() {
    	return false;
    }
 
}
