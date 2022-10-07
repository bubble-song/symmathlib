package complex.fft;

import sym.symmathlib.function.Function;
import sym.symmathlib.function.Interpolator;
import sym.symmathlib.function.Transform;
import sym.symmathlib.vector.VecTool;
import sym.symplotlib.Plot;

import static sym.symmathlib.constant.Constant.C_2PI;

public class FFTTest4
{
	public static void main(String[] args)
	{
		//		double[] vec1 = new double[101];
		//		double[] vec2 = new double[101];
		//
		//		for(int i = 0; i < 30; i++)
		//		{
		//			vec1[i] = Math.sin(0.1 * i);
		//			vec2[i] = 1;
		//		}
		//
		//		double[] vec3 = VecTransform.vecConvolution(vec1, vec2);
		//
		//		Plot plot = new Plot();
		//		double[] x1 = VecTool.linspace(0, 100, new double[101]);
		//		double[] x2 = VecTool.linspace(0, 200, new double[201]);
		//
		//		plot.plot(x1, vec1, "style=.;");
		//		plot.plot(x1, vec2, "style=.;");
		//		plot.plot(x2, vec3, "style=.;");
		//		plot.autoRange();
		//		plot.show();
		
		
		int POINT_COUNT1 = 10000;
		
		double[] xs1 = VecTool.linspace(-100, 100, new double[POINT_COUNT1]);
		double[] xs2 = VecTool.linspace(-100, 100, new double[POINT_COUNT1]);
		double[] ys1;
		double[] ys2;
		
		VecTool.Func1 func1 = (x) ->
		{
			double sigma = 10 / Math.sqrt(C_2PI);
			double x0 = 0;
			double f1 = Math.sqrt(2) / Math.sqrt(C_2PI) / sigma * Math.cos(0 * x);
			double f2 = -0.5 * Math.pow((x - x0) / sigma, 2);
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		
		ys1 = VecTool.trans(func1, xs1);
		ys2 = VecTool.trans(func1, xs1);
		Function rfunc1 = Interpolator.getFunctionLinear(xs1, ys1);
		Function rfunc2 = Interpolator.getFunctionLinear(xs1, ys2);
		Function rfunc3 = Transform.convolution(rfunc1, rfunc2, POINT_COUNT1);
		
		Plot plot = new Plot();
		double[] px1 = VecTool.linspace(-100, 100, new double[10001]);
		
		plot.plot(px1, VecTool.trans(rfunc1::get, px1), "style=-;");
		plot.plot(px1, VecTool.trans(rfunc2::get, px1), "style=-;");
		plot.plot(px1, VecTool.trans(rfunc3::get, px1), "style=-;");
		plot.autoRange();
		plot.show();
	}
}
