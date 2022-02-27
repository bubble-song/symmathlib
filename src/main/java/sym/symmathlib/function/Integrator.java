package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;

public class Integrator
{
	public static double integral(Function func, double x1, double x2, int pieceCount)
	{
		double delta = (x2 - x1) / pieceCount;
		double sum = 0;
		for(int i = 0; i < pieceCount; i++)
		{
			double x = x1 + delta * (0.5 + i);
			sum += func.get(x) * delta;
		}
		return sum;
	}
	
	public static double integral(VecTool.Func1 func1, double x1, double x2, int pieceCount)
	{
		double delta = (x2 - x1) / pieceCount;
		double sum = 0;
		for(int i = 0; i < pieceCount; i++)
		{
			double x = x1 + delta * (0.5 + i);
			sum += func1.calc(x) * delta;
		}
		return sum;
	}
}
