import sym.symmathlib.vector.VecTool;

class Test2
{
	public static void main(String[] args)
	{
		var xs = new double[]{3, 1, 2, 4};
		var order = VecTool.getOrdering(xs);
		
		for(int i = 0; i < order.length; i++)
		{
			System.out.println(order[i]);
		}
	}
}
