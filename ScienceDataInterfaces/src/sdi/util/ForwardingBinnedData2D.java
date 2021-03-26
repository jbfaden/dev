package sdi.util;

import java.util.Optional;

import sdi.data.Bin;
import sdi.data.BinnedData2D;
import sdi.data.FillDetector2D;
import sdi.data.UncertaintyProvider2D;
import sdi.data.XYZMetadata;

public class ForwardingBinnedData2D implements BinnedData2D {
	private final BinnedData2D data;

	protected ForwardingBinnedData2D(BinnedData2D data) {
		this.data = data;
		
	}

	protected BinnedData2D getData() {
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
		return data.getZ(i,  j);
	}

	@Override
	public Optional<FillDetector2D> getFillDetector() {
		return data.getFillDetector();
	}

	@Override
	public Optional<UncertaintyProvider2D> getZUncertProvider() {
		return data.getZUncertProvider();
	}

	@Override
	public XYZMetadata getMetadata() {
		return data.getMetadata();
	}

	protected BinnedData2D getInputData() {
		return data;
	}
}
