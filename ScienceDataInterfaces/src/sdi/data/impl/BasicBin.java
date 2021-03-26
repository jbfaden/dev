package sdi.data.impl;

import sdi.data.Bin;

public class BasicBin implements Bin
{
	public static BasicBin of(double min, double max, double reference) {
		return new BasicBin(min, max, reference);
	}

	private final double min; 
	private final double max;
	private final double reference;

	private BasicBin(double min, double max, double reference) {
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