package complex;

import sym.symmathlib.complex.CFunction;
import sym.symmathlib.complex.CInterpolator;
import sym.symmathlib.complex.CTransform;
import sym.symmathlib.vector.VecTool;
import sym.symplotlib.Plot;

import static sym.symmathlib.constant.Constant.C_2PI;

//test for symmathlib complex function
public class ComplexTest1
{
	public static void main(String[] args)
	{
		int POINT_COUNT1 = 10000;
		int POINT_COUNT2 = 1000;
		
		double[] xs1 = VecTool.linspace(-100, 100, new double[POINT_COUNT1]);
		double[] ys1R = new double[POINT_COUNT1];
		double[] ys1I = new double[POINT_COUNT1];
		
		VecTool.Func1 func1 = (x) ->
		{
			double sigma = 1;
			double x0 = 5;
			double f1 = 1 / Math.sqrt(C_2PI) / sigma * Math.cos(0 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		
		VecTool.trans(func1, xs1, ys1R);
		CFunction cfunc1 = CInterpolator.getFunctionLinear(xs1, ys1R, ys1I);
		
		double[] xs2 = VecTool.linspace(-100, 100, new double[POINT_COUNT2]);
		CFunction cfunc2 = CTransform.fourier(cfunc1, xs2);
		
		double[] pxs = VecTool.linspace(-100, 100, new double[100000]);
		
		Plot plot = new Plot();
		plot.plot(pxs, VecTool.trans(cfunc1::getR, pxs), "style=-;");
		plot.plot(pxs, VecTool.trans(cfunc2::getR, pxs), "style=-;");
		plot.plot(pxs, VecTool.trans(cfunc2::getI, pxs), "style=-;");
		plot.autoRange();
		plot.show();
	}
}
