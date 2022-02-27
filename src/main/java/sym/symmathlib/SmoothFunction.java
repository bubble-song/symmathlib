package sym.symmathlib;

import sym.symmathlib.vector.VecTool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;import java.io.FileOutputStream;


public class SmoothFunction
{
	final public double[] xs;
	final public double[] ys;
	final public int length;
	int cur;
	
	public SmoothFunction(double[] _xs, double[] _ys)
	{
		xs = _xs;
		ys = _ys;
		length = xs.length;
		cur = 0;
	}
	
	public static SmoothFunction load(String name) throws Exception
	{
		DataInputStream dis = new DataInputStream(new FileInputStream(String.format("res/smooth_function/%s.data", name)));
		int length = dis.readInt();
		double[] xs = new double[length];
		double[] ys = new double[length];
		for(int i = 0; i < length; i++)
		{
			xs[i] = dis.readDouble();
			ys[i] = dis.readDouble();
		}
		return new SmoothFunction(xs, ys);
	}
	
	public static void store(SmoothFunction f, String name) throws Exception
	{
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(String.format("res/smooth_function/%s.data", name)));
		dos.writeInt(f.length);
		for(int i = 0; i < f.length; i++)
		{
			dos.writeDouble(f.xs[i]);
			dos.writeDouble(f.ys[i]);
		}
	}
	
	public static double integral(VecTool.Func1 func, double x1, double x2, int datapointCount)
	{
		double delta = (x2 - x1) / datapointCount;
		double s = 0;
		for(int i = 0; i < datapointCount; i++)
		{
			double x = x1 + delta * (0.5 + i);
			s += func.calc(x) * delta;
		}
		return s;
	}
	
	public double get(double x)
	{
		if(x < xs[0] || x > xs[length - 1])
		{
			// new Exception("smooth function range out of available range!").printStackTrace();
			if(x < xs[0])
			{
				return ys[0];
			} else
			{
				return ys[length - 1];
			}
		} else
		{
			while(x > xs[cur + 1])
			{
				cur++;
			}
			while(x < xs[cur])
			{
				cur--;
			}
			double ans = (ys[cur] * (xs[cur + 1] - x) + ys[cur + 1] * (x - xs[cur])) / (xs[cur + 1] - xs[cur]);
			return ans;
		}
	}
	
	public void gets(double[] xsNew, double[] ysNew)
	{
		for(int i = 0; i < xsNew.length; i++)
		{
			ysNew[i] = get(xsNew[i]);
		}
	}
	
	public double integral()
	{
		double x1 = xs[0], x2 = xs[length - 1];
		return integral(x1, x2);
	}
	
	public double integral(double x1, double x2)
	{
		if(x1 < xs[0] || x1 > xs[length - 1] || x2 < xs[0] || x2 > xs[length - 1])
		{
			throw new RuntimeException("smooth function range out of available range!");
		}
		
		double y1 = get(x1);
		int cur1 = cur;
		double y2 = get(x2);
		int cur2 = cur;
		double s1 = -0.5 * (y1 + ys[cur1]) * (x1 - xs[cur1]);
		double s2 = +0.5 * (y2 + ys[cur2]) * (x2 - xs[cur2]);
		double s3 = 0;
		int inc = (cur1 <= cur2) ? +1 : -1;
		for(int i = cur1; i != cur2; i += inc)
		{
			s3 += 0.5 * (ys[i + inc] + ys[i]) * (xs[i + inc] - xs[i]);
		}
		double s = s1 + s2 + s3;
		return s;
	}
	
	public double convolution(VecTool.Func1 func, double x1, double x2)
	{
		//enhanced accuracy
		if(x1 < xs[0] || x1 > xs[length - 1] || x2 < xs[0] || x2 > xs[length - 1])
		{
			throw new RuntimeException("smooth function range out of available range!");
		} else
		{
			double val1 = 1d / 6;
			double val2 = 1d / 3;
			
			int cur1, cur2;
			double s1, s2, s3 = 0;
			{
				double y1 = get(x1);
				double y2 = ys[cur];
				double y3 = func.calc(x1);
				double y4 = func.calc(xs[cur]);
				cur1 = cur;
				s1 = -(val1 * (y1 * y4 + y2 * y3) + val2 * (y1 * y3 + y2 * y4)) * (x1 - xs[cur]);
			}
			{
				double y1 = get(x2);
				double y2 = ys[cur];
				double y3 = func.calc(x2);
				double y4 = func.calc(xs[cur]);
				cur2 = cur;
				s2 = +(val1 * (y1 * y4 + y2 * y3) + val2 * (y1 * y3 + y2 * y4)) * (x2 - xs[cur]);
			}
			{
				int inc = (cur1 <= cur2) ? +1 : -1;
				for(int i = cur1; i != cur2; i += inc)
				{
					double y1 = ys[i];
					double y2 = ys[i + inc];
					double y3 = func.calc(xs[i]);
					double y4 = func.calc(xs[i + inc]);
					s3 += (val1 * (y1 * y4 + y2 * y3) + val2 * (y1 * y3 + y2 * y4)) * (xs[i + inc] - xs[i]);
				}
			}
			double s = s1 + s2 + s3;
			return s;
		}
	}
	
	
	// public double derivative0(double x, double sigma)
	// {
	// 	double x1 = x - 6 * sigma;
	// 	double x2 = x + 6 * sigma;
	// 	if(x1 < xs[0])
	// 	{
	// 		x1 = xs[0];
	// 		x2 = x1 + 12 * sigma;
	// 	}
	// 	else if(x2 > xs[length - 1])
	// 	{
	// 		x2 = xs[length - 1];
	// 		x1 = x2 - 12 * sigma;
	// 	}
	// 	double center = 0.5 * (x1 + x2);
	// 	double radius = (x2 - x1) / 12;
	
	// 	double val1 = 1 / (Math.sqrt(2 * Math.PI) * Math.pow(radius, 1));
	// 	double convolution = convolution((_x)->(val1 * Math.exp(-0.5 * Math.pow((_x - center) / radius, 2))), x1, x2);
	// 	return convolution;
	// }
	// public double derivative1(double x, double sigma)
	// {
	// 	double x1 = x - 6 * sigma;
	// 	double x2 = x + 6 * sigma;
	// 	if(x1 < xs[0])
	// 	{
	// 		x1 = xs[0];
	// 		x2 = x1 + 12 * sigma;
	// 	}
	// 	else if(x2 > xs[length - 1])
	// 	{
	// 		x2 = xs[length - 1];
	// 		x1 = x2 - 12 * sigma;
	// 	}
	// 	double center = 0.5 * (x1 + x2);
	// 	double radius = (x2 - x1) / 12;
	
	// 	double val1 = 1 / (Math.sqrt(2 * Math.PI) * Math.pow(radius, 3));
	// 	double val2 = 1 / (2 * Math.pow(radius, 2));
	// 	double convolution = convolution((_x)->(val1 * (_x - center) * Math.exp(-0.5 * Math.pow((_x - center) / radius, 2))), x1, x2);
	// 	return convolution;
	// }
	// public double derivative2(double x, double sigma)
	// {
	// 	double x1 = x - 6 * sigma;
	// 	double x2 = x + 6 * sigma;
	// 	if(x1 < xs[0])
	// 	{
	// 		x1 = xs[0];
	// 		x2 = x1 + 12 * sigma;
	// 	}
	// 	else if(x2 > xs[length - 1])
	// 	{
	// 		x2 = xs[length - 1];
	// 		x1 = x2 - 12 * sigma;
	// 	}
	// 	double center = 0.5 * (x1 + x2);
	// 	double radius = (x2 - x1) / 12;
	
	// 	double val1 = 1 / (Math.sqrt(2 * Math.PI) * Math.pow(radius, 3));
	// 	double convolution = convolution((_x)->(val1 * (-1 + Math.pow((_x - center) / radius, 2)) * Math.exp(-0.5 * Math.pow((_x - center) / radius, 2))), x1, x2);
	// 	return convolution;
	// }
	
	public double derivative0(double x, double sigma)
	{
		double x1 = x - sigma;
		double x2 = x + sigma;
		if(x1 < xs[0])
		{
			x1 = xs[0];
			x2 = x1 + 2 * sigma;
		} else if(x2 > xs[length - 1])
		{
			x2 = xs[length - 1];
			x1 = x2 - 2 * sigma;
		}
		double center = 0.5 * (x1 + x2);

//		0.25*(1+2*cos(2x)+cos2(2x))
//		=0.375+0.5*cos(2x)+0.125*cos(4x)
//		pi*(0.375+0+0)
		
		double val1 = Math.PI / sigma;
		double val2 = 1 / (3 * sigma);
		VecTool.Func1 weight = (double _x) ->
		{
			double dr = val1 * (_x - center);
			double c = Math.cos(dr);
			double s = Math.sin(dr);
			double ans = val2 * (1 + c) * (1 + c);
			return ans;
		};
//		double convolution = convolution(weight, x1, x2);
		double convolution = integral((_x) -> (weight.calc(_x) * get(_x)), x1, x2, 1000);
		return convolution;
	}
	
	public double derivative1(double x, double sigma)
	{
		double x1 = x - sigma;
		double x2 = x + sigma;
		if(x1 < xs[0])
		{
			x1 = xs[0];
			x2 = x1 + 2 * sigma;
		} else if(x2 > xs[length - 1])
		{
			x2 = xs[length - 1];
			x1 = x2 - 2 * sigma;
		}
		double center = 0.5 * (x1 + x2);
		
		double val1 = Math.PI / sigma;
		double val2 = -1 / (3 * sigma) * val1;
		VecTool.Func1 weight = (double _x) ->
		{
			double dr = val1 * (_x - center);
			double c = Math.cos(dr);
			double s = Math.sin(dr);
			double ans = val2 * -2 * s * (1 + c);
			return ans;
		};
//		double convolution = convolution(weight, x1, x2);
		double convolution = integral((_x) -> (weight.calc(_x) * get(_x)), x1, x2, 1000);
		return convolution;
	}
	
	public double derivative2(double x, double sigma)
	{
		double x1 = x - sigma;
		double x2 = x + sigma;
		if(x1 < xs[0])
		{
			x1 = xs[0];
			x2 = x1 + 2 * sigma;
		} else if(x2 > xs[length - 1])
		{
			x2 = xs[length - 1];
			x1 = x2 - 2 * sigma;
		}
		double center = 0.5 * (x1 + x2);
		
		double val1 = Math.PI / sigma;
		double val2 = 1 / (3 * sigma) * val1 * val1;
		VecTool.Func1 weight = (double _x) ->
		{
			double dr = val1 * (_x - center);
			double c = Math.cos(dr);
			double s = Math.sin(dr);
			double ans = val2 * 4 * (1 + c) * (0.5 - c);
			return ans;
		};
//		 double convolution = convolution(weight, x1, x2);
		double convolution = integral((_x) -> (weight.calc(_x) * get(_x)), x1, x2, 1000);
		return convolution;
	}
}