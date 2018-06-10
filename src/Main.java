import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	private static int t,m,n,x,k;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String input = System.getProperty("user.dir")+ "\\"+ "configuration.txt";
		Retrieve(input);
		File file =new File(System.getProperty("user.dir")+ "\\solutions");
		deleteDir(file);
		for(int i=0;i<t;i++){
			Start start =new Start(n,m,x,k);
			start.Populate(i);
			start.calculateMeasuresForUsers("jaccard",i);
			start.calculateMeasuresForUsers("cosine",i);
			start.calculateMeasuresForUsers("corr",i);
			start.calculateMeasuresForItems("jaccard",i);
			start.calculateMeasuresForItems("cosine",i);
			start.calculateMeasuresForItems("corr",i);
		}
		System.out.println("Completed");
		
//		Start start =new Start(n,m,x,k);
//		start.Import();
//		start.calculateMeasuresForUsers("jaccard",1);
		
	}
	
	public static void Retrieve(String input) {
		try
		{
			File f = new File(input);
			Scanner scan = new Scanner(f);
			
			t = Integer.parseInt(scan.nextLine());
			n = Integer.parseInt(scan.nextLine());
			m = Integer.parseInt(scan.nextLine());
			x = Integer.parseInt(scan.nextLine());
			k = Integer.parseInt(scan.nextLine());

			scan.close();
		}catch(IOException i){
			System.out.println("Failed to read file");
			i.printStackTrace();
		}
	}
	
	static void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            if (! Files.isSymbolicLink(f.toPath())) {
	                deleteDir(f);
	            }
	        }
	    }
	    file.delete();
	}

}
