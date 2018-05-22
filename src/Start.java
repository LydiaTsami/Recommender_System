import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

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
		//for(int repetition=0; repetition<t;repetition++) {
			int number= n*m*x/100;
			table = new int[n][m];
			Stack<Integer> numbers = new Stack<Integer>();
			System.out.println(number);
		
			for(int i=0;i<number;i++) {
				numbers.push((int)(Math.random()*5) +1);
			}
			for(int i=number;i<n*m;i++) {
				numbers.push(0);
			}
			Collections.shuffle(numbers);
		
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					table[i][j]= numbers.pop();
		    	}
			}
			findEmptyByUser();
//			System.out.println(Arrays.toString(table[0]));
//		}
	}
	
	public void findEmptyByUser() {
		for(int i=0 ;i<table.length;i++) {
			for(int j = 0; j < table[i].length; j++) {
				if(table[i][j]==0) {
					calculateMeasuresForUsers(k,i);
					break;
				}
			}
		}
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
	
	public void calculateMeasuresForUsers(int k,int user) {
		double[][] measures = new double[n][4];
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		for(int i=0;i<table.length;i++) {
			if(i!=user) {
				double jaccard = new Jaccard(table[user],table[i]).calculateJaccardUser();
				double cosine = new Cosine(table[user],table[i]).CalculateCosineSimilarity();
				double pearson = new PearsonCorrelation(table[user],table[i]).CalculateCorrelation();
				measures[i][0] = i;
				measures[i][1] = jaccard;
				measures[i][2] = cosine;
				measures[i][3] = pearson;
			}
		}
		//compare by the first column (jaccard)
		Arrays.sort(measures, (a, b) -> Double.compare(b[1], a[1]));
		ExportMeasures("jaccard_users.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}	
		neighbors.clear();
		
		//compare by the seconds column (cosine)
		Arrays.sort(measures, (a, b) -> Double.compare(b[2], a[2]));
		ExportMeasures("cosine_users.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}
		neighbors.clear();
		
		//compare by the third column (pearson)
		Arrays.sort(measures, (a, b) -> Double.compare(b[3], a[3]));
		ExportMeasures("pearson_users.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}
	}
	
	public void calculateMeasuresForItems(int k,int item) {
		double[][] measures = new double[n][4];
		int[][] transpose = transposeMatrix(table);
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		for(int i=0;i<table.length;i++) {
			if(i!=item) {
				double jaccard = new Jaccard(transpose[item],transpose[i]).calculateJaccardUser();
				double cosine = new Cosine(transpose[item],transpose[i]).CalculateCosineSimilarity();
				double pearson = new PearsonCorrelation(transpose[item],transpose[i]).CalculateCorrelation();
				measures[i][0] = i;
				measures[i][1] = jaccard;
				measures[i][2] = cosine;
				measures[i][3] = pearson;
			}
		}
		//compare by the first column (jaccard)
		Arrays.sort(measures, (a, b) -> Double.compare(b[1], a[1]));
		ExportMeasures("jaccard_items.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}
		neighbors.clear();
		
		//compare by the seconds column (cosine)
		Arrays.sort(measures, (a, b) -> Double.compare(b[2], a[2]));
		ExportMeasures("cosine_item.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}
		neighbors.clear();
		
		//compare by the third column (pearson)
		Arrays.sort(measures, (a, b) -> Double.compare(a[3], a[3]));
		ExportMeasures("pearson_items.txt",measures);
		for(int j=measures.length-1;j>measures.length-1-k;j--) {
			neighbors.add((int) measures[j][0]);
		}
	}
	
	public static int[][] transposeMatrix(int [][] m){
		int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
	
}
