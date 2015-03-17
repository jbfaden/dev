package sdi.data;

import com.google.common.base.Preconditions;

/**
 * interval containing a reference location. The reference must be somewhere
 * within the interval. When a bin cannot be used, for example if data located
 * by the Bin is to be interpolated, then the reference value should be used.
 *
 * @author jbf
 */
public interface Bin {

    /**
     * return the minimum value for the bin
     *
     * @return the minimum value for the bin
     */
    double getMin();

    /**
     * return the maximum value for the bin
     *
     * @return the maximum value for the bin
     */
    double getMax();

    /**
     * return a single reference value for the bin, which must be within the
     * bin: getMin()&le;getReference()&le;getMax()
     *
     * @return a single reference value for the bin
     */
    double getReference();
}

/**
 * is this a reference implementation?
 */
class BinImpl implements Bin {

    private final double min;
    private final double max;
    /**
     * this is the "center" of the bin; note that min <= reference <= max
     */
    private final double reference;

    public BinImpl(double min, double max, double reference) {
        // TODO: clean up preconditions check
        Preconditions.checkArgument(min <= reference, "min %s must be less than ref %s", min, reference);
        Preconditions.checkArgument(max >= reference, "blah blah");
        this.min = min;
        this.max = max;
        this.reference = reference;
    }

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public double getReference() {
        return reference;
    }
}
