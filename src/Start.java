import java.util.ArrayList;

public class Start {
	private int t,m,n,x,k;
	int[][] multi;
	
	
	public Start(int users,int items,int perc,int k,int t) {
	this.n = users;
	this.m = items;
	this.x = perc;
	this.k = k;
	this.t = t;
	System.out.println(n+ " "+m+ " "+x+ " "+t+ " "+k);
	Populate();
	}
	
	public void Populate() {
		multi = new int[m][n];
		for (int i = 0; i < multi.length; i++) {
		    for (int j = 0; j < multi[i].length; j++) {
		    	int random = (int)(Math.random()*100);
		    	if(random<=x)
		    		multi[i][j] = 1;
		    }
		}
	}
	
	
}
