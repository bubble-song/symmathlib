package sym.symmathlib.complex;

import sym.symmathlib.constant.Constant;
import sym.symmathlib.function.Integrator;
import sym.symmathlib.vector.VecTool;

public class CTransform
{
	//perform complex fourier transform
	//kernel: e^ikx
	public static CFunction fourier(CFunction input, double[] _xs)
	{
		int pieceCount = 1000;
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
			ysR[i] = Integrator.integral(func1R, range[0], range[1], pieceCount) / Math.sqrt(Constant.C_2PI);
			ysI[i] = Integrator.integral(func1I, range[0], range[1], pieceCount) / Math.sqrt(Constant.C_2PI);
		}
		
		return CInterpolator.getFunctionLinear(xs, ysR, ysI);
	}
	
	//perform complex reverse fourier transform
	//kernel: e^-ikx
	public static CFunction fourierReverse(CFunction input, double[] _xs)
	{
		int pieceCount = 1000;
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
				return c * input.getR(_x) + s * input.getI(_x);
			};
			VecTool.Func1 func1I = (_k) ->
			{
				double c = Math.cos(_x * _k);
				double s = Math.sin(_x * _k);
				return c * input.getI(_x) - s * input.getR(_x);
			};
			ysR[i] = Integrator.integral(func1R, range[0], range[1], pieceCount) / Math.sqrt(Constant.C_2PI);
			ysI[i] = Integrator.integral(func1I, range[0], range[1], pieceCount) / Math.sqrt(Constant.C_2PI);
		}
		
		return CInterpolator.getFunctionLinear(xs, ysR, ysI);
	}
}
