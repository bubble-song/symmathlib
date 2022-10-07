package sym.symmathlib.vector;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class VecTransform
{
	//convolution using fft method
	public static double[] vecConvolution(double[] vec1, double[] vec2)
	{
		int M = vec1.length;
		int N = vec2.length;
		int K = M + N - 1;
		int L = (int) Math.round(Math.pow(2, Math.ceil(Math.log(K) / Math.log(2))));
		
		double[][] yRIs1 = new double[2][L];
		double[][] yRIs2 = new double[2][L];
		
		System.arraycopy(vec1, 0, yRIs1[0], 0, M);
		System.arraycopy(vec2, 0, yRIs2[0], 0, N);
		
		FastFourierTransformer.transformInPlace(yRIs1, DftNormalization.STANDARD, TransformType.FORWARD);
		FastFourierTransformer.transformInPlace(yRIs2, DftNormalization.STANDARD, TransformType.FORWARD);
		
		double[][] yRIs3 = new double[2][L];
		for(int i = 0; i < L; i++)
		{
			yRIs3[0][i] = yRIs1[0][i] * yRIs2[0][i] - yRIs1[1][i] * yRIs2[1][i];
			yRIs3[1][i] = yRIs1[0][i] * yRIs2[1][i] + yRIs1[1][i] * yRIs2[0][i];
		}
		FastFourierTransformer.transformInPlace(yRIs3, DftNormalization.STANDARD, TransformType.INVERSE);
		double[] ans = new double[K];
		System.arraycopy(yRIs3[0], 0, ans, 0, K);
		return ans;
	}
	
	public static double[][] vecComplexConvolution(double[][] _yRIs1, double[][] _yRIs2)
	{
		int M = _yRIs1[0].length;
		int N = _yRIs2[0].length;
		int K = M + N - 1;
		int L = (int) Math.round(Math.pow(2, Math.ceil(Math.log(K) / Math.log(2))));
		
		double[][] yRIs1 = new double[2][L];
		double[][] yRIs2 = new double[2][L];
		
		System.arraycopy(_yRIs1[0], 0, yRIs1[0], 0, M);
		System.arraycopy(_yRIs1[1], 0, yRIs1[1], 0, M);
		System.arraycopy(_yRIs2[0], 0, yRIs2[0], 0, N);
		System.arraycopy(_yRIs2[1], 0, yRIs2[1], 0, N);
		
		FastFourierTransformer.transformInPlace(yRIs1, DftNormalization.STANDARD, TransformType.FORWARD);
		FastFourierTransformer.transformInPlace(yRIs2, DftNormalization.STANDARD, TransformType.FORWARD);
		
		double[][] yRIs3 = new double[2][L];
		for(int i = 0; i < L; i++)
		{
			yRIs3[0][i] = yRIs1[0][i] * yRIs2[0][i] - yRIs1[1][i] * yRIs2[1][i];
			yRIs3[1][i] = yRIs1[0][i] * yRIs2[1][i] + yRIs1[1][i] * yRIs2[0][i];
		}
		FastFourierTransformer.transformInPlace(yRIs3, DftNormalization.STANDARD, TransformType.INVERSE);
		double[][] ans = new double[2][K];
		System.arraycopy(yRIs3[0], 0, ans[0], 0, K);
		System.arraycopy(yRIs3[0], 0, ans[1], 0, K);
		return ans;
	}
}
