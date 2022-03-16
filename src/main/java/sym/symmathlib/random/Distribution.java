package sym.symmathlib.random;

import java.util.Random;

public class Distribution
{
	static Random random = new Random(123123L);
	
	public static class Normal
	{
		//Box Muller Transform
		//return random number from normal distribution (sigma=1, mu=0)
		public static double next()
		{
			double w1, w2, r;
			do
			{
				w1 = 2.0 * random.nextDouble() - 1.0;
				w2 = 2.0 * random.nextDouble() - 1.0;
				r = w1 * w1 + w2 * w2;
			}
			while(r >= 1.0);
			r = Math.sqrt(-2.0 * Math.log(r) / r);
			return w2 * r;
		}
		
		public static double next(double mu, double sigma)
		{
			return mu + next() * sigma;
		}
	}
	
	//use gaussian approximation
	public static class Poisson
	{
		public static double next(double lambda)
		{
			return Normal.next(lambda, Math.sqrt(lambda));
		}
	}
}
