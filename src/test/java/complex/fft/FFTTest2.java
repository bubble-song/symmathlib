package complex.fft;

import sym.symmathlib.vector.VecFourier;
import sym.symmathlib.vector.VecTool;
import sym.symplotlib.Plot;

import static sym.symmathlib.constant.Constant.C_2PI;

public class FFTTest2
{
	public static void main(String[] args)
	{
		int POINT_COUNT1 = 1000;
		int POINT_COUNT2 = 100;
		
		double[] xs1 = VecTool.linspace(-20, 20, new double[POINT_COUNT1]);
		double[] yRs1;
		double[] yIs1;
		double[] ks1 = VecTool.linspace(-20, 20, new double[POINT_COUNT2]);
		
		VecTool.Func1 funcR1 = (x) ->
		{
			double sigma = 1.0;
			double x0 = 5;
			double f1 = +1 / Math.sqrt(C_2PI) / sigma * Math.cos(10 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		VecTool.Func1 funcI1 = (x) ->
		{
			double sigma = 1.0;
			double x0 = 5;
			double f1 = -1 / Math.sqrt(C_2PI) / sigma * Math.sin(10 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		
		yRs1 = VecTool.trans(funcR1, xs1);
		yIs1 = VecTool.trans(funcI1, xs1);
		
		double[][] xyRIs1 = new double[][]{xs1, yRs1, yIs1};
		
		double[][] kyRIs2 = VecFourier.czt(xyRIs1, ks1);
		
		double[][] xyRIs3 = VecFourier.cztInverse(kyRIs2, xs1);
		
		Plot plot = new Plot();
		
		plot.plot(xyRIs1[0], xyRIs1[1], "");
//		plot.plot(xyRIs1[0], xyRIs1[2], "");
//		plot.plot(kyRIs2[0], kyRIs2[1], "");
//		plot.plot(kyRIs2[0], kyRIs2[2], "");
		plot.plot(xyRIs3[0], xyRIs3[1], "");
//		plot.plot(xyRIs3[0], xyRIs3[2], "");
		plot.autoRange();
		plot.show();
	}
}
