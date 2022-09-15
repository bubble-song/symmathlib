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
	
	public double binaryGetR(double x)
	{
		return getR(x);
	}
	
	public double binaryGetI(double x)
	{
		return getI(x);
	}
	
	public abstract CFunction trans(VecTool.Func3 func3r, VecTool.Func3 func3i);
	
	public double[][] getData()
	{
		return null;
	}
}
