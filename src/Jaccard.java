import java.util.ArrayList;

public class Jaccard {
	int[] vector1;
	int[] vector2;

	public Jaccard(int[] vector1,int[] vector2) {
		this.vector1 = vector1;
		this.vector2 = vector2;
	}
	
	public double calculateJaccardUser() {
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
		double jaccard = intersection.size()/union.size()*100;
		return jaccard;
	}
}
