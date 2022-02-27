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
}
