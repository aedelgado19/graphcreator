import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphPanel extends JPanel{
	
	ArrayList<Node> nodeList = new ArrayList<Node>();
	ArrayList<Edge> edgeList = new ArrayList<Edge>();
	ArrayList<Node> conNodeList = new ArrayList<Node>();
	int circleRadius = 20;
	
	ArrayList<ArrayList<Boolean>> adjacency = new ArrayList<ArrayList<Boolean>>();
	
	public GraphPanel() {
		super();
		
	}
	
	public ArrayList<String> getConnectedLabels(String label){
		ArrayList<String> toReturn = new ArrayList<String>();
		int b = getIndex(label);
		for (int a = 0; a < adjacency.size(); a++) {
			if ((adjacency.get(b).get(a) == true) && (nodeList.get(a).getLabel().equals(label) == false )) {
				//add to array list
				toReturn.add(nodeList.get(a).getLabel());
			}
		}
		return toReturn;
	}
	
	public ArrayList<Edge> getConnectedEdges(Node n){
		ArrayList<Edge> connectedEdgesList = new ArrayList<Edge>();
		int b = getIndex(n.getLabel());
		for (int a = 0; a < edgeList.size(); a++) {
			if ((adjacency.get(b).get(a) == true) && (nodeList.get(a).getLabel().equals(n.getLabel()) == false )) {
			connectedEdgesList.add(edgeList.get(a));
			} 
		}
		return connectedEdgesList;
	}
	
	public void printAdjacency() {
		System.out.println();
		for (int i = 0; i < adjacency.size(); i++) {
			for (int j = 0; j < adjacency.size(); j++) {
				System.out.println(adjacency.get(i).get(j)+ "\t");
			}
			System.out.println();
		}
	}
	
	public Node getNode(int x, int y) {
		for (int i = 0; i < nodeList.size(); i++) { //in certain range of circle
			Node node = nodeList.get(i);
			//check if distance is less than radius of circle using pythag. thm
			double radius = Math.sqrt(Math.pow(x-node.getX(), 2) + Math.pow(y-node.getY(), 2));
			if (radius < circleRadius) {
				return node;
			}
		}
		return null;
	}
	
	public Node getNode(String s) {
		for (int i = 0; i < nodeList.size(); i++) {
			
			Node node = nodeList.get(i);
			if (s.contentEquals(node.getLabel())) {
				return node;
			}
		}
		return null;
	}
	
	public int getIndex(String s) {
		for (int i = 0; i < nodeList.size(); i++) {
			
			Node node = nodeList.get(i);
			if (s.contentEquals(node.getLabel())) {
				return i;
			}
		}
		return -1;
	}
	
	public void addEdge (Node first, Node second, String newlabel) {
		edgeList.add(new Edge(first, second, newlabel));
		
		boolean result;
		//check first node then second
		int firstIndex = 0;
		int secondIndex = 0;
		for (int i = 0; i < nodeList.size(); i++) {
			if (first.equals(nodeList.get(i))) {
				firstIndex = i;
			}
			if (second.equals(nodeList.get(i))) {
				secondIndex = i;
			}
			
			
		}
		
		for (int i = 0; i < conNodeList.size(); i++) {
			if (first.equals(conNodeList.get(i))) {
				result = conNodeList.remove(conNodeList.get(i));
			}
			
		}
		//result = conNodeList.remove(0);
		result = conNodeList.remove(getNode(first.getLabel()));
		System.out.println("Result was " + result);
	//	conNodeList.remove(second);
/*		
		for (int j = 0; j < conNodeList.size(); j++) {
			System.out.println("contents of conNodeList pre removal: " + conNodeList.get(j).getLabel());
			conNodeList.remove(conNodeList.get(j));

			
		}
*/
		for (int j = 0; j < conNodeList.size(); j++) {
			System.out.println("contents of conNodeList post removal: " + conNodeList.get(j).getLabel());
		}
		
		
		adjacency.get(firstIndex).set(secondIndex, true);
		adjacency.get(secondIndex).set(firstIndex, true);
		printAdjacency();
		
	}
	
	public boolean nodeExists(String s) {
		for (int i = 0; i < nodeList.size(); i++) {
			if (s.equals(nodeList.get(i).getLabel())) {
				return true;
			}
		}
		return false;
		
	}
	
	
	public void addNode (int newX, int newY, String newlabel) {
		nodeList.add(new Node(newX, newY, newlabel));
		conNodeList.add(new Node(newX, newY, newlabel));
		
		//update rows and columns
		adjacency.add(new ArrayList<Boolean>());
		for (int i = 0; i < adjacency.size(); i++) {
			adjacency.get(i).add(false);
		}
		
		for (int i = 0; i < adjacency.size(); i++) {
			adjacency.get(adjacency.size()-1).add(false); //each time new node is added, add new column and fill with # rows
		
		}	
		printAdjacency();
		
		
	
	}
	
	public void paintComponent(Graphics g) {//drawing area
		super.paintComponent(g);
		
		
		for (int i = 0; i < nodeList.size(); i++) { //-20 is to center the coords of circle
			
			if(nodeList.get(i).getHighlighted() == true) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLACK);
			}
			g.drawOval(nodeList.get(i).getX() - circleRadius, 
					nodeList.get(i).getY() - circleRadius, circleRadius*2, circleRadius*2);
			//draw label:
		
			g.drawString(nodeList.get(i).getLabel(), nodeList.get(i).getX(), nodeList.get(i).getY());
			
		}
		
		for (int i = 0; i < edgeList.size(); i++) { //-20 is to center the coords of circle
			g.setColor(Color.BLACK);
			g.drawLine(edgeList.get(i).getFirst().getX(),
						edgeList.get(i).getFirst().getY(), 
						edgeList.get(i).getSecond().getX(),
						edgeList.get(i).getSecond().getY());
			int fx = edgeList.get(i).getFirst().getX();
			int fy = edgeList.get(i).getFirst().getY();
			int sx = edgeList.get(i).getSecond().getX();
			int sy = edgeList.get(i).getSecond().getY();
			
			
			//find minimum, get difference between 2 (Math.abs), then /2
			g.drawString(edgeList.get(i).getLabel(), 
					Math.min(fx, sx) + (Math.abs(sx - fx) / 2),
					Math.min(fy, sy) + (Math.abs(sy - fy) / 2));
		
			
		}
		
	}

	public void stopHighlighting() {
		for (int a = 0; a < nodeList.size(); a++) {
			nodeList.get(a).setHighlighted(false);
		}
		
	}
	
}
