import sym.symmathlib.function.Function;
import sym.symmathlib.function.Interpolator;
import sym.symmathlib.function.Solver;
import sym.symmathlib.vector.VecTool;

public class Test1
{
	public static void main(String[] args)
	{
		double[] xs = VecTool.linspace(-6, 6, new double[20000]);
		double[] ys = VecTool.trans((x) -> (Math.sin(0.5 * x)), xs);
		
		Function f = Interpolator.getFunctionLinear(xs, ys);

//		double[] xs1 = VecTool.linspace(-2, 12, new double[200]);
//		double[] ys1 = VecTool.trans(x1 -> Differentiator.derivative0(f, x1, 0.1), xs1);
//		double[] ys2 = VecTool.trans(x1 -> Differentiator.derivative1(f, x1, 0.1), xs1);
//		double[] ys3 = VecTool.trans(x1 -> Differentiator.derivative2(f, x1, 0.1), xs1);
		
//		Plot plot = new Plot();
//
//		plot.plot(xs, ys, "");
//		plot.plot(xs1, ys1, "style=-;");
//		plot.plot(xs1, ys2, "style=-;");
//		plot.plot(xs1, ys3, "style=-;");
//		plot.autoRange();
//		plot.show();
	}
}
