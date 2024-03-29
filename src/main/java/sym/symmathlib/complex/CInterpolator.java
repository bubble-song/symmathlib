package sym.symmathlib.complex;

import sym.symmathlib.function.Function;
import sym.symmathlib.vector.VecTool;

public class CInterpolator
{
	public static CFunction getFunctionAnalytic(VecTool.Func1 func1R, VecTool.Func1 func1I)
	{
		return new CFunctionAnalytical(func1R, func1I);
	}
	
	public static CFunction getFunctionLinear(double[] xs, double[] ysR, double[] ysI)
	{
		return new CFunctionLinear(xs, ysR, ysI);
	}
	
	public static CFunction getFunctionLinear(Function rfunc1)
	{
		double[][] xyRs = rfunc1.getData();
		double[] yIs = new double[xyRs[0].length];
		return new CFunctionLinear(xyRs[0], xyRs[1], yIs);
	}
}
