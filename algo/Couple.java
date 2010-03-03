package algo;



public class Couple {

	public CaseTemplate first;
	public CaseTemplate second;
	
	public Couple(CaseTemplate first, CaseTemplate second) {
		
		this.first = first;
		this.second = second;
	}
	
	public boolean equals(Couple couple) {
		
		return first == couple.first && second == couple.second;
	}
	
	public String toString() {
		
		return "<" + first.toString() + "," + second.toString() + ">";
	}
}
