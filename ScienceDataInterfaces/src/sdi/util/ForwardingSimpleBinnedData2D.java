package sdi.util;

import sdi.data.Bin;
import sdi.data.SimpleBinnedData2D;

public class ForwardingSimpleBinnedData2D implements SimpleBinnedData2D {
	private final SimpleBinnedData2D data;

	protected ForwardingSimpleBinnedData2D(SimpleBinnedData2D data) {
		this.data = data;
		
	}

	protected SimpleBinnedData2D getData() {
		return data;
	}

	@Override
	public int sizeX() {
		return data.sizeX();
	}

	@Override
	public Bin getXBin(int i) {
		return data.getXBin(i);
	}
	
	@Override
	public int sizeY() {
		return data.sizeY();
	}
	
	@Override
	public Bin getYBin(int j) {
		return data.getYBin(j);
	}
	
	@Override
	public double getZ(int i, int j) {
		return data.getZ(i, j);
	}
}
