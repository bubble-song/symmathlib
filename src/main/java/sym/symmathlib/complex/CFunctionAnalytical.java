package sym.symmathlib.complex;

import sym.symmathlib.vector.VecTool;

public class CFunctionAnalytical extends CFunction
{
	VecTool.Func1 func1r;
	VecTool.Func1 func1i;
	
	public CFunctionAnalytical(VecTool.Func1 _func1r, VecTool.Func1 _func1i)
	{
		func1r = _func1r;
		func1i = _func1i;
		range = new double[]{-Double.MAX_VALUE, Double.MAX_VALUE};
	}
	
	@Override
	public double getR(double x)
	{
		return func1r.calc(x);
	}
	
	@Override
	public double getI(double x)
	{
		return func1i.calc(x);
	}
	
	@Override
	public CFunctionAnalytical trans(VecTool.Func3 _func3r, VecTool.Func3 _func3i)
	{
		return new CFunctionAnalytical(x -> _func3r.calc(x, getR(x), getI(x)), x -> _func3i.calc(x, getR(x), getI(x)));
	}
}
