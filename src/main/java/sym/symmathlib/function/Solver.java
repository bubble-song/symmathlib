package sym.symmathlib.function;

public class Solver
{
	//return the root within range (x1, x2), return one of them if multiple are present
	public static double binaryGetRoot(Function func, double _x1, double _x2, int searchCount)
	{
		if(_x1 > _x2 || !Double.isFinite(_x1) || !Double.isFinite(_x2))
		{
			throw new RuntimeException("the range is abnormal!");
		}
		else if(func.binaryGet(_x1) * func.binaryGet(_x2) > 0)
		{
			throw new RuntimeException("the function may not cross zero!");
		}
		else
		{
			double x1 = _x1;
			double f1 = func.binaryGet(x1);
			double x2 = _x2;
			double f2 = func.binaryGet(x2);
			double x3;
			double f3;
			for(int i = 0; i < searchCount; i++)
			{
				x3 = 0.5 * (x1 + x2);
				f3 = func.binaryGet(x3);
				if(f1 * f3 > 0)
				{
					x1 = x3;
					f1 = f3;
				}
				else
				{
					x2 = x3;
					f2 = f3;
				}
			}
			return x1;
		}
	}
	
	//return the root starting from x1, return one of them if multiple are present
	public static double newtonianGetRoot(Function func, double _x1, int searchCount, double epsilon)
	{
		double x1 = _x1;
		for(int i = 0; i < searchCount; i++)
		{
			double f1 = func.binaryGet(x1);
			double f2 = func.binaryGet(x1 + epsilon);
			double t = (f2 - f1) / epsilon;
			x1 -= f1 / t;
		}
		return x1;
		
	}
}
