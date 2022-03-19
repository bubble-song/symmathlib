package sym.symmathlib.vector;

import java.util.Comparator;
import java.util.stream.IntStream;

public class VecTool
{
	
	//basic arithmetics
	
	public static double[] add(double[] vec1, double[] vec2)
	{
		double[] target = new double[vec1.length];
		return add(vec1, vec2, target);
	}
	
	public static double[] add(double[] vec1, double[] vec2, double[] target)
	{
		for(int i = 0; i < vec1.length; i++)
		{
			target[i] = vec1[i] + vec2[i];
		}
		return target;
	}
	
	public static double[] sub(double[] vec1, double[] vec2)
	{
		double[] target = new double[vec1.length];
		return sub(vec1, vec2, target);
	}
	
	public static double[] sub(double[] vec1, double[] vec2, double[] target)
	{
		for(int i = 0; i < vec1.length; i++)
		{
			target[i] = vec1[i] - vec2[i];
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
	
	public static double[] div(double[] vec1, double[] vec2)
	{
		double[] target = new double[vec1.length];
		return div(vec1, vec2, target);
	}
	
	public static double[] div(double[] vec1, double[] vec2, double[] target)
	{
		for(int i = 0; i < vec1.length; i++)
		{
			target[i] = vec1[i] / vec2[i];
		}
		return target;
		
	}
	
	//initialization methods
	
	public static double[] linspace(double x1, double x2, double[] target)
	{
		double delta = (x2 - x1) / (target.length - 1);
		
		for(int i = 0; i < target.length; i++)
		{
			target[i] = x1 + delta * i;
		}
		return target;
	}
	
	//transform methods
	
	public static double[] copy(double[] xs)
	{
		double[] target = new double[xs.length];
		return copy(xs, target);
	}
	
	public static double[] copy(double[] xs, double[] target)
	{
		System.arraycopy(xs, 0, target, 0, xs.length);
		return target;
	}
	
	public static double[] trans(Func1 func, double[] xs)
	{
		double[] target = new double[xs.length];
		return trans(func, xs, target);
	}
	
	public static double[] trans(Func1 func, double[] xs, double[] target)
	{
		for(int i = 0; i < xs.length; i++)
		{
			target[i] = func.calc(xs[i]);
		}
		return target;
	}
	
	public static int[] getOrdering(double[] vec1)
	{
		int[] ordering = IntStream.range(0, vec1.length).boxed().sorted(Comparator.comparingDouble(i -> vec1[i])).mapToInt(ele -> ele).toArray();
		return ordering;
	}
	
	public static double sum(double[] vec1)
	{
		double sum = 0;
		for(double i : vec1)
		{
			sum += i;
		}
		return sum;
	}
	
	public static double[] normalize(double[] vec1)
	{
		int length = vec1.length;
		double[] ans = new double[length];
		double mul = 1 / sum(vec1);
		for(int i = 0; i < length; i++)
		{
			ans[i] = vec1[i] * mul;
		}
		return ans;
	}
	
	@FunctionalInterface
	public interface Func0
	{
		double calc();
	}
	
	@FunctionalInterface
	public interface Func1
	{
		double calc(double x1);
	}
	
	@FunctionalInterface
	public interface Func2
	{
		double calc(double x1, double x2);
	}
}