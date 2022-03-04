import sym.symmathlib.vector.VecTool;

public class Test2
{
	public static void main(String[] args)
	{
		double[] xs = new double[]{3, 1, 2, 4};
		int[] order = VecTool.getOrdering(xs);
		
		for(int i = 0; i < order.length; i++)
		{
			System.out.println(order[i]);
		}
	}
}
