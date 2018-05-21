import java.util.ArrayList;

public class Jaccard {
	int[] user1;
	int[] user2;

	public Jaccard(int[] user1,int[] user2) {
		this.user1 = user1;
		this.user2 = user2;
	}
	
	public double calculateJaccard() {
		int lenght = user1.length;
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		ArrayList<Integer> union = new ArrayList<Integer>();
		
		//intersection
		for(int i=0;i<lenght;i++) {
			if(user1[i]!=0) {
				for(int j=0;j<lenght;j++) {
					if(user1[i]==user2[j])
						if(!intersection.contains(user1[i]))
							intersection.add(user1[i]);
				}
			}
		}
		
		//union
		for(int i=0;i<lenght;i++) {
			if(user1[i]!=0 && (!union.contains(user1[i])))
				union.add(user1[i]);
			if(user2[i]!=0 && (!union.contains(user2[i])))
				union.add(user2[i]);
		}
		double jaccard = intersection.size()/union.size()*100;
		return jaccard;
	}
}
