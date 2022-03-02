import sym.symmathlib.function.Function;
import sym.symmathlib.function.Interpolator;
import sym.symmathlib.function.Solver;

public class Test3
{
	public static void main(String[] args)
	{
		Function func = Interpolator.getFunctionAnalytic(x -> x * x - 2);
		double x = Solver.binaryGetRoot(func, 0, 2, 20);
		System.out.println(x);
		System.out.println(x * x);
	}
}
