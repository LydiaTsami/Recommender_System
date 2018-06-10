import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
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

	//Fill the nxm table with numbers from 1 to 5 leaving 100-x% empty(empty=0)
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
			String outputpath = "\\repetition" + t;
			Export(outputpath,table,"original_table.txt");
	}
	
	//Export table
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
	
	//Export user or item absolute errors
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
	
	//Calls Calculator.java to calculata jaccard,cosine or correlation(user-user)
	public void calculateMeasuresForUsers(String method,int repetition) {
		double predictedvalues[][] = new double[n][m];
		double abs_error[] = new double[n+1];
		Calculator calc = new Calculator();
		
		int[][] filled_table = new int[n][m];
		for(int i=0;i<n;i++) {
		 for(int j=0;j<m;j++) {
		  filled_table[i][j] = table[i][j];
		 }
		}

		for(int user=0;user<table.length;user++) {
			double[][] measures = new double[n][2]; //1i stili--> #user, 2i stili --> timi omoiotitas(jaccard,cosine, or corr)
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
			FillEmpty(filled_table,user,predictedvalues_user,repetition,method,"user-user");
		}
		abs_error[n] = calc.tableMeanAbsoluteError(table,predictedvalues);
		String outputpath = "\\repetition" + repetition + "\\user_user";
		Export(outputpath ,filled_table, method + "_table.txt");
		ExportErrors(outputpath,abs_error,method + "_abserrors.txt");
	}
	
	//Calls Calculator.java to calculata jaccard,cosine or correlation(item-item)
	public void calculateMeasuresForItems(String method,int repetition) {
		double predictedvalues[][] = new double[n][m];
		double abs_error[] = new double[n+1];
		Calculator calc = new Calculator();
		int[][] transpose = transposeMatrix(table);
		
		int[][] filled_table = new int[n][m];
		for(int i=0;i<n;i++) {
		 for(int j=0;j<m;j++) {
		  filled_table[i][j] = transpose[i][j];
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
			FillEmpty(filled_table,item,predictedvalues_item,repetition,method,"item-item");
		}
		abs_error[n] = calc.tableMeanAbsoluteError(table,predictedvalues);
		String outputpath = "\\repetition" + repetition + "\\item_item";
		ExportErrors(outputpath,abs_error,method + "_abserrors.txt");
		filled_table = transposeMatrix(filled_table);
		Export(outputpath ,filled_table, method + "_table.txt");
	}
	
	//Transpose original table to be able to use same method for item-item system as for user-user
	public static int[][] transposeMatrix(int [][] m){
		int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
	
	//predicts value
	  public double[] predictValues(double[][] measures,int user) {
		  double predictedvalues[] = new double[table[user].length];
		  for(int j = 0; j < table[user].length; j++) {
			  double predictedvalue=0;
			  double denominator=0;//paranomastis
				  for(int i=0;i<k;i++) {
					  if(table[(int) measures[i][0]][j]!=0) {
						  predictedvalue += table[(int) measures[i][0]][j]*measures[i][1];
						  denominator+= measures[i][1];
					  }
				  }
			if (denominator!=0)
				predictedvalue = predictedvalue/denominator;
			else {
				double new_measures[][] = new double[measures.length-k][2];
				if(measures.length-k>4) {
					for(int i=0;i<measures.length-k;i++) {
						new_measures[i][0]= measures[i+k][0];
						new_measures[i][1]= measures[i+k][1];
					}
					predictedvalues =predictValues(new_measures,user);
					return predictedvalues;
				}
			}
			predictedvalues[j] = predictedvalue;
		  }	
		  return  predictedvalues;
	  }
	  
	  //Fills empty fields with predicted values from predictValues
	  public void FillEmpty(int[][] filled_table, int user, double[] predvalues,int rep,String method, String itemuser) { 
		  for(int j=0;j<filled_table[user].length;j++) {
			  if(filled_table[user][j]==0) {
				  filled_table[user][j] = (int) Math.round(predvalues[j]);
			  }
		  }
	  }
	
}
