package sym.symmathlib.complex;

import sym.symmathlib.function.Function;
import sym.symmathlib.function.Interpolator;
import sym.symmathlib.vector.VecTool;

class CFunctionAnalytical extends CFunction
{
	VecTool.Func1 func1R;
	VecTool.Func1 func1I;
	
	public CFunctionAnalytical(VecTool.Func1 _func1r, VecTool.Func1 _func1i)
	{
		func1R = _func1r;
		func1I = _func1i;
		range = new double[]{-Double.MAX_VALUE, Double.MAX_VALUE};
	}
	
	@Override
	public double getR(double x)
	{
		return func1R.calc(x);
	}
	
	@Override
	public double getI(double x)
	{
		return func1I.calc(x);
	}
	
	@Override
	public CFunctionAnalytical trans(VecTool.Func3 _func3r, VecTool.Func3 _func3i)
	{
		return new CFunctionAnalytical(x -> _func3r.calc(x, getR(x), getI(x)), x -> _func3i.calc(x, getR(x), getI(x)));
	}
	
	@Override
	public Function getFuncR()
	{
		return Interpolator.getFunctionAnalytic(func1R);
	}
	
	@Override
	public Function getFuncI()
	{
		return Interpolator.getFunctionAnalytic(func1I);
	}
	
	@Override
	public Function getFuncAbs()
	{
		VecTool.Func1 func = (x) ->
		{
			double r = getR(x);
			double i = getI(x);
			return Math.sqrt(r * r + i + i);
		};
		return Interpolator.getFunctionAnalytic(func);
	}
}
