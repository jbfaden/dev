package sdi.functions;

/**
 * Interface representing a univariate function of double returning double.
 * 
 * @author James.Peachey@jhuapl.edu
 *
 */
public interface SimpleScalarFunctionOfOneArg {
	/**
	 * Evaluate the function of the supplied parameter and return the value computed.
	 * @param t the parameter at which to evaluate the function
	 * @return the result of evaluating the function.
	 */
	double evaluate(double t);
}
