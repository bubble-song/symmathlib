package complex;

import sym.symmathlib.complex.CFunction;
import sym.symmathlib.complex.CInterpolator;
import sym.symmathlib.complex.CTransform;
import sym.symmathlib.vector.VecTool;
import sym.symplotlib.Plot;

public class ComplexTest2
{
	public static void main(String[] args)
	{
		int POINT_COUNT1 = 10000;
		int POINT_COUNT2 = 10000;
		double F_1 = 10;
		double SIGMA_1 = 20;
		
		double[] xs1 = VecTool.linspace(-100, 100, new double[POINT_COUNT1]);
		double[] ys1R = new double[POINT_COUNT1];
		double[] ys1I = new double[POINT_COUNT1];
		
		VecTool.Func1 func1R = (x) ->
		{
			double f1 = Math.cos(F_1 * x);
			double f2 = -x * x / (2 * SIGMA_1 * SIGMA_1);
			double f3 = f1 * Math.exp(f2);
			return f3;
		};
		VecTool.Func1 func1I = (x) ->
		{
			double f1 = -Math.sin(F_1 * x);
			double f2 = -x * x / (2 * SIGMA_1 * SIGMA_1);
			double f3 = f1 * Math.exp(f2);
			return f3;
		};
		
		
		VecTool.trans(func1R, xs1, ys1R);
		VecTool.trans(func1R, xs1, ys1I);
		CFunction cfunc1 = CInterpolator.getFunctionLinear(xs1, ys1R, ys1I);
		
		double[] xs2 = VecTool.linspace(-100, 100, new double[POINT_COUNT2]);
		CFunction cfunc2 = CTransform.fourier(cfunc1, xs2);
//		CFunction cfunc2 = CTransform.fourierParallel(cfunc1, xs2, 16);
//		CFunction cfunc2 = CTransform.fourierInverse(cfunc1, xs2);
		
		double[] xs3 = VecTool.linspace(-100, 100, new double[POINT_COUNT1]);
//		CFunction cfunc3 = CTransform.fourier(cfunc1, xs3);
//		CFunction cfunc3 = CTransform.fourierInverse(cfunc1, xs3);
		
		double[] pxs = VecTool.linspace(-100, 100, new double[100000]);
		
		Plot plot = new Plot();
		plot.plot(pxs, VecTool.trans(cfunc1::getR, pxs), "style=-;");
		plot.plot(pxs, VecTool.trans(cfunc2::getR, pxs), "style=-;");
//		plot.plot(pxs, VecTool.trans(cfunc2::getR, pxs), "style=-;");
//		plot.plot(pxs, VecTool.trans(cfunc2::getI, pxs), "style=-;");
		plot.autoRange();
		plot.show();
	}
}
