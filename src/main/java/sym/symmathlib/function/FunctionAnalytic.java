package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;

class FunctionAnalytic extends Function
{
	VecTool.Func1 func1;
	
	public FunctionAnalytic(VecTool.Func1 _func)
	{
		func1 = _func;
		range = new double[]{-Double.MAX_VALUE, Double.MAX_VALUE};
	}
	
	public double get(double x)
	{
		return func1.calc(x);
	}
}
