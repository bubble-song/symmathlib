package sym.symmathlib.complex;

import sym.symmathlib.function.Function;
import sym.symmathlib.vector.VecTool;

public abstract class CFunction
{
	double[] range;
	
	public double[] getRange()
	{
		return VecTool.copy(range);
	}
	
	public abstract double getR(double x);
	
	public abstract double getI(double x);
	
	public double getAbs2(double x)
	{
		double r = getR(x);
		double i = getI(x);
		return r * r + i * i;
	}
	
	public double getAbs(double x)
	{
		return Math.sqrt(getAbs2(x));
	}
	
	
	public double binaryGetR(double x)
	{
		return getR(x);
	}
	
	public double binaryGetI(double x)
	{
		return getI(x);
	}
	
	public abstract CFunction trans(VecTool.Func3 func3R, VecTool.Func3 func3I);
	
	public double[][] getData()
	{
		return null;
	}
	
	public abstract Function getFuncR();
	
	public abstract Function getFuncI();
	
	public abstract Function getFuncAbs();
}
