package sdi.data;

/**
 *
 * @author jbf
 */
public interface UncertaintyProvider {
   double getUncertPlus(int i);
   double getUncertMinus(int i);  
}
