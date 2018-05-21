
public class Cosine {
	private int[] vector1, vector2;
	
	public Cosine(int[] vector1,int[] vector2) {
		this.vector1 = vector1;
		this.vector2 = vector2;
	}
	/**
	   * Method to calculate cosine similarity of vectors
	   * 1 - exactly similar (angle between them is 0)
	   * 0 - orthogonal vectors (angle between them is 90)
	   * @param vector1 - vector in the form [a1, a2, a3, ..... an]
	   * @param vector2 - vector in the form [b1, b2, b3, ..... bn]
	   * @return - the cosine similarity of vectors (ranges from 0 to 1)
	   */
	  public double CalculateCosineSimilarity() {

	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vector1.length; i++) {
	      dotProduct += vector1[i] * vector2[i];
	      normA += Math.pow(vector1[i], 2);
	      normB += Math.pow(vector2[i], 2);
	    }
	    double cosine = (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)))*100;
	    return cosine;
	  }
}
