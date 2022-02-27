package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;

public class Differentiator
{
	public static double derivative0(Function func, double x, double radius)
	{
		double x1 = x - radius;
		double x2 = x + radius;
		double[] range = func.getRange();
		if(x1 < range[0])
		{
			x1 = range[0];
			x2 = x1 + 2 * radius;
		}
		else if(x2 > range[1])
		{
			x2 = range[1];
			x1 = x2 - 2 * radius;
		}
		double center = 0.5 * (x1 + x2);

//		0.25*(1+2*cos(2x)+cos2(2x))
//		=0.375+0.5*cos(2x)+0.125*cos(4x)
//		pi*(0.375+0+0)
		
		double val1 = Math.PI / radius;
		double val2 = 1 / (3 * radius);
		VecTool.Func1 weight = (_x) ->
		{
			double dr = val1 * (_x - center);
			double c = Math.cos(dr);
			double s = Math.sin(dr);
			double ans = val2 * (1 + c) * (1 + c);
			return ans;
		};
		double convolution = Integrator.integral((_x) -> (weight.calc(_x) * func.get(_x)), x1, x2, 1000);
		return convolution;
	}
	
	public static double derivative1(Function func, double x, double radius)
	{
		double x1 = x - radius;
		double x2 = x + radius;
		double[] range = func.getRange();
		if(x1 < range[0])
		{
			x1 = range[0];
			x2 = x1 + 2 * radius;
		}
		else if(x2 > range[1])
		{
			x2 = range[1];
			x1 = x2 - 2 * radius;
		}
		double center = 0.5 * (x1 + x2);
		
		double val1 = Math.PI / radius;
		double val2 = -1 / (3 * radius) * val1;
		VecTool.Func1 weight = (double _x) ->
		{
			double dr = val1 * (_x - center);
			double c = Math.cos(dr);
			double s = Math.sin(dr);
			double ans = val2 * -2 * s * (1 + c);
			return ans;
		};
		double convolution = Integrator.integral((_x) -> (weight.calc(_x) * func.get(_x)), x1, x2, 1000);
		return convolution;
	}
	
	public static double derivative2(Function func, double x, double radius)
	{
		double x1 = x - radius;
		double x2 = x + radius;
		double[] range = func.getRange();
		if(x1 < range[0])
		{
			x1 = range[0];
			x2 = x1 + 2 * radius;
		}
		else if(x2 > range[1])
		{
			x2 = range[1];
			x1 = x2 - 2 * radius;
		}
		double center = 0.5 * (x1 + x2);
		
		double val1 = Math.PI / radius;
		double val2 = 1 / (3 * radius) * val1 * val1;
		VecTool.Func1 weight = (double _x) ->
		{
			double dr = val1 * (_x - center);
			double c = Math.cos(dr);
			double s = Math.sin(dr);
			double ans = val2 * 4 * (1 + c) * (0.5 - c);
			return ans;
		};
		double convolution = Integrator.integral((_x) -> (weight.calc(_x) * func.get(_x)), x1, x2, 1000);
		return convolution;
	}
}
