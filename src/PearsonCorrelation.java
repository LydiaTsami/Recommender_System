
public class PearsonCorrelation {
	private int[] vector1, vector2;
	
	public PearsonCorrelation(int[] vector1,int[] vector2) {
		this.vector1 = vector1;
		this.vector2 = vector2;
	}
	
	public double CalculateCorrelation() {
	    //TODO: check here that arrays are not null, of the same length etc

	    double sx = 0.0;
	    double sy = 0.0;
	    double sxx = 0.0;
	    double syy = 0.0;
	    double sxy = 0.0;

	    int n = vector1.length;

	    for(int i = 0; i < n; ++i) {
	      double x = vector1[i];
	      double y = vector2[i];

	      sx += x;
	      sy += y;
	      sxx += x * x;
	      syy += y * y;
	      sxy += x * y;
	    }

	    // covariation
	    double cov = sxy / n - sx * sy / n / n;
	    // standard error of x
	    double sigmax = Math.sqrt(sxx / n -  sx * sx / n / n);
	    // standard error of y
	    double sigmay = Math.sqrt(syy / n -  sy * sy / n / n);

	    // correlation is just a normalized covariation
	    double pearson = cov / sigmax / sigmay;
	    System.out.println("Pearson's Correlation: " +pearson);
	    return pearson;
	  }
}
