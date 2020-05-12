
public class Edge {
//edge has two nodes that are connection points
	
	Node first;
	Node second;
	String label;
	
	public Edge(Node newfirst, Node newsecond, String newlabel) {
		first = newfirst;
		second = newsecond;
		label = newlabel;
	}
	
	public Node getOtherEnd(Node n) {
		//check if first, second, or neither
		if (first.equals(n)) {
			return second;
		} else if (second.equals(n)) {
			return first;
		} else {
			return null;
		}
	}
	
	public Node getFirst() {
		return first;
	}
	public void setFirst(Node first) {
		this.first = first;
	}
	public Node getSecond() {
		return second;
	}
	public void setSecond(Node second) {
		this.second = second;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
