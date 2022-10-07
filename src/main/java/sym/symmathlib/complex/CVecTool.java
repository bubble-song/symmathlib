package sym.symmathlib.complex;

import sym.symmathlib.vector.VecTool;

//VecTool for complex calculations
public class CVecTool
{
	public static void add(double[] r1, double[] i1, double[] r2, double[] i2, double[] r3, double[] i3)
	{
		for(int i = 0; i < r1.length; i++)
		{
			r3[i] = r1[i] + r2[i];
			i3[i] = i1[i] + i2[i];
		}
	}
	
	public static void sub(double[] r1, double[] i1, double[] r2, double[] i2, double[] r3, double[] i3)
	{
		for(int i = 0; i < r1.length; i++)
		{
			r3[i] = r1[i] - r2[i];
			i3[i] = i1[i] - i2[i];
		}
	}
	
	public static void mul(double[] r1, double[] i1, double[] r2, double[] i2, double[] r3, double[] i3)
	{
		for(int i = 0; i < r1.length; i++)
		{
			r3[i] = r1[i] * r2[i] - i1[i] * i2[i];
			i3[i] = r1[i] * i2[i] + i1[i] * r1[i];
		}
	}
	
	public static void div(double[] r1, double[] i1, double[] r2, double[] i2, double[] r3, double[] i3)
	{
		for(int i = 0; i < r1.length; i++)
		{
			double val1 = 1 / (r2[i] * r2[i] + i2[i] * i2[i]);
			r3[i] = (r1[i] * r2[i] + i1[i] * i2[i]) * val1;
			i3[i] = (-r1[i] * i2[i] + i1[i] * r1[i]) * val1;
		}
	}
	
	public static void trans(VecTool.Func2 cfr, VecTool.Func2 cfi, double[] r1, double[] i1, double[] r2, double[] i2)
	{
		for(int i = 0; i < r1.length; i++)
		{
			r2[i] = cfr.calc(r1[i], i1[i]);
			i2[i] = cfi.calc(r1[i], i1[i]);
		}
	}
}
