import java.util.*;

public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException{
        public PathDoesNotExistException(String str){
            super(str);
        }
    }

    BellmanFord(WGraph g, int source) throws NegativeWeightException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         */
    	
    	this.source = source;
    	this.distances = new int[g.getNbNodes()];
    	this.predecessors = new int[g.getNbNodes()];
    	
    	
    	boolean nCycle = false;
    	
		for (int i = 0; i < g.getNbNodes(); i++) {
			distances[i] = Integer.MAX_VALUE;
			predecessors[i] = -1;
		}
		distances[source] = 0;
		
		
		for (int i = 1; i < g.getNbNodes(); i++) {
			//System.out.println("Itaration # " + i);
			for(Edge e:g.getEdges()) {
				int u = e.nodes[0];
				int v = e.nodes[1];
				int weight = e.weight;
				
				//System.out.println("Current edge: " + u + " --> " + v);
				
				if (distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
					//System.out.println("RELAXED\nDistance for "+ v + " " + (distances[u] + weight));
					//System.out.println("Predecessors for "+ v + " " + u);
					distances[v] = ((int) distances[u] + weight);
					predecessors[v] = u;
				}
			}
			
//			System.out.print("Distances:\n[ ");
//			for(int a : distances) {
//				System.out.print(a + ", ");
//			}
//			
//			System.out.print(" ]\n Predecessors\n[ ");
//			for(int a : predecessors) {
//				System.out.print(a + ", ");
//			}
//			System.out.print(" ]\n");
		}
		
		for(Edge e:g.getEdges()) {
			int u = e.nodes[0];
			int v = e.nodes[1];
			int weight = e.weight;
			
			if (distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
				throw new NegativeWeightException("Graph contain negative cycle. For edge " + u + " --> " + v);
			}
		}
		
//		System.out.print("Distances:\n[ ");
//		for(int a : distances) {
//			System.out.print(a + ", ");
//		}
//		
//		System.out.print(" ]\n Predecessors\n[ ");
//		for(int a : predecessors) {
//			System.out.print(a + ", ");
//		}
//		System.out.print(" ]\n");
//		System.out.println("Graph:\n" + g);
    }

    public int[] shortestPath(int destination) throws PathDoesNotExistException{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Error is thrown
         */
    	
    	int current = destination;
    	boolean cont = true;
    	ArrayList<Integer> rtn = new ArrayList<Integer>();
//    	System.out.println("Finding path to " + destination + "\n \n");
//    	System.out.println("Finding path to " + this.source);
    	
    	while(cont) {
    		//System.out.println("Current node: " + current);
    		//&& (predecessors[current] != -1 && current != source)
    		if (distances[current] != Integer.MAX_VALUE) {
    			rtn.add(current);
    			//System.out.print("Reverse path:\n[ ");
    			for(int a : rtn) {
    				//System.out.print(a + ", ");
    			}
    			//System.out.print(" ] \n");
    			if(current == source) {
    				cont = false;
    			}
    			else {
    				current = predecessors[current];
    			}
    		}
    		else {
    			throw new PathDoesNotExistException("No path to " + destination);
    		}
    	}
    	
    	//System.out.println("Done with path");
    	
    	int[] rtnArray = new int[rtn.size()];
    	
    	for(int i=0; i < rtn.size(); i++) {
    		rtnArray[i] = rtn.get(rtn.size()-(i+1));
    	}
//    	System.out.print("Final Path:\n[ ");
//		for(int a : rtnArray) {
//			System.out.print(a + ", ");
//		}
//		System.out.print(" ] \n");
        return rtnArray;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (Exception e){
            System.out.println(e);
        }

   } 
}

