
package sdi.data;

/**
 * container for methods that check validity, when semantic checking is
 * needed.
 * @author faden@cottagesystems.com
 */
public class Validator {

    private Validator() {
    }
    
    /**
     * checks for:<ol>
     * <li> monotonicity
     * <li> the x bin reference is within [low of bin i, lo of bin i+1],
     * <li> no repeat reference values
     * </ol>
     *
     * @param d the SimpleContiguousBinnedData1D
     * @throws IllegalArgumentException when the are semantic errors with the data. 
     */
    public static void checkValid(SimpleContiguousBinnedData1D d) {
        // First check for monotonicity of bin boundaries:
        int n = d.size();
        for (int i = 0; i < n - 1; i++) {
            if (d.getXBinLo(i) >= d.getXBinLo(i + 1)) {
                throw new IllegalArgumentException("bad bin");
            }
        }
        if (d.getXBinLo(n - 1) >= d.getLastXBinHi()) {
            throw new IllegalArgumentException("bad last bin");
        }

        // now check that refs are inside the right range
        for (int i = 0; i < n - 1; i++) {
            double r = d.getXBinReference(i);
            if (r == d.getXBinReference(i + 1)) {
                throw new IllegalArgumentException("duplicate ref");
            }
            if ((r < d.getXBinLo(i)) || (r > d.getXBinLo(i + 1))) {
                throw new IllegalArgumentException("bad ref");
            }
        }

        {
            double r = d.getXBinReference(n - 1);
            if ((r < d.getXBinLo(n - 1)) || (r > d.getLastXBinHi())) {
                throw new IllegalArgumentException("bad last ref");
            }
        }
    }

    /**
     * This checks for valid 
     * SimpleContiguousBinnedData1D, and also verifies that it can getFillDetector.
     * @param d the ContiguousBinnedData1D.
     * @throws IllegalArgumentException when the are semantic errors with the data. 
     */
    public static void checkValid(ContiguousBinnedData1D d) {
        checkValid((SimpleContiguousBinnedData1D) d);
        d.getFillDetector(); // do something trivial
    }
    
    /**
     * Check for valid bin, where the reference value must be within the
     * min, max boundaries.
     * @param d the Bin.
     * @throws IllegalArgumentException when the are semantic errors with the Bin.
     */
    public static void checkValid(Bin d) {
        if ( d.getMin() > d.getReference() ) throw new IllegalArgumentException( "reference must be greater than or equal to min");
        if ( d.getMax() < d.getReference() ) throw new IllegalArgumentException( "reference must be less than or equal to max");
    }
    
    /**
     * return true if the data is valid.  
     * @param d the ContiguousBinnedData1D.
     * @return true if the data is valid.
     */
    public static boolean isValid(ContiguousBinnedData1D d) {
        try {
            checkValid(d);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

}
