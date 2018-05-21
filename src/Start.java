import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Start {
	private int t,m,n,x,k;
	int[][] table;
	
	
	public Start(int users,int items,int perc,int k,int t) {
	this.n = users;
	this.m = items;
	this.x = perc;
	this.k = k;
	this.t = t;
	Populate();
	Export("table.txt");
	}
	
	public void Populate() {
		for(int repetition=0; repetition<t;repetition++) {
			table = new int[n][m];
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					int random = (int)(Math.random()*99+1);
		    		if(random<=x) 
		    			table[i][j] = (int)(Math.random()*5) +1;
				}
			}
		}
		
		System.out.println(Arrays.toString(table[0]));
		calculateMeasures(k,0);
	}
	
	public void Export(String name) {
		String output = System.getProperty("user.dir")+ "\\"+ name;
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < table.length; i++)//for each row
		{
		   for(int j = 0; j < table[i].length; j++)//for each column
		   {
		      builder.append(table[i][j]+"");//append to the output string
		      if(j < table[i].length - 1 && i<table.length)//if this is not the last row element
		         builder.append(" ");
		   }
		   builder.append("\n");//append new line at the end of the row
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(output));
			writer.write(builder.toString());//save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ExportMeasures(String name,double[][] grid) {
		String output = System.getProperty("user.dir")+ "\\"+ name;
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < grid.length; i++)//for each row
		{
		   for(int j = 0; j < grid[i].length; j++)//for each column
		   {
		      builder.append(grid[i][j]+"");//append to the output string
		      if(j < grid[i].length - 1 && i<grid.length)//if this is not the last row element
		         builder.append(" ");
		   }
		   builder.append("\n");//append new line at the end of the row
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(output));
			writer.write(builder.toString());//save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void calculateMeasures(int k,int user) {
		double[][] measures = new double[n][4];
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		for(int i=0;i<table.length;i++) {
			if(i!=user) {
				double jaccard = new Jaccard(table[user],table[i]).calculateJaccard();
				double cosine = new Cosine(table[user],table[i]).CalculateCosineSimilarity();
				double pearson = new PearsonCorrelation(table[user],table[i]).CalculateCorrelation();
				measures[i][0] = i;
				measures[i][1] = jaccard;
				measures[i][2] = cosine;
				measures[i][3] = pearson;
			}
		}
		//compare by the first column (jaccard)
		Arrays.sort(measures, (a, b) -> Double.compare(a[1], b[1]));
		ExportMeasures("jaccard.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}
		System.out.println(neighbors);
		neighbors.clear();
		
		//compare by the seconds column (cosine)
		Arrays.sort(measures, (a, b) -> Double.compare(a[2], b[2]));
		ExportMeasures("cosine.txt",measures);
		
		//compare by the third column (pearson)
		Arrays.sort(measures, (a, b) -> Double.compare(a[3], b[3]));
		ExportMeasures("pearson.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}
		System.out.println(neighbors);
	}
	
	
}
