package sym.symmathlib.complex;

import sym.symmathlib.vector.VecTool;

public class CFunctionLinear extends CFunction
{
	int length;
	double[] xs;
	double[] ysr;
	double[] ysi;
	int cur;
	
	public CFunctionLinear(double[] _xs, double[] _ysr, double[] _ysi)
	{
		int[] ordering = VecTool.getOrdering(_xs);
		length = _xs.length;
		xs = new double[length];
		ysr = new double[length];
		ysi = new double[length];
		for(int i = 0; i < length; i++)
		{
			xs[i] = _xs[ordering[i]];
			ysr[i] = _ysr[ordering[i]];
			ysr[i] = _ysi[ordering[i]];
		}
		range = new double[]{xs[0], xs[length - 1]};
		
		cur = 0;
	}
	
	@Override
	public double getR(double x)
	{
		if(x < range[0])
		{
			return ysr[0];
		}
		else if(x > range[1])
		{
			return ysr[length - 1];
		}
		else
		{
			findCur(x);
			double x1 = xs[cur];
			double x2 = xs[cur + 1];
			double y1 = ysr[cur];
			double y2 = ysr[cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	@Override
	public double getI(double x)
	{
		if(x < range[0])
		{
			return ysi[0];
		}
		else if(x > range[1])
		{
			return ysi[length - 1];
		}
		else
		{
			findCur(x);
			double x1 = xs[cur];
			double x2 = xs[cur + 1];
			double y1 = ysi[cur];
			double y2 = ysi[cur + 1];
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
	
	int binaryFindCur(double x)
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
		return _cur;
	}
	
	@Override
	public double binaryGetR(double x)
	{
		if(x < range[0])
		{
			return ysr[0];
		}
		else if(x > range[1])
		{
			return ysr[length - 1];
		}
		else
		{
			int _cur = binaryFindCur(x);
			
			double x1 = xs[_cur];
			double x2 = xs[_cur + 1];
			double y1 = ysr[_cur];
			double y2 = ysr[_cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	@Override
	public double binaryGetI(double x)
	{
		if(x < range[0])
		{
			return ysi[0];
		}
		else if(x > range[1])
		{
			return ysi[length - 1];
		}
		else
		{
			int _cur = binaryFindCur(x);
			
			double x1 = xs[_cur];
			double x2 = xs[_cur + 1];
			double y1 = ysi[_cur];
			double y2 = ysi[_cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	@Override
	public CFunctionLinear trans(VecTool.Func3 _func3r, VecTool.Func3 _func3i)
	{
		double[] _xs = VecTool.copy(xs);
		double[] _ysr = VecTool.trans(x -> _func3r.calc(x, getR(x), getI(x)), _xs);
		double[] _ysi = VecTool.trans(x -> _func3i.calc(x, getR(x), getI(x)), _xs);
		return new CFunctionLinear(_xs, _ysr, _ysi);
	}
	
	@Override
	public double[][] getData()
	{
		double[][] data = new double[3][length];
		for(int i = 0; i < length; i++)
		{
			data[0][i] = xs[i];
			data[1][i] = ysr[i];
			data[2][i] = ysi[i];
		}
		return data;
	}
}
