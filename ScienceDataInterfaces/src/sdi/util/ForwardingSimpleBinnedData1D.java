package sdi.util;

import sdi.data.Bin;
import sdi.data.SimpleBinnedData1D;

public class ForwardingSimpleBinnedData1D implements SimpleBinnedData1D {
	private final SimpleBinnedData1D data;

	protected ForwardingSimpleBinnedData1D(SimpleBinnedData1D data) {
		this.data = data;
		
	}

	protected SimpleBinnedData1D getData() {
		return data;
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public Bin getXBin(int i) {
		return data.getXBin(i);
	}

	@Override
	public double getY(int i) {
		return data.getY(i);
	}

}
