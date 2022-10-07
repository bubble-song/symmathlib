package sym.symmathlib.complex;

import sym.symmathlib.function.Integrator;
import sym.symmathlib.vector.VecFourier;
import sym.symmathlib.vector.VecTool;
import sym.symmathlib.vector.VecTransform;

import static sym.symmathlib.constant.Constant.C_2PI;
import static sym.symmathlib.constant.Constant.C_SQRT_2PI;

public class CTransform
{
	//perform complex fourier transform
	//kernel: e^ikx
	public static CFunction fourier(CFunction input, double[] _xs)
	{
		int pieceCount = input.getData()[0].length;
		int length = _xs.length;
		double[] xs = VecTool.copy(_xs);
		double[] ysR = new double[length];
		double[] ysI = new double[length];
		
		double[] range = input.getRange();
		for(int i = 0; i < length; i++)
		{
			double _k = xs[i];
			VecTool.Func1 func1R = (_x) ->
			{
				double c = Math.cos(_x * _k);
				double s = Math.sin(_x * _k);
				return c * input.getR(_x) - s * input.getI(_x);
			};
			VecTool.Func1 func1I = (_x) ->
			{
				double c = Math.cos(_x * _k);
				double s = Math.sin(_x * _k);
				return c * input.getI(_x) + s * input.getR(_x);
			};
			ysR[i] = Integrator.integral(func1R, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
			ysI[i] = Integrator.integral(func1I, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
		}
		
		return CInterpolator.getFunctionLinear(xs, ysR, ysI);
	}
	
	//perform complex inverse fourier transform
	//kernel: e^-ikx
	public static CFunction fourierInverse(CFunction input, double[] _xs)
	{
		int pieceCount = input.getData()[0].length;
		int length = _xs.length;
		double[] xs = VecTool.copy(_xs);
		double[] ysR = new double[length];
		double[] ysI = new double[length];
		
		double[] range = input.getRange();
		for(int i = 0; i < length; i++)
		{
			double _x = xs[i];
			VecTool.Func1 func1R = (_k) ->
			{
				double c = Math.cos(_x * _k);
				double s = Math.sin(_x * _k);
				return c * input.getR(_k) + s * input.getI(_k);
			};
			VecTool.Func1 func1I = (_k) ->
			{
				double c = Math.cos(_x * _k);
				double s = Math.sin(_x * _k);
				return c * input.getI(_k) - s * input.getR(_k);
			};
			ysR[i] = Integrator.integral(func1R, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
			ysI[i] = Integrator.integral(func1I, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
		}
		
		return CInterpolator.getFunctionLinear(xs, ysR, ysI);
	}
	
	//accelerated fourier transform using chirp z-transform
	public static CFunction dft(CFunction input, double[] ks)
	{
		double[][] xyRIs = input.getData();
		double[][] kyRIs = VecFourier.czt(xyRIs, ks);
		return CInterpolator.getFunctionLinear(kyRIs[0], kyRIs[1], kyRIs[2]);
	}
	
	//accelerated fourier inverse transform using chirp z-transform
	public static CFunction dftInverse(CFunction input, double[] xs)
	{
		double[][] kyRIs = input.getData();
		double[][] xyRIs = VecFourier.cztInverse(kyRIs, xs);
		return CInterpolator.getFunctionLinear(xyRIs[0], xyRIs[1], xyRIs[2]);
	}
	
	//add hann window
	public static CFunction addWindow(CFunction signal)
	{
		double[][] xyRIs = signal.getData();
		int length = xyRIs[0].length;
		double omega = C_2PI / length;
		for(int i = 0; i < length; i++)
		{
			double mul = 0.5 * (1 - Math.cos(omega * i));
			xyRIs[1][i] *= mul;
			xyRIs[2][i] *= mul;
		}
		return CInterpolator.getFunctionLinear(xyRIs[0], xyRIs[1], xyRIs[2]);
	}
	
	public static CFunction convolution(CFunction func1, CFunction func2, int pointCount)
	{
		double[] range1 = func1.getRange();
		double[] range2 = func2.getRange();
		double[] range3 = new double[]{range1[0] + range2[0], range1[1] + range2[1]};
		
		int L = pointCount;
		int M = (int) Math.round(pointCount * (range1[1] - range1[0]) / (range3[1] - range3[0]));
		int N = L - M + 1;
		double dx = (range3[1] - range3[0]) / (L - 1);
		
		double[][] yRIs1 = new double[2][M];
		double[][] yRIs2 = new double[2][N];
		for(int i = 0; i < M; i++)
		{
			yRIs1[0][i] = func1.getR(range1[0] + i * dx);
			yRIs1[1][i] = func1.getI(range1[0] + i * dx);
		}
		for(int i = 0; i < N; i++)
		{
			yRIs2[0][i] = func2.getR(range2[0] + i * dx);
			yRIs2[1][i] = func2.getI(range2[0] + i * dx);
		}
		
		double[][] yRIs3 = VecTransform.vecComplexConvolution(yRIs1, yRIs2);
		for(int i = 0; i < L; i++)
		{
			yRIs3[0][i] *= dx;
			yRIs3[1][i] *= dx;
		}
		double[] xs = VecTool.linspace(range3[0], range3[1], new double[L]);
		return CInterpolator.getFunctionLinear(xs, yRIs3[0], yRIs3[1]);
	}
	
	//convolution of a gaussian kernel
	public static CFunction gaussianSmooth(CFunction cfunc1, double sigma)
	{
		double[] range = cfunc1.getRange();
		int length = cfunc1.getData()[0].length;
		double dx = (range[1] - range[0]) / (length - 1);
		int kernelLength = (int) Math.round(2 * 4 * sigma / dx) + 1;
		VecTool.Func1 kernel = (x) ->
		{
			double f1 = 1 / C_SQRT_2PI / sigma;
			double f2 = x / sigma;
			return f1 * Math.exp(-0.5 * f2 * f2);
		};
		double[] xs = VecTool.linspace(-4 * sigma, 4 * sigma, new double[kernelLength]);
		double[] yRs = VecTool.trans(kernel, xs);
		double[] yIs = VecTool.trans((x) -> 0, xs);
		CFunction cfunc2 = CInterpolator.getFunctionLinear(xs, yRs, yIs);
		return convolution(cfunc1, cfunc2, length);
	}
	
	//	public static CFunction fourierParallel(CFunction input, double[] _xs, int threadCount)
	//	{
	//		int pieceCount = input.getData()[0].length;
	//		int length = _xs.length;
	//		double[] xs = VecTool.copy(_xs);
	//		double[] ysR = new double[length];
	//		double[] ysI = new double[length];
	//
	//		double[] range = input.getRange();
	//		VecTool.Func1 func = (x) ->
	//		{
	//			int i = (int)Math.round(x);
	//			double _k = xs[i];
	//			VecTool.Func1 func1R = (_x) ->
	//			{
	//				double c = Math.cos(_x * _k);
	//				double s = Math.sin(_x * _k);
	//				return c * input.binaryGetR(_x) - s * input.binaryGetI(_x);
	//			};
	//			VecTool.Func1 func1I = (_x) ->
	//			{
	//				double c = Math.cos(_x * _k);
	//				double s = Math.sin(_x * _k);
	//				return c * input.binaryGetI(_x) + s * input.binaryGetR(_x);
	//			};
	//			ysR[i] = Integrator.integral(func1R, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
	//			ysI[i] = Integrator.integral(func1I, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
	//			return 0;
	//		};
	//		Parallel.runTask(func, length, threadCount);
	//
	//		return CInterpolator.getFunctionLinear(xs, ysR, ysI);
	//	}
	//
	//	public static CFunction fourierInverseParallel(CFunction input, double[] _xs, int threadCount)
	//	{
	//		int pieceCount = input.getData()[0].length;
	//		int length = _xs.length;
	//		double[] xs = VecTool.copy(_xs);
	//		double[] ysR = new double[length];
	//		double[] ysI = new double[length];
	//
	//		double[] range = input.getRange();
	//		VecTool.Func1 func = (x) ->
	//		{
	//			int i = (int)Math.round(x);
	//			double _x = xs[i];
	//			VecTool.Func1 func1R = (_k) ->
	//			{
	//				double c = Math.cos(_x * _k);
	//				double s = Math.sin(_x * _k);
	//				return c * input.binaryGetR(_k) + s * input.binaryGetI(_k);
	//			};
	//			VecTool.Func1 func1I = (_k) ->
	//			{
	//				double c = Math.cos(_x * _k);
	//				double s = Math.sin(_x * _k);
	//				return c * input.binaryGetI(_k) - s * input.binaryGetR(_k);
	//			};
	//			ysR[i] = Integrator.integral(func1R, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
	//			ysI[i] = Integrator.integral(func1I, range[0], range[1], pieceCount) / Math.sqrt(C_2PI);
	//			return 0;
	//		};
	//		Parallel.runTask(func, length, threadCount);
	//
	//		return CInterpolator.getFunctionLinear(xs, ysR, ysI);
	//	}
}
