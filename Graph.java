/*Author: Allison Delgado
 * May 2020
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Graph implements ActionListener, MouseListener {
	
	JFrame frame = new JFrame();
	GraphPanel panel = new GraphPanel();
	JButton nodeB = new JButton("Node");
	JButton edgeB = new JButton("Edge");
	JTextField labelsTF = new JTextField("A");
	JTextField firstNode = new JTextField("First");
	JTextField secondNode = new JTextField("Second");
	JButton connected = new JButton("Check Connection");
	Container west = new Container();
	Container east = new Container();
	Container south = new Container();
	JTextField salesmanStartTF = new JTextField("A");
	JButton salesmanB = new JButton("Travelling Salesman");
	final int NODE_CREATE = 0;
	final int EDGE_FIRST = 1; //need to click on first circle
	final int EDGE_SECOND = 2; 
	int state = NODE_CREATE;
	char nodeName = 'A';
	char edgeName = '1';
	Node first = null;
	int TotalPathsFound = 0;
	int cheapestPathCost = 0;
	String printablePath = "";
	int a = 0;
	ArrayList<Node> cheapestActualPath = new ArrayList<Node>();
	
	
	//list tracks what nodes are connected
	ArrayList<String> connectedList = new ArrayList<String>();
	
	//list tracks what nodes have been completed
	ArrayList<ArrayList<Node>> completed = new ArrayList<ArrayList<Node>>();
	
	public Graph() { //set up graph
		
		frame.setSize(800,600);
		frame.setLayout(new BorderLayout());
		
		//south container
		south.setLayout(new GridLayout(1,2));
		south.add(salesmanStartTF);
		south.add(salesmanB);
		salesmanB.addActionListener(this);
		frame.add(south, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.CENTER);
		
		//3 buttons (west)
		nodeB.setOpaque(true);
		nodeB.setBackground(Color.BLUE);
		west.setLayout(new GridLayout(3,1));
		west.add(nodeB);
		nodeB.addActionListener(this);
		

		west.add(edgeB);
		edgeB.setOpaque(true);
		edgeB.addActionListener(this);
		edgeB.setBackground(Color.LIGHT_GRAY);
		
		west.add(labelsTF);
		frame.add(west, BorderLayout.WEST);
		panel.addMouseListener(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		//east container
		east.setLayout(new GridLayout(3,1));		
		east.add(firstNode);
		east.add(secondNode);
		east.add(connected);
		connected.addActionListener(this);
		frame.add(east, BorderLayout.EAST);
		
	}
	
	public static void main(String[] args) {
		new Graph();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		System.out.println(e.getX() + "," +e.getY());
		//check state
		if (state == NODE_CREATE) { //add new node
			panel.addNode(e.getX(), e.getY(), labelsTF.getText());
			
			/*
			//to make it easier, nodes autofill letters until Z then reset to A
			if (nodeName < 90) { // 90 = Z
				nodeName++;
			} 
			else {
				nodeName = 65; // 65 = A
			}
			*/
			labelsTF.setText(nodeName + "");
			
			
		}
		else if (state == EDGE_FIRST) { //check to see if you've clicked previous node
			//pass X and Y coord, get back node
			/*if (edgeName > 49) { //57 = 9
				edgeName--;
			}
			else {
				edgeName = 57;
			}
			*/
			//labelsTF.setText(edgeName + "");
			
			
			Node n = panel.getNode(e.getX(), e.getY());
			if (n != null) {
				first = n;
				state = EDGE_SECOND;
				n.setHighlighted(true);
			}
			
		}
		//connect edge to second node
		else if (state == EDGE_SECOND) {
			Node n = panel.getNode(e.getX(), e.getY());
			if (n != null && !first.equals(n)) {
				String s = labelsTF.getText();
				
				//can only have digits in edge labels
				boolean valid = true;
				for (int a = 0; a < s.length(); a++) {
					if(Character.isDigit(s.charAt(a)) == false) {
						valid = false;
					}
				}
				if (valid == true) {
					first.setHighlighted(false);
					//create edge
					panel.addEdge(first, n, labelsTF.getText());
					first = null;
					state = EDGE_FIRST;
				}
				else { // error message
					JOptionPane.showMessageDialog(frame, "Can only have digits in edge labels");
					
				}
			}
		}
		
		frame.repaint(); 
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Node> path = new ArrayList<Node>();
		
		if (e.getSource().equals(nodeB)) { // if node button is selected
			nodeB.setBackground(Color.BLUE);
			edgeB.setBackground(Color.LIGHT_GRAY);
			state = NODE_CREATE;
			panel.stopHighlighting();
			frame.repaint();
			
		}
		if (e.getSource().equals(edgeB)) { // if edge button is selected
			edgeB.setBackground(Color.BLUE);
			nodeB.setBackground(Color.LIGHT_GRAY);
			state = EDGE_FIRST;
		}
		
		//check if 2 nodes are connected
		if (e.getSource().equals(connected)) {
			if (panel.nodeExists(firstNode.getText()) == false) { //check to see if node 1 exists
				JOptionPane.showMessageDialog(frame, "First node is not in graph");
			} 
			else if (panel.nodeExists(secondNode.getText()) == false) { // check to see if node 2 exists
				JOptionPane.showMessageDialog(frame, "Second node is not in graph");
			}
			else {
				Queue queue = new Queue();
				
				connectedList.add(panel.getNode(firstNode.getText()).getLabel());
				ArrayList<String> edges = panel.getConnectedLabels(firstNode.getText());
				for (int a = 0; a < edges.size(); a++) {//initial connection
					queue.enqueue(edges.get(a));
				}
				//find all connections
				while (queue.isEmpty() == false) {
					String currentNode = queue.dequeue();
					if (connectedList.contains(currentNode) == false) {
						connectedList.add(currentNode);
					}
					edges = panel.getConnectedLabels(currentNode);
					for (int a = 0; a < edges.size(); a++) { //check if already in
						if (connectedList.contains(edges.get(a)) == false) {
							queue.enqueue(edges.get(a));	
						}
					}
				}
				if(connectedList.contains(secondNode.getText())) {
					JOptionPane.showMessageDialog(frame, "Connected!");
				}
				else {
					JOptionPane.showMessageDialog(frame, "Not Connected.");	
				}
				
			}
		}
		
		if(e.getSource().equals(salesmanB)) {
			
			/* Algorithm:
			 * 
			 * 1. get Node that comes from text field to find where the path starts
			 * 
			 * 
			 * 2. Recursively call travelling() to find paths
			 * 		creates list of paths that result from starting node
			 * 		if path length = num of nodes, you have completed the path
			 * 		for each completed path, get the value of the edge length, add together
			 * 
			 * 3. Check if all nodes are connected:
			 * 		A. Added nodes are already in nodeList (refer to nodeList.getSize()
			 *      B. Take a path and see if it hits every node in nodeList, else: error
			 * 		
			 * 4. Find shortest path:
			 * 		assign the first valid path to shortestPath
			 * 		keep looping
			 * 		if current path < shortestPath, reassign shortestPath to current path
			 * 		once finished with all paths, print out the path distance value and what the path actually was (copy path (node names) to bestPath)
			 *
			 */
			
			
			//get node that comes from text field
			for (a = 0; a < panel.nodeList.size(); a++) {
				connectedList.add(panel.getNode(salesmanStartTF.getText()).getLabel());
				ArrayList<String> edges = panel.getConnectedLabels(salesmanStartTF.getText()); 
			}
			
			//call travelling() to find paths
			TotalPathsFound = 0;
			path.add(panel.getNode(salesmanStartTF.getText()));
			travelling(panel.getNode(salesmanStartTF.getText()), path, 0);
			System.out.println("TotalPathsFound " + TotalPathsFound);
			for (int i = 0; i < cheapestActualPath.size(); i++) {
				printablePath += cheapestActualPath.get(i).getLabel();
			}
			
				JOptionPane.showMessageDialog(frame, "The cheapest path found costs " + cheapestPathCost + " and is: " + printablePath);	
				
			
		}
	}
	
	
	public void travelling(Node n, ArrayList<Node> path, int total) {
		//depth-first search: find complete path by going as far as you can in graph, 
		//then if you can't find way out, for loop ends, go back to previous call of travelling()
		int i = 0;
		if(panel.nodeList.size() == path.size()) {
			System.out.println("Complete path found with cost " + total + ".");
			TotalPathsFound++;
			for (i = 0; i < path.size(); i++) {
				System.out.println(path.get(i).getLabel());
				
				
			}
			//find cheapest path
			
			if (cheapestPathCost == 0) {
				cheapestPathCost = total;
				cheapestActualPath = (ArrayList<Node>) path.clone();
			}
			else {
				if (total < cheapestPathCost) {
					//swap
					cheapestPathCost = total;
					//save the actual path
					cheapestActualPath = (ArrayList<Node>) path.clone();
					
					
				}
			}
			
			
		}
		
		for (i = 0; i < panel.edgeList.size(); i++) {
			Edge e = panel.edgeList.get(i);
			if (e.getOtherEnd(n) != null) {
				if(path.contains(e.getOtherEnd(n)) == false) { // If it's not in the path..
					path.add(e.getOtherEnd(n)); // Add to path
					travelling(e.getOtherEnd(n), path, total + Integer.parseInt(e.getLabel())); // Traverse to node
				}
			}
		}
		if (i > 0) {
			path.remove(path.size()-1);
		}
		
	}
}
