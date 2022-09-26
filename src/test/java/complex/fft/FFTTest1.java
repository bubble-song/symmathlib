package complex.fft;

//import org.apache.commons.math3.transform.FastFourierTransformer;
import sym.symmathlib.vector.VecTool;

public class FFTTest1
{
	public static void main(String[] args)
	{
		int POINT_COUNT1 = 10000;
		int POINT_COUNT2 = 1000;
		
		double[] xs1 = VecTool.linspace(-100, 100, new double[POINT_COUNT1]);
		double[] ys1R;
		double[] ys1I;
		
		VecTool.Func1 func1 = (x) ->
		{
			double f1 = Math.cos(50 * x);
			double f2 = -0.01 * x * x;
			double f3 = f1 * Math.exp(f2);
			
			return f3;
		};
		
		ys1R = VecTool.trans(func1, xs1);
		ys1I = VecTool.trans((x) -> 0, xs1);
		
		double[] yRI = new double[POINT_COUNT1 * 2];
		for(int i = 0; i < POINT_COUNT1; i++)
		{
			yRI[2 * i + 0] = ys1R[i];
			yRI[2 * i + 1] = ys1I[i];
		}

	}
}
