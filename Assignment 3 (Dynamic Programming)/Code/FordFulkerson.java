import java.util.*;
import java.io.File;

public class FordFulkerson {

	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> path = new ArrayList<Integer>();
		boolean[] isVisited = new boolean[graph.getNbNodes()];	// Keeps track of the visited nodes
		Stack<Integer> stk = new Stack<Integer>();
		stk.push(source);
		
		//path.add(source);
		pathBuild(source, destination, graph, path, stk, isVisited);
		
		//pathBuild(source, destination, graph, isVisited, path);
		
		return path;
	}
	
	public static void pathBuild(int s, int d, WGraph g, ArrayList<Integer> path, Stack<Integer> stk, boolean[] isVisited){
		boolean added = false;
		//System.out.println("Visiting node " + s);
		//System.out.println("Current Graph: \n" + g.toString());
		
		if(!isVisited[s]) {
			path.add(s);
			isVisited[s] = true;
			//System.out.println("Added node " + s + " to path");
			//System.out.println("Current Path: " + path.toString());
		}
		
		if (s == d) return;
		
		//System.out.println("Adding edges");
		for (Edge e: g.getEdges()) {
			if(e.weight != 0) {
				if (e.nodes[0] == s) {
					if(!isVisited[e.nodes[1]]) {
						//System.out.println("Pushed node " + e.nodes[1] + " on to stack");
						added = true;
						stk.push(e.nodes[1]);
					}
				}
			}
		}
			
			//System.out.println("Current Stack: " + stk);
			
			if(added) {
				//System.out.println("Did push nodes");
				if(!stk.empty()) pathBuild(stk.peek(), d, g, path, stk, isVisited);
			}
			else {
				//System.out.println("No pushes");
				//System.out.println("Removing node " + s + " from path");
				//System.out.println("Poping node " + s +" from stack");
				if(!path.isEmpty()) path.remove((Integer) s);
				if(!stk.empty()) stk.pop();
				if(!stk.empty()) pathBuild(stk.peek(), d, g, path, stk, isVisited);
			}
		}

	public static String fordfulkerson( WGraph graph){
		String answer="";
		int maxFlow = 0;
		
		WGraph risGraph = new WGraph(graph);
		for (Edge e : graph.getEdges()) {
			Edge backEdge = graph.getEdge(e.nodes[1], e.nodes[0]);
			if (backEdge == null) {
				risGraph.addEdge(new Edge(e.nodes[1], e.nodes[0],0));
			}
		}
		
		for (Edge e : graph.getEdges()) {
			graph.setEdge(e.nodes[0], e.nodes[1], 0);
		}
		
		
		//System.out.println("Risigual Graph: \n" + risGraph);
		
		//System.out.println("Flow Graph: \n" + graph);
		
		maxFlow = findMaxFlow(maxFlow, graph, risGraph);
		
		answer += maxFlow + "\n" + graph.toString();	
		return answer;
	}
	
	public static int findMaxFlow(int maxFlow, WGraph graph, WGraph rg) {
		ArrayList<Integer> path = pathDFS(rg.getSource(), rg.getDestination(), rg);
		//System.out.println("Current path: "+ path);
		if (path.isEmpty()) return maxFlow;
		
		int bNeck = -1;
		
		Object[] pathArr = path.toArray();
		
		for(int i=0; i<pathArr.length;i++) {
			//System.out.println("Calculating bottle neck, current index: " + i + " current element: " + (int)pathArr[i]);
			if((int)pathArr[i]!= rg.getDestination()) {
					Edge rgEdge = rg.getEdge((int) pathArr[i], (int) pathArr[i+1]);
					int w = rgEdge.weight;
					if (bNeck == -1) bNeck = w;
					else if (w <= bNeck) bNeck = w;
					
					//System.out.println("Checking edge from "+ pathArr[i] + " --> " + (pathArr[i+1]) + "  weight: " + w);
					
					//System.out.println("Bottel Neck: " + bNeck);
			}
		}

		//System.out.println("Current Bottle neck: "+ bNeck);
		maxFlow += bNeck;
		//System.out.println("Current maxFlow: "+ maxFlow);
		
		for(int i=0; i<pathArr.length;i++) {
			if((int)pathArr[i]!= rg.getDestination()) {
				Edge rgEdge = rg.getEdge((int) pathArr[i], (int) pathArr[i+1]);
				//System.out.println("Augmented along "+ pathArr[i] + " --> " + pathArr[i+1] + "  weight: " + rgEdge.weight + " - " + bNeck);
				rg.setEdge((int) pathArr[i], (int) pathArr[i+1],((rgEdge.weight) - bNeck));
				
				Edge gEdge = graph.getEdge((int) pathArr[i], (int) pathArr[i+1]);
				graph.setEdge((int) pathArr[i], (int) pathArr[i+1], ((gEdge.weight) + bNeck));
				
				//System.out.println("Risigual Graph: \n" + rg);
				
				//System.out.println("Flow Graph: \n" + graph);
			}
			if((int)pathArr[i]!= rg.getSource()) {
				Edge rgEdge = rg.getEdge((int) pathArr[i], (int) pathArr[i-1]);
				rg.setEdge((int) pathArr[i], (int) pathArr[i-1],((rgEdge.weight) + bNeck));
				//System.out.println("Augmented along "+ pathArr[i] + " --> " + pathArr[i-1] + "  weight: " + (rgEdge.weight));
				//System.out.println("Risigual Graph: \n" + rg);
				//System.out.println("Flow Graph: \n" + graph);
			}
		}
		
		return findMaxFlow(maxFlow, graph, rg);
	}
	

	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
		 //System.out.println(pathDFS(0,5,g));
	     System.out.println(fordfulkerson(g));
	 }
}

