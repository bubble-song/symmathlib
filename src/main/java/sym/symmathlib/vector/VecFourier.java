package sym.symmathlib.vector;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import static sym.symmathlib.constant.Constant.C_2PI;
import static sym.symmathlib.constant.Constant.C_SQRT_2PI;

public class VecFourier
{
	public static double[][] fft(double[][] _yRIs)
	{
		double[][] ysRI = VecTool.copy(_yRIs);
		FastFourierTransformer.transformInPlace(ysRI, DftNormalization.UNITARY, TransformType.FORWARD);
		return ysRI;
	}
	
	public static double[][] fftInverse(double[][] _yRIs)
	{
		double[][] ysRI = VecTool.copy(_yRIs);
		FastFourierTransformer.transformInPlace(ysRI, DftNormalization.UNITARY, TransformType.INVERSE);
		return ysRI;
	}
	
//	public static double[][] dft(double[][] _xyRIs, double[] _ks)
//	{
//		int N = _xyRIs[0].length;
//		int M = _ks.length;
//		double[] ks = VecTool.copy(_ks);
//		double[][] kRIs = new double[2][M];
//		double XD = _xyRIs[0][1] - _xyRIs[0][0];
//		double KD = ks[1] - ks[0];
//
//		for(int i = 0; i < M; i++)
//		{
//			double sumR = 0;
//			double sumI = 0;
//			for(int j = 0; j < N; j++)
//			{
//				double angle = ks[i] * _xyRIs[0][j];
//				double cos = Math.cos(angle);
//				double sin = Math.sin(angle);
//				double yR = _xyRIs[1][j];
//				double yI = _xyRIs[2][j];
//				sumR += cos * yR - sin * yI;
//				sumI += cos * yI + sin * yR;
//			}
//			kRIs[0][i] = sumR * XD / Math.sqrt(C_2PI);
//			kRIs[1][i] = sumI * XD / Math.sqrt(C_2PI);
//		}
//
//		return new double[][]{ks, kRIs[0], kRIs[1]};
//	}
	
	public static double[][] czt(double[][] xyRIs, double[] ks)
	{
		ChirpZ chirpZ = new ChirpZ(xyRIs[0], ks);
		double[][] yRIs = chirpZ.czt(new double[][]{xyRIs[1], xyRIs[2]});
		return new double[][]{ks, yRIs[0], yRIs[1]};
	}
	
	public static double[][] cztInverse(double[][] kyRIs, double[] xs)
	{
		ChirpZ chirpZ = new ChirpZ(kyRIs[0], xs);
		double[][] yRIs = chirpZ.cztInverse(new double[][]{kyRIs[1], kyRIs[2]});
		return new double[][]{xs, yRIs[0], yRIs[1]};
	}
	
	public static class ChirpZ
	{
		private final int M;
		private final int N;
		private final int L;
		
		private final double K0;
		private final double KD;
		private final double X0;
		private final double XD;
		
		private double[][] y, Y, V, G, g;
		
		public ChirpZ(double[] xs, double[] ks)
		{
			N = xs.length;
			M = ks.length;
			L = (int)Math.round(Math.pow(2, Math.ceil(Math.log(N + M - 1) / Math.log(2))));
			
			X0 = xs[0];
			XD = (xs[N - 1] - xs[0]) / (N - 1);
			
			K0 = ks[0];
			KD = (ks[M - 1] - ks[0]) / (M - 1);
			
			y = new double[2][L];
			G = new double[2][L];
			
			double[][] v = new double[2][L];
			for(int i = 0; i < M; i++)
			{
				double angle = -(KD * 0.5 * i * i) * XD;
				v[0][i] = Math.cos(angle);
				v[1][i] = Math.sin(angle);
			}
			for(int i = L - N + 1; i < L; i++)
			{
				double angle = -(KD * 0.5 * (L - i) * (L - i)) * XD;
				v[0][i] = Math.cos(angle);
				v[1][i] = Math.sin(angle);
			}
			V = fft(v);
		}
		
		public double[][] czt(double[][] yRIs)
		{
			if(yRIs[0].length != N)
			{
				throw new ArrayIndexOutOfBoundsException("Length of input vector x must be " + N);
			}
			for(int i = 0; i < N; i++)
			{
				double angle = (+K0 * i + KD * 0.5 * i * i) * XD;
				double cos = Math.cos(angle);
				double sin = Math.sin(angle);
				y[0][i] = +cos * yRIs[0][i] - sin * yRIs[1][i];
				y[1][i] = +cos * yRIs[1][i] + sin * yRIs[0][i];
			}
			for(int i = N; i < L; i++)
			{
				y[0][i] = 0;
				y[1][i] = 0;
			}
			
			Y = fft(y);
			
			for(int i = 0; i < L; i++)
			{
				G[0][i] = +Y[0][i] * V[0][i] - Y[1][i] * V[1][i];
				G[1][i] = +Y[0][i] * V[1][i] + Y[1][i] * V[0][i];
			}
			g = fftInverse(G);
			
			double[][] kRIs = new double[2][M];
			double mul = XD * Math.sqrt(L) / C_SQRT_2PI;
			for(int i = 0; i < M; i++)
			{
				double angle = +(KD * 0.5 * i * i) * XD + (K0 + KD * i) * X0;
				double cos = Math.cos(angle);
				double sin = Math.sin(angle);
				kRIs[0][i] = (+cos * g[0][i] - sin * g[1][i]) * mul;
				kRIs[1][i] = (+cos * g[1][i] + sin * g[0][i]) * mul;
			}
			
			return kRIs;
		}
		
		public double[][] cztInverse(double[][] yRIs)
		{
			if(yRIs[0].length != N)
			{
				throw new ArrayIndexOutOfBoundsException("Length of input vector x must be " + N);
			}
			for(int i = 0; i < N; i++)
			{
				double angle = (+K0 * i + KD * 0.5 * i * i) * XD;
				double cos = Math.cos(angle);
				double sin = Math.sin(angle);
				y[0][i] = +cos * yRIs[0][i] + sin * yRIs[1][i];
				y[1][i] = -cos * yRIs[1][i] + sin * yRIs[0][i];
			}
			for(int i = N; i < L; i++)
			{
				y[0][i] = 0;
				y[1][i] = 0;
			}
			
			Y = fft(y);
			
			for(int i = 0; i < L; i++)
			{
				G[0][i] = +Y[0][i] * V[0][i] - Y[1][i] * V[1][i];
				G[1][i] = +Y[0][i] * V[1][i] + Y[1][i] * V[0][i];
			}
			g = fftInverse(G);
			
			double[][] kRIs = new double[2][M];
			double mul = XD * Math.sqrt(L) / C_SQRT_2PI;
			for(int i = 0; i < M; i++)
			{
				double angle = +(KD * 0.5 * i * i) * XD + (K0 + KD * i) * X0;
				double cos = Math.cos(angle);
				double sin = Math.sin(angle);
				kRIs[0][i] = (+cos * g[0][i] - sin * g[1][i]) * mul;
				kRIs[1][i] = (-cos * g[1][i] - sin * g[0][i]) * mul;
			}
			
			return kRIs;
		}
	}
}
