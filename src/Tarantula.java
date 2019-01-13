import java.util.Arrays;

public class Tarantula {
	
	private int[][] mat;
	private int numStms;
	private int numTests;
	
	public Tarantula(int[][] mat) {
		this.mat = mat;
		numStms = mat[0].length - 1;
		numTests = mat.length;
	}	
	
	public int passed(int s) {
		int p = 0;
		for(int i = 0; i < mat.length; i++) {
			if(mat[i][numStms] == 0 && mat[i][s] == 1) p++;
		}
		return p;
	}
	
	public int failed(int s) {
		int f = 0;
		for(int i = 0; i < mat.length; i++) {
			if(mat[i][numStms] == 1 && mat[i][s] == 1) f++;
		}
		return f;
	}
	
	public int totalPassed() {
		int p = 0;
		for(int i = 0; i < mat.length; i++) {
			p += (mat[i][numStms] == 0) ? 1 : 0;
		}
		return p;
	}
	
	public int totalFailed() {
		int f = 0;
		for(int i = 0; i < mat.length; i++) {
			f += mat[i][numStms];
		}
		return f;
	}
	
	public Statement[] getScores() {
		Statement[] s = new Statement[numStms];
		int tp = totalPassed();
		int tf = totalFailed();
		for(int i = 0; i < numStms; i++) {
			double p = (tp == 0) ? 0 : (0.0 + passed(i)) / tp;
			double f = (tf == 0) ? 0 : (0.0 + failed(i)) / tf;
			// if the coverage of a statement s is 0 (passed(s) + failed(s) = 0), its suspiciousness score should be 0. 
			// If we use the formula for hue(s), then the suspiciousness score will become 1 because s was not covered by any passed test cases
			// However, the reason why it was not covered is not because it is faulty, it is because it is never covered by any test cases
			// that is why we need to set the score to 0 if the coverage is 0, i.e. the hue is 1
			// some programs may unintentionally contain lines that are never covered, e.g. an if statement's condition is never true
			// these lines tell us nothing about the location of the bug
			// therefore, they are harmless, i.e. even though they were never covered, their hue should not be red
			// the hue should not indicate the coverage but should indicate the likelihood that a statement is harmless
			// in this case, statements that are not covered should have a hue of 1, i.e. green
			double score = (p + f) == 0 ? 0 : 1 - p / (p + f);
			s[i] = new Statement(i + 1, score);
		}
		Arrays.sort(s);
		
		int rank = 0;
		int j = 1;
		double prevScore = s[0].getScore();
		for(int i = 1; i < s.length; i++) {
			if(Math.abs(s[i].getScore() - prevScore) >= 0.000001) {
				rank += j;
				for(int k = i - 1; k > i - j - 1; k--) {
					s[k].setRank(rank);
					//System.out.println(s[k]);
				}
				j = 1;
			} else j++;
			prevScore = s[i].getScore();
		}
		for(int k = s.length - 1; k > s.length - j - 1; k--) {
			s[k].setRank(rank + j);
		}
		return s;
	}
	
	public void printScores() {
		Statement[] s = getScores();
		System.out.println("Index \tSuspicion score Rank");
		for(int i = 0; i < s.length; i++) {
			System.out.println(s[i]);
		}
	}
	
	public void setMatrix(int[][] m) {
		this.mat = m;
	}
}