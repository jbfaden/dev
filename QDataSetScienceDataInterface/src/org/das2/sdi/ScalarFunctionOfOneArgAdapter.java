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
public class ScalarFunctionOfOneArgAdapter {
	public static QFunction adapt(ScalarFunctionOfOneArg funcOfOneArg) {
		return new AbstractQFunction() {

			@Override
			public QDataSet value(QDataSet parm) {
				int rank = parm.rank();
				if (rank > 1) throw new IllegalArgumentException("Arguments to QFunction.value must have rank 0 or 1");
				QDataSet arg = rank == 0 ? parm : parm.slice(0);
		        BundleDataSet outbds= BundleDataSet.createRank0Bundle();
		        XYMetadata md = funcOfOneArg.get();
		        Units yUnits = getUnits(md.getYUnits());
		        synchronized (this) {
		        	QDataSet converted = Ops.convertUnitsTo(arg, getUnits(md.getXUnits()));
		        	DRank0DataSet result = DataSetUtil.asDataSet(funcOfOneArg.evaluate(converted.value()), yUnits);	
		        	result.putProperty(QDataSet.NAME, md.getYName());
		        	result.putProperty(QDataSet.LABEL, md.getYLabel());
		        	result.putProperty(QDataSet.UNITS, yUnits);
		        	result.putProperty(QDataSet.FORMAT, "%4.2f");
		        	result.putProperty("Suggested Color", Color.BLACK);
		        	
		        	outbds.bundle(result);
		        }
		        return outbds;
			}

			@Override
			public QDataSet exampleInput() {
		        BundleDataSet inbds= BundleDataSet.createRank0Bundle();
		        DRank0DataSet dd= DataSetUtil.asDataSet(10. / 3., Units.t2000);
		        dd.putProperty(QDataSet.LABEL, "Time") ;
		        inbds.bundle( dd );
		        return inbds;
			}

			@Override
			public QDataSet exampleOutput() {
		        BundleDataSet inbds= BundleDataSet.createRank0Bundle();
		        DRank0DataSet dd= DataSetUtil.asDataSet(10. / 3., Units.dimensionless);
		        dd.putProperty(QDataSet.LABEL, funcOfOneArg.get().getYLabel()) ;
		        inbds.bundle( dd );
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
