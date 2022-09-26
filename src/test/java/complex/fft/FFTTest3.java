package complex.fft;

import sym.symmathlib.vector.VecFourier;
import sym.symmathlib.vector.VecTool;
import sym.symplotlib.Plot;

import static sym.symmathlib.constant.Constant.C_2PI;
import static sym.symmathlib.constant.Constant.C_PI;

public class FFTTest3
{
	public static void main(String[] args)
	{
		int POINT_COUNT1 = (2 << 6);
		
		double[] xs1 = VecTool.linspace(-10.24, 10.24, new double[POINT_COUNT1]);
		
		double XD = xs1[1] - xs1[0];
		
		VecTool.Func1 funcR1 = (x) ->
		{
			double sigma = 1 / Math.sqrt(C_2PI);
			double x0 = -0;
			double f1 = +1 / Math.sqrt(C_2PI) / sigma * Math.cos(0 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		VecTool.Func1 funcI1 = (x) ->
		{
			double sigma = 1 / Math.sqrt(C_2PI);
			double x0 = -0;
			double f1 = -0 / Math.sqrt(C_2PI) / sigma * Math.sin(0 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		
		VecTool.Func1 funcR2 = (x) ->
		{
			double sigma = 1 / Math.sqrt(C_2PI);
			double x0 = -0;
			double f1 = +1 / Math.sqrt(C_2PI) / sigma * Math.cos(0 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		VecTool.Func1 funcI2 = (x) ->
		{
			double sigma = 1 / Math.sqrt(C_2PI);
			double x0 = -0;
			double f1 = -0 / Math.sqrt(C_2PI) / sigma * Math.sin(0 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		
		
		double[][] yRIs1 = new double[2][];
		double[][] yRIs2 = new double[2][];
		yRIs1[0] = VecTool.trans(funcR1, xs1);
		yRIs1[1] = VecTool.trans(funcI1, xs1);
		
		yRIs2[0] = VecTool.trans(funcR2, xs1);
		yRIs2[1] = VecTool.trans(funcI2, xs1);
		
		double[][] kRIs1 = VecFourier.fft(yRIs1);
		double[][] kRIs2 = VecFourier.fft(yRIs2);
		
		double[][] kRIs3 = new double[2][POINT_COUNT1];
		double mul = XD * Math.sqrt(2 * POINT_COUNT1);
		for(int i = 0; i < POINT_COUNT1; i++)
		{
			kRIs3[0][i] = (+kRIs1[0][i] * kRIs2[0][i] - kRIs1[1][i] * kRIs2[1][i]) * mul;
			kRIs3[1][i] = (+kRIs1[0][i] * kRIs2[1][i] + kRIs1[1][i] * kRIs2[0][i]) * mul;
		}
		
		double[][] yRIs3 = VecFourier.fftInverse(kRIs3);
		
		Plot plot = new Plot();
		
		plot.plot(xs1, yRIs1[0], "style=-;");
		plot.plot(xs1, kRIs1[0], "style=-;");
		plot.plot(xs1, yRIs2[0], "style=-;");
		plot.plot(xs1, yRIs3[0], "style=-;");
		plot.autoRange();
		plot.show();
	}
}
