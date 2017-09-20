package sdi.util;

import java.util.Optional;

import sdi.data.Bin;
import sdi.data.BinnedData1D;
import sdi.data.FillDetector;
import sdi.data.UncertaintyProvider;
import sdi.data.XYMetadata;

public class ForwardingBinnedData1D implements BinnedData1D {
	private final BinnedData1D data;

	protected ForwardingBinnedData1D(BinnedData1D data) {
		this.data = data;
		
	}

	protected BinnedData1D getData() {
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

	@Override
	public Optional<FillDetector> getFillDetector() {
		return data.getFillDetector();
	}

	@Override
	public Optional<UncertaintyProvider> getYUncertProvider() {
		return data.getYUncertProvider();
	}

	@Override
	public XYMetadata getMetadata() {
		return data.getMetadata();
	}
}
