package sdi.util;

import java.util.Optional;

import sdi.data.Bin;
import sdi.data.BinnedData1D;
import sdi.data.BinnedData2D;
import sdi.data.FillDetector;
import sdi.data.FillDetector2D;
import sdi.data.Metadata;
import sdi.data.SimpleBinnedData1D;
import sdi.data.SimpleBinnedData2D;
import sdi.data.UncertaintyProvider;
import sdi.data.UncertaintyProvider2D;
import sdi.data.Units;
import sdi.data.XYMetadata;
import sdi.data.XYZMetadata;

public class Slice {
	// Methods to slice SimpleBinnedData2D //////////////////////////////////////////////
	public static SimpleBinnedData1D getXSliceAtY(SimpleBinnedData2D data, int yIndex) {
		return new SimpleBinnedData1D() {
			@Override
			public int size() {
 				return data.sizeX();
			}

			@Override
			public Bin getXBin(int i) {
				return data.getXBin(i);
			}

			@Override
			public double getY(int i) {
				return data.getZ(i, yIndex);
			}
		};
	}

	public static SimpleBinnedData1D getYSliceAtX(SimpleBinnedData2D data, int xIndex) {
		return new SimpleBinnedData1D() {
			@Override
			public int size() {
				return data.sizeY();
			}
			
			@Override
			public Bin getXBin(int i) {
				return data.getYBin(i);
			}
			
			@Override
			public double getY(int i) {
				return data.getZ(xIndex, i);
			}
		};
	}
	
	public static SimpleBinnedData1D getSlice(Dimension outputDimension, SimpleBinnedData2D data, int atIndex) {
		switch (outputDimension) {
		case X:
			return getXSliceAtY(data, atIndex);
		case Y:
			return getYSliceAtX(data, atIndex);
			default:
				throw new AssertionError();
		}
	}

	// Methods to slice BinnedData2D //////////////////////////////////////////////
	public static BinnedData1D getXSliceAtY(BinnedData2D data, int yIndex) {
		return new BinnedData1D() {
			@Override
			public int size() {
				return data.sizeX();
			}
			
			@Override
			public Bin getXBin(int i) {
				return data.getXBin(i);
			}
			
			@Override
			public double getY(int i) {
				return data.getZ(i, yIndex);
			}
			
			@Override
			public Optional<FillDetector> getFillDetector() {
				return getXFillAtY(data.getFillDetector(), yIndex);
			}
			
			@Override
			public Optional<UncertaintyProvider> getYUncertProvider() {
				return getXUncertaintyAtY(data.getZUncertProvider(), yIndex);
			}
			
			@Override
			public XYMetadata getMetadata() {
				return getXZMetadata(data.getMetadata());
			}
		};
	}
	
	public static BinnedData1D getYSliceAtX(BinnedData2D data, int xIndex) {
		return new BinnedData1D() {
			@Override
			public int size() {
 				return data.sizeY();
			}

			@Override
			public Bin getXBin(int i) {
				return data.getYBin(i);
			}

			@Override
			public double getY(int i) {
				return data.getZ(xIndex, i);
			}

			@Override
			public Optional<FillDetector> getFillDetector() {
				return getYFillAtX(data.getFillDetector(), xIndex);
			}

			@Override
			public Optional<UncertaintyProvider> getYUncertProvider() {
				return getYUncertaintyAtX(data.getZUncertProvider(), xIndex);
			}

			@Override
			public XYMetadata getMetadata() {
				return getYZMetadata(data.getMetadata());
			}
		};
	}

	public static SimpleBinnedData1D getSlice(Dimension outputDimension, BinnedData2D data, int atIndex) {
		switch (outputDimension) {
		case X:
			return getYSliceAtX(data, atIndex);
		case Y:
			return getXSliceAtY(data, atIndex);
			default:
				throw new AssertionError();
		}
	}

	// Methods to slice FillDetector2D //////////////////////////////////////////////
	public static Optional<FillDetector> getXFillAtY(Optional<FillDetector2D> optionalDetector, int yIndex) {
		Optional<FillDetector> result;
		if (optionalDetector.isPresent()) {
			final FillDetector2D detector = optionalDetector.get(); 
			result = Optional.of(new FillDetector() {
				@Override
				public boolean isFill(int index) {
					return detector.isFill(index, yIndex);
				}
			});
		} else {
			result = Optional.empty();
		}
		return result;
	}
	
	public static Optional<FillDetector> getYFillAtX(Optional<FillDetector2D> optionalDetector, int xIndex) {
		Optional<FillDetector> result;
		if (optionalDetector.isPresent()) {
			final FillDetector2D detector = optionalDetector.get(); 
			result = Optional.of(new FillDetector() {
				@Override
				public boolean isFill(int index) {
					return detector.isFill(xIndex, index);
				}
			});
		} else {
			result = Optional.empty();
		}
		return result;
	}

	public static Optional<FillDetector> getFill(Dimension outputDimension, Optional<FillDetector2D> optionalDetector, int atIndex) {
		switch (outputDimension) {
		case X:
			return getXFillAtY(optionalDetector, atIndex);
		case Y:
			return getYFillAtX(optionalDetector, atIndex);
			default:
				throw new AssertionError();
		}
	}

	// Methods to slice UncertaintyProvider2D //////////////////////////////////////////////
	public static Optional<UncertaintyProvider> getXUncertaintyAtY(Optional<UncertaintyProvider2D> optionalProvider, int yIndex) {
		Optional<UncertaintyProvider> result;
		if (optionalProvider.isPresent()) {
			final UncertaintyProvider2D provider = optionalProvider.get(); 
			result = Optional.of(new UncertaintyProvider() {
				
				@Override
				public double getUncertPlus(int i) {
					return provider.getUncertPlus(i, yIndex);
				}
				
				@Override
				public double getUncertMinus(int i) {
					return provider.getUncertMinus(i, yIndex);
				}
			});
		} else {
			result = Optional.empty();
		}
		return result;
	}
	
	public static Optional<UncertaintyProvider> getYUncertaintyAtX(Optional<UncertaintyProvider2D> optionalProvider, int xIndex) {
		Optional<UncertaintyProvider> result;
		if (optionalProvider.isPresent()) {
			final UncertaintyProvider2D provider = optionalProvider.get(); 
			result = Optional.of(new UncertaintyProvider() {

				@Override
				public double getUncertPlus(int i) {
					return provider.getUncertPlus(xIndex, i);
				}

				@Override
				public double getUncertMinus(int i) {
					return provider.getUncertMinus(xIndex, i);
				}
			});
		} else {
			result = Optional.empty();
		}
		return result;
	}

	public static Optional<UncertaintyProvider> getUncertainty(Dimension outputDimension, Optional<UncertaintyProvider2D> optionalProvider, int atIndex) {
		switch (outputDimension) {
		case X:
			return getXUncertaintyAtY(optionalProvider, atIndex);
		case Y:
			return getYUncertaintyAtX(optionalProvider, atIndex);
			default:
				throw new AssertionError();
		}
	}

	// Methods to slice XYMetadata/XYZMetadata //////////////////////////////////////////////
	public static Metadata getXMetadata(XYMetadata metadata) {
		return new Metadata() {
			@Override
			public Units getUnits() {
				return metadata.getXUnits();
			}

			@Override
			public String getName() {
				return metadata.getXName();
			}

			@Override
			public String getLabel() {
				return metadata.getXLabel();
			}
		};
	}

	public static Metadata getYMetadata(XYMetadata metadata) {
		return new Metadata() {
			@Override
			public Units getUnits() {
				return metadata.getYUnits();
			}

			@Override
			public String getName() {
				return metadata.getYName();
			}

			@Override
			public String getLabel() {
				return metadata.getYLabel();
			}
		};
	}

	public static Metadata getZMetadata(XYZMetadata metadata) {
		return new Metadata() {
			@Override
			public Units getUnits() {
				return metadata.getZUnits();
			}

			@Override
			public String getName() {
				return metadata.getZName();
			}

			@Override
			public String getLabel() {
				return metadata.getZLabel();
			}
		};
	}

	public static XYMetadata getXZMetadata(XYZMetadata metadata) {
		return new XYMetadata() {
			
			@Override
			public String getName() {
				return metadata.getName();
			}
			
			@Override
			public Units getXUnits() {
				return metadata.getXUnits();
			}
			
			@Override
			public Units getYUnits() {
				return metadata.getZUnits();
			}
			
			@Override
			public String getXName() {
				return metadata.getXName();
			}
			
			@Override
			public String getYName() {
				return metadata.getZName();
			}
			
			@Override
			public String getXLabel() {
				return metadata.getXLabel();
			}
			
			@Override
			public String getYLabel() {
				return metadata.getZLabel();
			}
		};
	}
	
	public static XYMetadata getYZMetadata(XYZMetadata metadata) {
		return new XYMetadata() {
			@Override
			public String getName() {
				return metadata.getName();
			}

			@Override
			public Units getXUnits() {
				return metadata.getYUnits();
			}

			@Override
			public Units getYUnits() {
				return metadata.getZUnits();
			}

			@Override
			public String getXName() {
				return metadata.getYName();
			}

			@Override
			public String getYName() {
				return metadata.getZName();
			}

			@Override
			public String getXLabel() {
				return metadata.getYLabel();
			}

			@Override
			public String getYLabel() {
				return metadata.getZLabel();
			}
		};
	}

	public static XYMetadata getZMetadata(Dimension dimension, XYZMetadata metadata) {
		switch (dimension) {
		case X:
			return getYZMetadata(metadata);
		case Y:
			return getXZMetadata(metadata);
			default:
				throw new AssertionError();
		}
	}

	private Slice() {
		throw new AssertionError();
	}
}
