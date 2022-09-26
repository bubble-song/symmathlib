package sym.symmathlib.random;

import java.util.Random;

public class Distribution
{
	static Random random = new Random(123123L);
	
	public static void randomize()
	{
		random = new Random(System.nanoTime());
	}
	
	public static void randomize(long seed)
	{
		random = new Random(seed);
	}
	
	//evenly distributed
	public static class Flat
	{
		//0-1
		public static double next()
		{
			return random.nextDouble();
		}
		
		public static double next(double x1, double x2)
		{
			double rnd = random.nextDouble();
			return x1 + rnd * (x2 - x1);
		}
	}
	
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
			if(lambda < 20)
			{
				double expLambda = Math.exp(-lambda); //constant for terminating loop
				double randUni; //uniform variable
				double prodUni; //product of uniform variables
				int randPoisson; //Poisson variable
				
				randPoisson = -1;
				prodUni = 1;
				do
				{
					randUni = random.nextDouble(); //generate uniform variable
					prodUni = prodUni * randUni; //update product
					randPoisson++; //increase Poisson variable
					
				} while(prodUni > expLambda);
				return randPoisson;
			}
			else
			{
				return Normal.next(lambda, Math.sqrt(lambda));
			}
		}
	}
}
