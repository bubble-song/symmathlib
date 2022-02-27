import sym.symmathlib.vector.VecTool;
import sym.symplotlib.Plot;

class Test1
{
	public static void main(String[] args)
	{
		var xs = VecTool.linspace(0, 10, new double[100]);
		var ys = VecTool.trans((x) -> (Math.sin(x)), xs);
		
		Plot plot = new Plot();
		
		plot.plot(xs, ys, "");
		plot.autoRange();
		plot.show();
	}
}
