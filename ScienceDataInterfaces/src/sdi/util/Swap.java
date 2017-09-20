package sdi.util;

import java.util.Optional;

import sdi.data.Bin;
import sdi.data.BinnedData2D;
import sdi.data.FillDetector2D;
import sdi.data.SimpleBinnedData2D;
import sdi.data.UncertaintyProvider2D;
import sdi.data.Units;
import sdi.data.XYZMetadata;

public class Swap {
	public static SimpleBinnedData2D swapXY(final SimpleBinnedData2D data) {
		return new SwappedSimpleBinnedData2D(data);
	}

	public static BinnedData2D swapXY(final BinnedData2D data) {
		return new SwappedBinnedData2D(data);
	}

	static class SwappedSimpleBinnedData2D implements SimpleBinnedData2D {
		private final SimpleBinnedData2D data;

		SwappedSimpleBinnedData2D(SimpleBinnedData2D data) {
			this.data = data;
		}

		@Override
		public int sizeX() {
			return data.sizeY();
		}

		@Override
		public Bin getXBin(int i) {
			return data.getYBin(i);
		}

		@Override
		public int sizeY() {
			return data.sizeX();
		}

		@Override
		public Bin getYBin(int j) {
			return data.getXBin(j);
		}

		@Override
		public double getZ(int i, int j) {
			return data.getZ(j, i);
		}

		@Override
		public String toString() {
			return data.toString() + " (X <-> Y)";
		}
	};

	static class SwappedBinnedData2D implements BinnedData2D {

		private final BinnedData2D data;

		SwappedBinnedData2D(BinnedData2D data) {
			this.data = data;
		}

		@Override
		public int sizeX() {
			return data.sizeY();
		}

		@Override
		public Bin getXBin(int i) {
			return data.getYBin(i);
		}

		@Override
		public int sizeY() {
			return data.sizeX();
		}

		@Override
		public Bin getYBin(int j) {
			return data.getXBin(j);
		}

		@Override
		public double getZ(int i, int j) {
			return data.getZ(j, i);
		}

		@Override
		public Optional<FillDetector2D> getFillDetector() {
			Optional<FillDetector2D> optFillDetector = data.getFillDetector();
			if (optFillDetector.isPresent()) {
				final FillDetector2D fillDetector = optFillDetector.get();
				return Optional.of(new FillDetector2D() {

					@Override
					public boolean isFill(int i, int j) {
						return fillDetector.isFill(j, i);
					}
					
				});
			}
			return optFillDetector;
		}

		@Override
		public Optional<UncertaintyProvider2D> getZUncertProvider() {
			Optional<UncertaintyProvider2D> optProvider = data.getZUncertProvider();
			if (optProvider.isPresent()) {
				final UncertaintyProvider2D provider = optProvider.get();
				return Optional.of(new UncertaintyProvider2D() {

					@Override
					public double getUncertPlus(int i, int j) {
						return provider.getUncertPlus(j, i);
					}

					@Override
					public double getUncertMinus(int i, int j) {
						return provider.getUncertMinus(j, i);
					}
				});
			}
			return optProvider;
		}

		@Override
		public XYZMetadata getMetadata() {
			final XYZMetadata origMetadata = data.getMetadata();
			return new XYZMetadata() {

				@Override
				public Units getXUnits() {
					return origMetadata.getYUnits();
				}

				@Override
				public Units getYUnits() {
					return origMetadata.getXUnits();
				}

				@Override
				public String getXName() {
					return origMetadata.getYName();
				}

				@Override
				public String getYName() {
					return origMetadata.getXName();
				}

				@Override
				public String getXLabel() {
					return origMetadata.getYLabel();
				}

				@Override
				public String getYLabel() {
					return origMetadata.getXLabel();
				}

				@Override
				public String getName() {
					return origMetadata.getName();
				}

				@Override
				public Units getZUnits() {
					return origMetadata.getZUnits();
				}

				@Override
				public String getZName() {
					return origMetadata.getZName();
				}

				@Override
				public String getZLabel() {
					return origMetadata.getZLabel();
				}
			};
		}

		@Override
		public String toString() {
			return data.toString() + " (X <-> Y)";
		}
	}
}
