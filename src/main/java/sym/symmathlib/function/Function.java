package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;

public abstract class Function
{
	double[] range;
	
	public double[] getRange()
	{
		return VecTool.copy(range);
	}
	
	public abstract double get(double x);
	
	public double binaryGet(double x)
	{
		return get(x);
	}
	
	public abstract Function trans(VecTool.Func2 func2);
	
	public double[][] getData()
	{
		return null;
	}
}
