import java.text.MessageFormat;

public class Statement implements Comparable<Statement> {
	
	private int index;
	private double score;
	private int rank;
	
	public Statement(int index, double score) {
		this.index = index;
		this.score = score;
		this.rank = -1;
	}
	
	public int getIndex() {
		return index;
	}
	
	public double getScore() {
		return score;
	}
	
	public String toString() {
		return String.format("S%d\t%.6f\t%s", index, score, (rank == -1) ? "unknown" : rank + "");
	}
	
	public void setRank(int r) {
		this.rank = r;
	}
	
	public boolean equals(Object o) {
		Statement s = (Statement) o;
		return s.getScore() - getScore() < 0.000001;
	}

	@Override
	public int compareTo(Statement o) {
		if(Math.abs(o.getScore() - getScore()) < 0.000001) return 0;
        return (getScore() - o.getScore() < 0) ? 1 : -1;
	}
}
