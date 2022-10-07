package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;
import sym.symmathlib.vector.VecTransform;

import static sym.symmathlib.constant.Constant.C_SQRT_2PI;

public class Transform
{
	public static Function convolution(Function func1, Function func2, int pointCount)
	{
		double[] range1 = func1.getRange();
		double[] range2 = func2.getRange();
		double[] range3 = new double[]{range1[0] + range2[0], range1[1] + range2[1]};
		
		int L = pointCount;
		int M = (int) Math.round(pointCount * (range1[1] - range1[0]) / (range3[1] - range3[0]));
		int N = L - M + 1;
		double dx = (range3[1] - range3[0]) / (L - 1);
		
		double[] vec1 = new double[M];
		double[] vec2 = new double[N];
		for(int i = 0; i < M; i++)
		{
			vec1[i] = func1.get(range1[0] + i * dx);
		}
		for(int i = 0; i < N; i++)
		{
			vec2[i] = func2.get(range2[0] + i * dx);
		}
		
		double[] vec3 = VecTransform.vecConvolution(vec1, vec2);
		for(int i = 0; i < L; i++)
		{
			vec3[i] *= dx;
		}
		double[] xs = VecTool.linspace(range3[0], range3[1], new double[L]);
		return Interpolator.getFunctionLinear(xs, vec3);
	}
	
	//convolution of a gaussian kernel
	public static Function gaussianSmooth(Function rfunc1, double sigma)
	{
		double[] range = rfunc1.getRange();
		int length = rfunc1.getData()[0].length;
		double dx = (range[1] - range[0]) / (length - 1);
		int kernelLength = (int) Math.round(2 * 4 * sigma / dx) + 1;
		VecTool.Func1 kernel = (x) ->
		{
			double f1 = 1 / C_SQRT_2PI / sigma;
			double f2 = x / sigma;
			return f1 * Math.exp(-0.5 * f2 * f2);
		};
		double[] xs = VecTool.linspace(-4 * sigma, 4 * sigma, new double[kernelLength]);
		double[] ys = VecTool.trans(kernel, xs);
		Function rfunc2 = Interpolator.getFunctionLinear(xs, ys);
		return convolution(rfunc1, rfunc2, length);
	}
}
