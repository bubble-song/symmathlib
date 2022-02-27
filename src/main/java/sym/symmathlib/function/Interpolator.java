package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;

public class Interpolator
{
	public static Function getFunctionAnalytic(VecTool.Func1 func1)
	{
		return new FunctionAnalytic(func1);
	}
}
