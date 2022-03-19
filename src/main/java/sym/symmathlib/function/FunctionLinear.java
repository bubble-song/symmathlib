package sym.symmathlib.function;

import sym.symmathlib.vector.VecTool;

class FunctionLinear extends Function
{
	int length;
	double[] xs;
	double[] ys;
	int cur;
	
	public FunctionLinear(double[] _xs, double[] _ys)
	{
		int[] ordering = VecTool.getOrdering(_xs);
		length = _xs.length;
		xs = new double[length];
		ys = new double[length];
		for(int i = 0; i < length; i++)
		{
			xs[i] = _xs[ordering[i]];
			ys[i] = _ys[ordering[i]];
		}
		range = new double[]{xs[0], xs[length - 1]};
		
		cur = 0;
	}
	
	@Override
	public double get(double x)
	{
		if(x < range[0])
		{
			return ys[0];
		}
		else if(x > range[1])
		{
			return ys[length - 1];
		}
		else
		{
			findCur(x);
			double x1 = xs[cur];
			double x2 = xs[cur + 1];
			double y1 = ys[cur];
			double y2 = ys[cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	void findCur(double x)
	{
		while(x > xs[cur + 1])
		{
			cur++;
		}
		while(x < xs[cur])
		{
			cur--;
		}
	}
	
	@Override
	public double binaryGet(double x)
	{
		if(x < range[0])
		{
			return ys[0];
		}
		else if(x > range[1])
		{
			return ys[length - 1];
		}
		else
		{
			int _cur;
			{
				int cur1 = 0;
				int cur2 = length - 1;
				int cur3;
				while(cur1 < cur2 - 1)
				{
					cur3 = (cur1 + cur2) / 2;
					if(x < xs[cur3])
					{
						cur2 = cur3;
					}
					else
					{
						cur1 = cur3;
					}
				}
				_cur = cur1;
			}
			
			double x1 = xs[_cur];
			double x2 = xs[_cur + 1];
			double y1 = ys[_cur];
			double y2 = ys[_cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	@Override
	public FunctionLinear trans(VecTool.Func2 _func2)
	{
		double[] _xs = VecTool.copy(xs);
		double[] _ys = VecTool.trans(x -> _func2.calc(x, get(x)), _xs);
		return new FunctionLinear(_xs, _ys);
	}
	
	@Override
	public double[][] getData()
	{
		double[][] data = new double[2][length];
		for(int i = 0; i < length; i++)
		{
			data[0][i] = xs[i];
			data[1][i] = ys[i];
		}
		return data;
	}
}
