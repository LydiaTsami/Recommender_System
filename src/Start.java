import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class Start {
	private int m,n,x,k;
	int[][] table;
	
	
	public Start(int users,int items,int perc,int k) {
	this.n = users;
	this.m = items;
	this.x = perc;
	this.k = k;
	}
	
	public void Populate(int t) {
			int number= n*m*x/100;
			table = new int[n][m];
			Stack<Integer> numbers = new Stack<Integer>();
		
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
			String outputpath = "\\repetition" + (t+1);
			Export(outputpath,table,"original_table.txt");
	}
	
	public void Export(String path,int grid[][],String name) {
		String output = System.getProperty("user.dir")+ "\\solutions"+ path;
		new File(output).mkdirs();
        output =output +  "\\" + name;
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
	
	public void ExportErrors(String path,double grid[],String name) {
		String output = System.getProperty("user.dir")+ "\\solutions"+ path;
		new File(output).mkdirs();
        output =output +  "\\" + name;
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < n+1; i++)//for each row
		{
			if(i==n)
				builder.append("Pinakas " +grid[i]);
			else
				builder.append(i + " " +grid[i]);//append to the output string
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
	
	public void calculateMeasuresForUsers(String method,int repetition) {
		double predictedvalues[][] = new double[n][m];
		double abs_error[] = new double[n+1];
		Calculator calc = new Calculator();
		
		int[][] copied = new int[n][m];
		for(int i=0;i<n;i++) {
		 for(int j=0;j<m;j++) {
		  copied[i][j] = table[i][j];
		 }
		}

		for(int user=0;user<table.length;user++) {
			double[][] measures = new double[n][2];
			double predictedvalues_user[];
			for(int i=0;i<table.length;i++) {
				if(i!=user) {
					measures[i][0] = i;
					if(method=="jaccard")
						measures[i][1] = calc.calculateJaccard(table[user],table[i]);
					else if(method=="cosine") {
						measures[i][1] = calc.CalculateCosineSimilarity(table[user],table[i]);
					}
					else if(method=="corr") {
						measures[i][1] = calc.CalculateCorrelation(table[user],table[i]);
					}
				}
			}
			//compare by the first column (jaccard)
			Arrays.sort(measures, (a, b) -> Double.compare(b[1], a[1]));
			predictedvalues_user = predictValues(measures,user);
			predictedvalues[user]=predictedvalues_user;
			abs_error[user] = calc.meanAbsoluteError(table[user],predictedvalues_user);//meso apolito lathos ana xristi
			FillEmpty(copied,user,predictedvalues_user);
		}
		abs_error[n] = calc.tableMeanAbsoluteError(table,predictedvalues);
		String outputpath = "\\repetition" + (repetition+1) + "\\user_user";
		Export(outputpath ,copied, method + "_table.txt");
		ExportErrors(outputpath,abs_error,method + "_abserrors.txt");
	}
	
	public void calculateMeasuresForItems(String method,int repetition) {
		double predictedvalues[][] = new double[n][m];
		double abs_error[] = new double[n+1];
		Calculator calc = new Calculator();
		int[][] transpose = transposeMatrix(table);
		
		int[][] copied = new int[n][m];
		for(int i=0;i<n;i++) {
		 for(int j=0;j<m;j++) {
		  copied[i][j] = transpose[i][j];
		 }
		}

		for(int item=0;item<transpose.length;item++) {
			double[][] measures = new double[n][2];
			double predictedvalues_item[];
			for(int i=0;i<transpose.length;i++) {
				if(i!=item) {
					measures[i][0] = i;
					if(method=="jaccard")
						measures[i][1] = calc.calculateJaccard(transpose[item],transpose[i]);
					else if(method=="cosine") {
						measures[i][1] = calc.CalculateCosineSimilarity(transpose[item],transpose[i]);
					}
					else if(method=="corr") {
						measures[i][1] = calc.CalculateCorrelation(transpose[item],transpose[i]);
					}
				}
			}
			//compare by the first column (jaccard)
			Arrays.sort(measures, (a, b) -> Double.compare(b[1], a[1]));
			predictedvalues_item = predictValues(measures,item);
			predictedvalues[item]=predictedvalues_item;
			abs_error[item] = calc.meanAbsoluteError(transpose[item],predictedvalues_item);//meso apolito lathos/item
			FillEmpty(copied,item,predictedvalues_item);
		}
		abs_error[n] = calc.tableMeanAbsoluteError(table,predictedvalues);
		String outputpath = "\\repetition" + (repetition+1) + "\\item_item";
		copied = transposeMatrix(copied);
		Export(outputpath ,copied, method + "_table.txt");
		ExportErrors(outputpath,abs_error,method + "_abserrors.txt");
	}
	
	public static int[][] transposeMatrix(int [][] m){
		int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
	
	  public double[] predictValues(double[][] measures,int user) {
		  double predictedvalues[] = new double[table[user].length];
		  for(int j = 0; j < table[user].length; j++) {
			  double predictedvalue=0;
			  double denominator=0;
				  for(int i=0;i<k;i++) {
					  if(table[(int) measures[i][0]][j]!=0) {
						  double value = table[(int) measures[i][0]][j]*measures[i][1];
						  System.out.println("predicted value for (" +measures[i][0] +"," + j +") " +value+ " " + table[(int) measures[i][0]][j] + " " + measures[i][1]);
						  predictedvalue += table[(int) measures[i][0]][j]*measures[i][1];
						  denominator+= measures[i][1];
					  }
				  }
			if (denominator!=0)
				predictedvalue = predictedvalue/denominator;
			predictedvalues[j] = predictedvalue;
		  }		
		  return  predictedvalues;
	  }
	  
	  public void FillEmpty(int[][] copied, int user, double[] predvalues) { 
		  for(int j=0;j<copied[user].length;j++) {
			  if(copied[user][j]==0) {
				  copied[user][j] = (int) Math.round(predvalues[j]);
			  }
		  }
	  }
	
}
