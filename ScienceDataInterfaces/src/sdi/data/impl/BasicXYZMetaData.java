package sdi.data.impl;

import sdi.data.Units;
import sdi.data.XYZMetadata;

public class BasicXYZMetaData implements XYZMetadata {
	
	public static BasicXYZMetaData of(String name, Units xUnits, Units yUnits, Units zUnits, String xName, String yName, String zName,
			String xLabel, String yLabel, String zLabel) {
		return new BasicXYZMetaData(name, xUnits, yUnits, zUnits, xName, yName, zName,
				xLabel, yLabel, zLabel);
	}
	
	private final String name;
	private final Units xUnits;
	private final Units yUnits;
	private final Units zUnits;
	private final String xName;
	private final String yName;
	private final String zName;
	private final String xLabel;
	private final String yLabel;
	private final String zLabel;

	private BasicXYZMetaData(String name, Units xUnits, Units yUnits, Units zUnits, String xName, String yName, String zName,
			String xLabel, String yLabel, String zLabel) {
		super();
		this.name = convertEscapeSequences(name);
		this.xUnits = xUnits;
		this.yUnits = yUnits;
		this.zUnits = zUnits;
		this.xName = convertEscapeSequences(xName);
		this.yName = convertEscapeSequences(yName);
		this.zName = convertEscapeSequences(zName);
		this.xLabel = convertEscapeSequences(xLabel);
		this.yLabel = convertEscapeSequences(yLabel);
		this.zLabel = convertEscapeSequences(zLabel);
	}

	private static String convertEscapeSequences(String string) {
		String[] subString = string.split("\n");
		StringBuilder builder = new StringBuilder();
		String separator = null;
		for (String each: subString) {
			if (separator == null) {
				separator = "!C";
			} else {
				builder.append(separator);
			}
			builder.append(each);
		}
		return builder.toString();
	}

	@Override
	public Units getXUnits() {
		return xUnits;
	}

	@Override
	public Units getYUnits() {
		return yUnits;
	}

	@Override
	public String getXName() {
		return xName;
	}

	@Override
	public String getYName() {
		return yName;
	}

	@Override
	public String getXLabel() {
		return xLabel;
	}

	@Override
	public String getYLabel() {
		return yLabel;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Units getZUnits() {
		return zUnits;
	}

	@Override
	public String getZName() {
		return zName;
	}

	@Override
	public String getZLabel() {
		return zLabel;
	}
}