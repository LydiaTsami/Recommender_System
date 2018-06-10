import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.sound.midi.Synthesizer;

public class Calculator {
	
	public double calculateJaccard(int[] vector1,int[] vector2) {
		int lenght = vector1.length;
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		ArrayList<Integer> union = new ArrayList<Integer>();
		
		//intersection
		for(int i=0;i<lenght;i++) {
			if(vector1[i]!=0) {
				for(int j=0;j<lenght;j++) {
					if(vector1[i]==vector2[j])
						if(!intersection.contains(vector1[i]))
							intersection.add(vector1[i]);
				}
			}
		}
		
		//union
		for(int i=0;i<lenght;i++) {
			if(vector1[i]!=0 && (!union.contains(vector1[i])))
				union.add(vector1[i]);
			if(vector2[i]!=0 && (!union.contains(vector2[i])))
				union.add(vector2[i]);
		}
		double jaccard = (double)intersection.size()/union.size();
		jaccard = BigDecimal.valueOf(jaccard)
			    .setScale(3, RoundingMode.HALF_UP)
			    .doubleValue();
		return jaccard;
	}
	
	/**
	   * Method to calculate cosine similarity of vectors
	   * 1 - exactly similar (angle between them is 0)
	   * 0 - orthogonal vectors (angle between them is 90)
	   * @param vector1 - vector in the form [a1, a2, a3, ..... an]
	   * @param vector2 - vector in the form [b1, b2, b3, ..... bn]
	   * @return - the cosine similarity of vectors (ranges from 0 to 1)
	   */
	  public double CalculateCosineSimilarity(int[] vector1,int[] vector2) {

	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vector1.length; i++) {
	      dotProduct += vector1[i] * vector2[i];
	      normA += Math.pow(vector1[i], 2);
	      normB += Math.pow(vector2[i], 2);
	    }
	    double cosine = (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
	    cosine = BigDecimal.valueOf(cosine)
			    .setScale(3, RoundingMode.HALF_UP)
			    .doubleValue();
	    return cosine;
	  }
	  
	  public double CalculateCorrelation(int[] vector1,int[] vector2) {
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
		    pearson = BigDecimal.valueOf(pearson)
				    .setScale(3, RoundingMode.HALF_UP)
				    .doubleValue();
		    return pearson;
		  }
	  
	  //Calculates the mean of absolute errors between 2 1-dimensional grids.
	  public double meanAbsoluteError(int[] realV, double[] predictedV) {
			int count = 0;
			double sum=0;
			
			for(int i=0; i<realV.length; i++) { 
				if(realV[i] !=0 && predictedV[i] !=0) {
					sum += Math.abs(realV[i] - predictedV[i]);
					count++;
				}
			}
			
			double result = sum/count;
			if(sum!=0)
				result = BigDecimal.valueOf(result)
					.setScale(3, RoundingMode.HALF_UP)
				    .doubleValue();
			return result;
		}
	  
	  //Calculates table's Absolute Error
	  public double tableMeanAbsoluteError(int table[][],double predicted[][]) {
			int count = 0;
			double sum=0;
			
			for(int i=0;i<table.length;i++) {
				for(int j=0; j<table[0].length; j++) { 
					if(table[i][j] !=0 && predicted[i][j] !=0) {
						sum += Math.abs(table[i][j] - predicted[i][j]);
						count++;
					}
				}
			}
			
			double result = sum/count;
			result = BigDecimal.valueOf(result)
				    .setScale(3, RoundingMode.HALF_UP)
				    .doubleValue();
			return result;
		}


}
