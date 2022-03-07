package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;

class FunctionAnalytical extends Function
{
	VecTool.Func1 func1;
	
	public FunctionAnalytical(VecTool.Func1 _func)
	{
		func1 = _func;
		range = new double[]{-Double.MAX_VALUE, Double.MAX_VALUE};
	}
	
	@Override
	public double get(double x)
	{
		return func1.calc(x);
	}
	
	@Override
	public FunctionAnalytical trans(VecTool.Func2 _func2)
	{
		return new FunctionAnalytical(x -> _func2.calc(x, get(x)));
	}
}
