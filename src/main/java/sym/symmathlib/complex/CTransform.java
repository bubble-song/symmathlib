package sym.symmathlib.complex;

import sym.symmathlib.function.Integrator;
import sym.symmathlib.util.Parallel;
import sym.symmathlib.vector.VecFourier;
import sym.symmathlib.vector.VecTool;

import static sym.symmathlib.constant.Constant.*;

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
	public static CFunction dft(CFunction input, double[] _ks)
	{
		double[][] xyRIs = input.getData();
		double[] ks = VecTool.copy(_ks);
		double[][] yRIs = VecFourier.czt(xyRIs, ks);
		return CInterpolator.getFunctionLinear(ks, yRIs[0], yRIs[1]);
	}
	
	//accelerated fourier inverse transform using chirp z-transform
	public static CFunction dftInverse(CFunction input, double[] _xs)
	{
		double[][] kyRIs = input.getData();
		double[] xs = VecTool.copy(_xs);
		double[][] yRIs = VecFourier.cztInverse(kyRIs, xs);
		return CInterpolator.getFunctionLinear(xs, yRIs[0], yRIs[1]);
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
