import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FaultyProgram {
    public static void main(String[] args) {
        // generateData();
        runTarantula("YEAR INFO", "wrongYear10.txt");
		runTarantula("YEAR INFO", "wrongYear50.txt");
		runTarantula("YEAR INFO", "wrongYear100.txt");
		runTarantula("CROSSTAB", "crosstab.txt");
		runTarantula("TARANTULA", "tarantula.txt");
    }
    
    public static void generateData() {
    	// generate coverage matrices
        int[] testCases = {10, 50, 100};

        int maxFailedRatio = 5;
        final int vectorSize = 51;
        int[] c = new int[vectorSize];

        for(int t = 0; t < testCases.length; t++) {
            int numTests = testCases[t];
            int[] years = new int[numTests];

            // deliberately introduce bugs
            HashMap<String, String> bugs = new HashMap<>();
            bugs.put("Buggy", "No");
            bugs.put("Bug Index", "3");
            getYears(years, numTests, maxFailedRatio, bugs);

            String fileName = "wrongYear" + numTests + ".txt";
            String output = "";
            for(int i = 0; i < numTests; i++) {
                Arrays.fill(c, 0);
                coverage(c, years[i]);
                for(int k = 0; k < c.length; k++)
                    output += c[k] + " ";
                output += "\n";
            }
            try {
                FileWriter fileWriter = new FileWriter(fileName);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.print(output);
                printWriter.close();
            } catch (IOException e) {
                System.out.println("Error writing coverage matrix to file");
                System.exit(404);
            }
        }
    }

    public static void coverage(int[] c, int year) {
        //System.out.println(year); 
        c[0]++;
        if(year < 0 && c[1]++ > -1) {
            System.out.println("Invalid year"); c[2]++;
            c[3]++; System.exit(1);
        }
        Map<String, Boolean> map = new HashMap<String, Boolean>(); c[4]++;
        map.put("Leap Year", false); c[5]++;
        map.put("In the Future", false); c[6]++;
        map.put("Winter olympics", false); c[7]++;
        map.put("Summer olympics", false); c[8]++;
        map.put("World cup", false); c[9]++;
        // Leap Year
        if(year % 4 == 0 && c[10]++ > -1) {
            if( year % 100 == 0 && c[11]++ > -1) {
                if (year % 400 != 0 && c[12]++ > -1) {// BUG 1 ==
                    map.replace("Leap Year", true); c[13]++;
                } else if(c[14]++ > -1) {
                    map.replace("Leap Year", false); c[15]++;
                }
            } else if(c[16]++ > -1) {
                map.replace("Leap Year", true); c[17]++;
            }
        } else if(c[18]++ > -1) {
            map.replace("Leap Year", false); c[19]++;
        }
        // In the Future
        int currYear = Calendar.getInstance().get(Calendar.YEAR); c[20]++;
        if(year < currYear && c[21]++ > -1) { // BUG 2 <= (wrong is <)
            map.replace("In the Future", false); c[22]++;
        } else if(c[23]++ > -1) {
            map.replace("In the Future", true); c[24]++;
        }

        // Winter olympics
        int sampleWOlympYear = 2002; c[25]++;
        if(Math.abs(year - sampleWOlympYear) % 4 == 0 && c[26]++ > -1) {
            map.replace("Winter olympics", true); c[27]++;
        } else if (c[28]++ > -1){
            map.replace("Winter olympics", false); c[29]++;
        }

        // Summer olympics
        int sampleSOlympYear = 2020; c[30]++;
        if(Math.abs(year - sampleSOlympYear) % 4 == 0 && c[31]++ > -1) {
            map.replace("Summer olympics", true); c[32]++;
        } else if(c[33]++ > -1){
            map.replace("Summer olympics", false); c[34]++;
        }

        // World cup
        if(!map.get("In the Future") && c[35]++ > -1) {
            if(year < 1930 && c[36]++ > -1) {
                map.replace("World cup", false); c[37]++;
            } else if(c[38]++ > -1){
                if((year == 1939 || year == 1951) && c[39]++ > -1) { // BUG 3! 1938 and 1950
                    map.replace("World cup", true); c[40]++;
                } else if((year - 1930) % 4 == 0 && c[41]++ > -1) {
                    map.replace("World cup", true); c[42]++;
                } else if(c[43]++ > -1) {
                    map.replace("World cup", false); c[44]++;
                }
            }
        } else if(c[45]++ > -1){
            if(map.get("Leap Year") && c[46]++ > -1) {
                map.replace("World cup", true); c[47]++;
            } else if(c[48]++ > -1){
                map.replace("World cup", false); c[49]++;
            }
        }
        Map<String, Boolean> correct = CorrectProgram.getInfo(year);
        int result = 0;
        for (String key : map.keySet()) {
            if(map.get(key) != correct.get(key)) {
                result = 1;
            }
        }
        c[50] = result;
    }

    public static int[] getRandomLeapYears() {
        int[] r = new  int[7];
        for(int i = 0; i < 7; i++) {
            r[i] = 400 + i * 400;
        }
        return r;
    }

    public static void getYears(int[] years, int numTests, int maxFailedRatio, HashMap<String, String> bugs) {
        if(bugs.get("Buggy") == "Yes") {
            int[] randomLP = getRandomLeapYears();
            int ratio = numTests / maxFailedRatio;
            switch(bugs.get("Bug Index")){
                case "1":
                    for(int i = 0; i < ratio; i++) {
                        years[i] = randomLP[(int) Math.floor(Math.random() * randomLP.length)];
                    }
                    break;
                case "2":
                    for(int i = 0; i < ratio; i++) {
                        years[i] = Calendar.getInstance().get(Calendar.YEAR);
                    }
                    break;
                case "3":
                    for(int i = 0; i < ratio; i++) {
                        years[i] = ((int) Math.round(Math.random() * 2) + 1) == 1 ? 1939 : 1951;
                    }
            }
        }
        for(int y = 0; y < numTests; y++)
            if(years[y] < 1) years[y] = (int) Math.floor(Math.random() * 200) + 1918;
    }
    
    public static void runTarantula(String programName, String fileName) {		
		int[][] matrix = getMatrixFrom(fileName);
		System.out.println("______________________________________________________");
		System.out.println(programName + " MATRIX WITH " + matrix.length + " TEST CASES");
		System.out.println("______________________________________________________");
		Tarantula t = new Tarantula(matrix);
		t.printScores();
	}
    
    public static int[][] getMatrixFrom(String fileName) {
		try {
			ArrayList<String> temp = new ArrayList<String>();
            FileReader fileReader = new FileReader(fileName);
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
            
            bufferedReader.close();
            return m;
        } catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        } catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
		return null;
	}
}
