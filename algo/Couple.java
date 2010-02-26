package algo;



public class Couple {

	public StateTemplate first;
	public StateTemplate second;
	
	public Couple(StateTemplate first, StateTemplate second) {
		
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
