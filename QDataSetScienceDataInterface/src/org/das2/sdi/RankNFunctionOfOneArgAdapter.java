package org.das2.sdi;

import java.awt.Color;

import org.das2.datum.Units;
import org.das2.qds.AbstractQFunction;
import org.das2.qds.BundleDataSet;
import org.das2.qds.DRank0DataSet;
import org.das2.qds.DataSetUtil;
import org.das2.qds.QDataSet;
import org.das2.qds.QFunction;
import org.das2.qds.ops.Ops;

import sdi.data.XYMetadata;
import sdi.functions.ScalarFunctionOfOneArg;

public class RankNFunctionOfOneArgAdapter {
	private static final Boolean LOCK_OBJECT = new Boolean(false);

	public static Boolean getLockObject() {
		return LOCK_OBJECT;
	}

	public static QFunction adapt(Iterable<? extends ScalarFunctionOfOneArg> funcsOfOneArg, QDataSet exampleInput) {
		if (funcsOfOneArg == null) throw new NullPointerException();
		if (!funcsOfOneArg.iterator().hasNext()) throw new IllegalArgumentException();
		return new AbstractQFunction() {

			@Override
			public QDataSet value(QDataSet parm) {
				int rank = parm.rank();
				if (rank > 1) throw new IllegalArgumentException("Arguments to QFunction.value must have rank 0 or 1");
				QDataSet arg = rank == 0 ? parm : parm.slice(0);
		        BundleDataSet outbds= BundleDataSet.createRank0Bundle();
		        synchronized (getLockObject()) {
		        	for (ScalarFunctionOfOneArg each: funcsOfOneArg) {
		        		XYMetadata md = each.get();
		        		Units yUnits = getUnits(md.getYUnits());
		        		QDataSet converted = Ops.convertUnitsTo(arg, getUnits(md.getXUnits()));
		        		DRank0DataSet result = DataSetUtil.asDataSet(each.evaluate(converted.value()), yUnits);	
		        		result.putProperty(QDataSet.NAME, md.getYName());
		        		result.putProperty(QDataSet.LABEL, md.getYLabel());
		        		result.putProperty(QDataSet.UNITS, yUnits);
		        		result.putProperty(QDataSet.FORMAT, "%4.2f");
		        		result.putProperty("Suggested Color", Color.BLACK);
		        		
		        		outbds.bundle(result);
		        	}
		        }
		        return outbds;
			}

			@Override
			public QDataSet exampleInput() {
		        BundleDataSet inbds= BundleDataSet.createRank0Bundle();
		        inbds.bundle(exampleInput);
		        return inbds;
			}
		};
	}

	public static Units getUnits(sdi.data.Units name) {
		try {
	        return Units.getByName(name.getName().trim());
		} catch (@SuppressWarnings("unused") Exception e) {
			return Units.dimensionless;
		}
	}
}
