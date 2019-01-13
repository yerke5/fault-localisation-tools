import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		runTarantula("YEAR INFO", "wrongYear10.txt");
		runTarantula("YEAR INFO", "wrongYear50.txt");
		runTarantula("YEAR INFO", "wrongYear100.txt");
		runTarantula("TARANTULA'S PAPER", "tarantula.txt");
		runTarantula("CROSSTAB'S PAPER", "crosstab.txt");
	}
	
	public static void getMatrixFor(String readFile, String writeFile) {
		try {
			Parser.constructMatrix(readFile, writeFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not read matrix from Numbercompare.html");
			System.exit(1);
		}
	}
	
	public static void runTarantula(String programName, String fileName) {
		/*int[][] m;
		try {
			m = Parser.constructMatrix().matrix;
			Tarantula t = new Tarantula(m);
			t.printScores();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not read matrix from Numbercompare.html");
			System.exit(1);
		}*/
		
		// Crosstab Matrix
		/*int[][] m = {
			{1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1},
			{1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0},
			{1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0},
			{1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0},
			{1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0},
			{1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0},
			{1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0},
			{0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0},
			{1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1},
			{0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0},
			{1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0},
			{1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0},
			{0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0},
			{1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1},
			{0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0},
			{1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0},
			{1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1},
			{1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0},
			{0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0},
			{1, 1, 0, 0, 0, 0, 1 ,1, 0, 1, 1},
			{0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0},
			{0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0},
			{1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0},
			{1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1},
			{1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0},
			{1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0},
			{0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0},
			{0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0},
			{1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0},
			{1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
			{1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0},
			{1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0},
			{1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1},
			{0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0},
			{0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0}
        };*/
		// Tarantula Matrix
		/*int[][] m = {
			{1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0},
			{1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
			{1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0},
			{1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0},
			{1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
			{1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1}
		};*/
		
		int[][] matrix = getMatrixFrom(fileName);
		out("______________________________________________________");
		out(programName + " MATRIX WITH " + matrix.length + " TEST CASES");
		out("______________________________________________________");
		Tarantula t = new Tarantula(matrix);
		t.printScores();
		/*int line = 8;
		out("Passed: " + t.passed(line) + "; Failed: " + t.failed(line) + "; TotalPassed: " + t.totalPassed() + "; TotalFailed: " + t.totalFailed());
		double tp = t.totalPassed() == 0 ? 0 : (0.0 + t.passed(line)) / t.totalPassed();
		out(tp + "");
		double tf = t.totalFailed() == 0 ? 0 : (0.0 + t.failed(line)) / t.totalFailed();
		out(tf + "");
		double score = (tp + tf == 0) ? 0 : tp / (tp + tf);
		out("" + (1 - score));*/
	}
	
	public static void out(String s) {
		System.out.println(s);
	}
	
	public static int[][] getMatrixFrom(String fileName) {
		try {
			ArrayList<String> temp = new ArrayList<String>();
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String vector;
            while((vector = bufferedReader.readLine()) != null) {
            	temp.add(vector);
            }   
            int numStms = temp.get(0).split(" ").length;
            int numTests = temp.size();
            int[][] m = new int[numTests][numStms];
            int i = 0;
            for(String s : temp) {
            	String[] x = s.split(" ");
            	for(int j = 0; j < numStms; j++) {
            		m[i][j] = Integer.parseInt(x[j]);
            	}
            	i++;
            }
            /*for(int j = 0; j < numTests; j++) {
            	System.out.println(Arrays.toString(m[j]));
            }*/
            // Always close files.
            bufferedReader.close();
            return m;
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		return null;
	}

}
