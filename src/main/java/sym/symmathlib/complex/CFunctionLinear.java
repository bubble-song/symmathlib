package sym.symmathlib.complex;

import sym.symmathlib.function.Function;
import sym.symmathlib.function.Interpolator;
import sym.symmathlib.vector.VecTool;

class CFunctionLinear extends CFunction
{
	int length;
	double[] xs;
	double[] ysR;
	double[] ysI;
	int cur;
	
	public CFunctionLinear(double[] _xs, double[] _ysR, double[] _ysI)
	{
		int[] ordering = VecTool.getOrdering(_xs);
		length = _xs.length;
		xs = new double[length];
		ysR = new double[length];
		ysI = new double[length];
		for(int i = 0; i < length; i++)
		{
			xs[i] = _xs[ordering[i]];
			ysR[i] = _ysR[ordering[i]];
			ysR[i] = _ysI[ordering[i]];
		}
		range = new double[]{xs[0], xs[length - 1]};
		
		cur = 0;
	}
	
	@Override
	public double getR(double x)
	{
		if(x < range[0])
		{
			return ysR[0];
		}
		else if(x > range[1])
		{
			return ysR[length - 1];
		}
		else
		{
			findCur(x);
			double x1 = xs[cur];
			double x2 = xs[cur + 1];
			double y1 = ysR[cur];
			double y2 = ysR[cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	@Override
	public double getI(double x)
	{
		if(x < range[0])
		{
			return ysI[0];
		}
		else if(x > range[1])
		{
			return ysI[length - 1];
		}
		else
		{
			findCur(x);
			double x1 = xs[cur];
			double x2 = xs[cur + 1];
			double y1 = ysI[cur];
			double y2 = ysI[cur + 1];
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
			return ysR[0];
		}
		else if(x > range[1])
		{
			return ysR[length - 1];
		}
		else
		{
			int _cur = binaryFindCur(x);
			
			double x1 = xs[_cur];
			double x2 = xs[_cur + 1];
			double y1 = ysR[_cur];
			double y2 = ysR[_cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	@Override
	public double binaryGetI(double x)
	{
		if(x < range[0])
		{
			return ysI[0];
		}
		else if(x > range[1])
		{
			return ysI[length - 1];
		}
		else
		{
			int _cur = binaryFindCur(x);
			
			double x1 = xs[_cur];
			double x2 = xs[_cur + 1];
			double y1 = ysI[_cur];
			double y2 = ysI[_cur + 1];
			double y = (y1 * (x2 - x) + y2 * (x - x1)) / (x2 - x1);
			return y;
		}
	}
	
	@Override
	public CFunctionLinear trans(VecTool.Func3 _func3R, VecTool.Func3 _func3I)
	{
		double[] _xs = VecTool.copy(xs);
		double[] _ysr = VecTool.trans(x -> _func3R.calc(x, getR(x), getI(x)), _xs);
		double[] _ysi = VecTool.trans(x -> _func3I.calc(x, getR(x), getI(x)), _xs);
		return new CFunctionLinear(_xs, _ysr, _ysi);
	}
	
	@Override
	public double[][] getData()
	{
		double[][] data = new double[3][length];
		for(int i = 0; i < length; i++)
		{
			data[0][i] = xs[i];
			data[1][i] = ysR[i];
			data[2][i] = ysI[i];
		}
		return data;
	}
	
	@Override
	public Function getFuncR()
	{
		double[] _xs = VecTool.copy(xs);
		double[] _ys = VecTool.copy(ysR);
		return Interpolator.getFunctionLinear(_xs, _ys);
	}
	
	@Override
	public Function getFuncI()
	{
		double[] _xs = VecTool.copy(xs);
		double[] _ys = VecTool.copy(ysI);
		return Interpolator.getFunctionLinear(_xs, _ys);
	}
	
	@Override
	public Function getFuncAbs()
	{
		VecTool.Func1 func = (x) ->
		{
			double r = getR(x);
			double i = getI(x);
			return Math.sqrt(r * r + i + i);
		};
		double[] _xs = VecTool.copy(xs);
		double[] _ys = VecTool.trans(func, xs);
		return Interpolator.getFunctionLinear(_xs, _ys);
	}
}
