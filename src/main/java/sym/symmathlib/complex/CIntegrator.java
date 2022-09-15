package sym.symmathlib.complex;

import sym.symmathlib.vector.VecTool;

public class CIntegrator
{
	public static double[] integral(CFunction func, double x1, double x2, int pieceCount)
	{
		double delta = (x2 - x1) / pieceCount;
		double sumR = 0;
		double sumI = 0;
		for(int i = 0; i < pieceCount; i++)
		{
			double x = x1 + delta * (0.5 + i);
			sumR += func.getR(x) * delta;
			sumI += func.getI(x) * delta;
		}
		return new double[]{sumR, sumI};
	}
	
	public static double[] integral(VecTool.Func1 func1R, VecTool.Func1 func1I, double x1, double x2, int pieceCount)
	{
		double delta = (x2 - x1) / pieceCount;
		double sumR = 0;
		double sumI = 0;
		for(int i = 0; i < pieceCount; i++)
		{
			double x = x1 + delta * (0.5 + i);
			sumR += func1R.calc(x) * delta;
			sumI += func1I.calc(x) * delta;
		}
		return new double[]{sumR, sumI};
	}
}
