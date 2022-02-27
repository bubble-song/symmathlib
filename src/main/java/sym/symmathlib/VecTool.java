package sym.symmathlib;

public class VecTool
{
	public static double[] linspace(double x1, double x2, double[] target)
	{
		double delta = (x2 - x1) / (target.length - 1);
		
		for(int i = 0; i < target.length; i++)
		{
			target[i] = x1 + delta * i;
		}
		return target;
	}
	
	public static double[] trans(Func func, double[] xs)
	{
		double[] target = new double[xs.length];
		return trans(func, xs, target);
	}
	
	public static double[] trans(Func func, double[] xs, double[] target)
	{
		for(int i = 0; i < xs.length; i++)
		{
			target[i] = func.calc(xs[i]);
		}
		return target;
	}
	
	public static double[] mul(double[] vec1, double[] vec2)
	{
		double[] target = new double[vec1.length];
		return mul(vec1, vec2, target);
	}
	
	public static double[] mul(double[] vec1, double[] vec2, double[] target)
	{
		for(int i = 0; i < vec1.length; i++)
		{
			target[i] = vec1[i] * vec2[i];
		}
		return target;
	}
	
	
	@FunctionalInterface
	public interface Func
	{
		double calc(double x);
	}
}