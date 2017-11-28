package sdi.data.impl;

import sdi.data.Units;
import sdi.data.XYMetadata;

public class BasicXYMetaData implements XYMetadata {

	public static BasicXYMetaData of(String name, Units xUnits, Units yUnits, String xName, String yName, String xLabel, String yLabel) {
		return new BasicXYMetaData(name, xUnits, yUnits, xName, yName, xLabel, yLabel);
	}
	
	private final String name;
	private final Units xUnits;
	private final Units yUnits;
	private final String xName;
	private final String yName;
	private final String xLabel;
	private final String yLabel;

	private BasicXYMetaData(String name, Units xUnits, Units yUnits, String xName, String yName, String xLabel, String yLabel) {
		super();
		this.name = convertEscapeSequences(name);
		this.xUnits = xUnits;
		this.yUnits = yUnits;
		this.xName = convertEscapeSequences(xName);
		this.yName = convertEscapeSequences(yName);
		this.xLabel = convertEscapeSequences(xLabel);
		this.yLabel = convertEscapeSequences(yLabel);
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
}