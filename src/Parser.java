import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static class deliverable{
        public int[][] matrix;
        public int[] lines;
    }

    public static deliverable constructMatrix(String programName, String outputFile) throws IOException{
		String current_dir = System.getProperty("user.dir");
		FileWriter fileWriter = new FileWriter(current_dir+"\\" + outputFile);
        deliverable return1 = new deliverable();
        int test_number = 1;
        String dirs[] = new String[test_number];
        for(int i=1;i<=test_number;i++) {
            String output_dir = current_dir+"\\line_coverage\\t" + i + "\\.empty\\.classes";
            dirs[i-1] = output_dir;
        }
        int[][] result_matrix ={};
        for(int counter=0;counter<test_number;counter++) {
            File file = new File(dirs[counter] + "\\" + programName);
            int i = 0;
            String lines[] = new String[1000];
            int j = 0;
            int number_lines = 0;
            Scanner sc = null;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (sc.hasNextLine())
                lines[j++] = sc.nextLine();
            for (int k = 0; k < lines.length; k++) {
                if (lines[k] != null && lines[k].startsWith("<b class")) {
                    number_lines++;
                }
            }
            result_matrix = new int[test_number][number_lines + 1];
            //System.out.println("There are " + number_lines + " in total");
            int[] line_number = new int[number_lines];
            int index = 0;
            for (int k = 0; k < lines.length; k++) {
                if (lines[k] != null && lines[k].startsWith("<b class")) {
                    final Pattern pattern = Pattern.compile("<i>(.+?)</i>");
                    final Matcher matcher = pattern.matcher(lines[k]);
                    matcher.find();
                    line_number[index++] = Integer.valueOf(matcher.group(1));
                }
            }
            return1.lines=line_number;
            int counter2 = 0;
            for (int k = 0; k < lines.length; k++) {
                if (lines[k] != null && lines[k].startsWith("<b class")) {
                    if (lines[k].contains("<b class=\"ncp\">") || lines[k].contains("<b class=\"fcp\">")) {
                        result_matrix[counter][number_lines] = 0;     //passed test cases: 0
                    } else
                        result_matrix[counter][number_lines] = 1;
                    final Pattern pattern = Pattern.compile("<i>(.+?)</i>");
                    final Matcher matcher = pattern.matcher(lines[k]);
                    matcher.find();
                    if (lines[k].contains("<b class=\"nc")) {
                        //System.out.println(matcher.group(1) + "th line is not covered");
                        result_matrix[counter][counter2] = 0;
                    } else {
                        //System.out.println(matcher.group(1) + "th line is covered");
                        result_matrix[counter][counter2] = 1;
                    }
                    counter2++;
                } else
                    continue;
            }
            
			for (int p = 0; p < number_lines + 1; p++) {
				try {
					fileWriter.write(result_matrix[counter][p] + " ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
			try {
				fileWriter.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		fileWriter.close();
        return1.matrix=result_matrix;
        return return1;
    }

    public static void main(String args[]) throws IOException {
    	System.out.print("executing..."+"\n");
        constructMatrix("Numbercompare.html", "NumberCompare.txt");
        System.out.print("finished");
    }
}
