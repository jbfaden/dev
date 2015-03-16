/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdi.data;

/**
 *
 * @author jbf
 */
public class Validator {

    /**
     * checks for:<ol>
     * <li> monotonicity
     * <li> the x bin reference is within [low of bin i, lo of bin i+1],
     * <li> no repeat ref. values
     * </ol>
     *
     * could write one of these that traps exceptions and return true or false
     *
     * @param d the SimpleContiguousBinnedData1D
     */
    public static void checkValid(SimpleContiguousBinnedData1D d) {
        // First check for monotonicity of bin boundaries:
        int n = d.size();
        for (int i = 0; i < n - 1; i++) {
            if (d.getXBinLo(i) >= d.getXBinLo(i + 1)) {
                throw new RuntimeException("bad bin");
            }
        }
        if (d.getXBinLo(n - 1) >= d.getLastXBinHi()) {
            throw new RuntimeException("bad last bin");
        }

        // now check that refs are inside the right range
        for (int i = 0; i < n - 1; i++) {
            double r = d.getXBinReference(i);
            if (r == d.getXBinReference(i + 1)) {
                throw new RuntimeException("duplicate ref");
            }
            if ((r < d.getXBinLo(i)) || (r > d.getXBinLo(i + 1))) {
                throw new RuntimeException("bad ref");
            }
        }

        {
            double r = d.getXBinReference(n - 1);
            if ((r < d.getXBinLo(n - 1)) || (r > d.getLastXBinHi())) {
                throw new RuntimeException("bad last ref");
            }
        }
    }

    public static void checkValid(ContiguousBinnedData1D d) {
        checkValid((SimpleContiguousBinnedData1D) d);
        d.getFillDetector(); // do something trivial
    }

    public static boolean isValid(ContiguousBinnedData1D d) {
        try {
            checkValid(d);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

}
