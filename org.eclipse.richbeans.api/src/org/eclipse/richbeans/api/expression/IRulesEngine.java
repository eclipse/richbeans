package org.eclipse.richbeans.api.expression;

import java.util.Collection;

/**
 * Used to evaluate user interface rules as expressions.
 * @author Matthew Gerring
 *
 */
public interface IRulesEngine {

	/**
	 * 
	 * @param expression
	 * @throws Exception
	 */
	void createExpression(String expression) throws Exception;

	/**
	 * 
	 * @return
	 */
	Collection<String> getLazyVariableNamesFromExpression();

	/**
	 * 
	 * @param name
	 * @param value
	 */
	<T> void addLoadedVariable(String name, T value);

	/**
	 * 
	 * @return
	 */
	<T> T evaluate();


}
