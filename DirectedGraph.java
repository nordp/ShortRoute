import java.util.*;
import java.lang.*;

public class DirectedGraph<E extends Edge> {

	/**
	* This class uses the graph of Gothenburg's tram-map to calculate 
	* the shortest route between different stops and the minimal spanning tree
	* of the complete map.
	* @author (Philip Nord)
	* @author (Jakob Wall)
	* @version (2018)
	*/ 

	/**
	 * An array with lists. Each place in the array represents a bus stop and 
	 * the list on that place contains every bus edge out from this stop.
	 */
	private List<E>[] nodeList;

	/**
	 * Constructor
	 * @param noOfNodes the total number of nodes in the graph.
	 */
	public DirectedGraph(int noOfNodes) {
		nodeList = new LinkedList[noOfNodes];
		fillWithEmptyEdgeLists(nodeList);
	}

	/**
	 * Adds an edge to the graph.
	 * @param e the edge that should be added.
	 */
	public void addEdge(E e) {
		if(e == null){
			return;
		}
		nodeList[e.from].add(e);
		nodeList[e.to].add(e);
	}

	/**
	 * Calculates the shortest path from one place in the graph to the other.
	 * @param from the number of the node we want the path to start in.
	 * @param to the number of the node we want the path to finish in.
	 * @return an iterator with all the edges being used. If there is
	 * no way, null is returned.
	 */
	public Iterator<E> shortestPath(int from, int to) {	

		//A PriorityQueue comparing edges with the compare-method in CompDijkstraPath.
		PriorityQueue<KnownPath> p = new PriorityQueue<>(new CompDijkstraPath());

		//A boolean array with already visited nodes.
		boolean[] visited = new boolean[nodeList.length];

		//Adds the starting node.
		p.add(new KnownPath(from, 0, new LinkedList<E>()));

		//While the PriorityQueue still has elements left.
		while(!p.isEmpty()){
			//Get the shortest Edge.
			KnownPath q = p.poll();
			if(!visited[q.getNode()]){

				//If it's our finish node.
				if(q.getNode() == to){
					return q.getPath().iterator();
				}

				//Else if it's not our finish node.
				else{
					//Add in visited-array.
					visited[q.getNode()] = true;

					//Adds all edges from the added node to the PriorityQueue.
					for (E edge : nodeList[q.getNode()]){
						if(!visited[edge.to]){
							List<E> path = new LinkedList();
							path.addAll(q.path);
							path.add(edge);
							p.add(new KnownPath(edge.to, q.cost + edge.getWeight(), path));
						}
					}
				}
			}
		}

		//If there is no way between the two nodes, return null.
		return null;
	}

	/**
	 * Calculates the minimum spanning tree of a graph.
	 * @return an iterator of the edges connecting all the nodes in a
	 * minimal spanning tree in relation to the weight of the edges.
	 * @throws IllegalStateException if the nodeList is null or empty.
	 */
	public Iterator<E> minimumSpanningTree() {

		if(nodeList == null || nodeList.length == 0){
			throw new IllegalStateException("No nodeList has been populated.");
		}

		//An array containing Lists. Every place in the array represents a node 
		//and the list represents the current subgraph including the node.
		List<E>[] cc = new LinkedList[nodeList.length];
		fillWithEmptyEdgeLists(cc);

		//Adding all the edges into a PriorityQueue which prioritizes 
		//the edges in relation to their weight.
		PriorityQueue<E> pq = new PriorityQueue(new CompKruskalEdge());
		for (int i = 0 ; i < nodeList.length; i++){
			for(E e : nodeList[i]){
				pq.add(e);
			}
		}

		//Local variable keeping track of the amount of actual lists in cc.
		int ccSize = cc.length;

		//While the graph is divided.
		while (!pq.isEmpty() && ccSize > 1){
			//Pick the cheapest edge.
			E edge = pq.poll();

			//If edge leaps between different subgraphs.
			if (cc[edge.from] != cc[edge.to]){
				int shortest = cc[edge.from].size() < cc[edge.to].size() ? edge.from : edge.to;
				int longest = cc[edge.from].size() >= cc[edge.to].size() ? edge.from : edge.to;
				
				cc[longest].addAll(cc[shortest]);

				for(E e : cc[shortest]){
					cc[e.to] = cc[longest];
					cc[e.from] = cc[longest];
				}
				cc[shortest] = cc[longest];
				
				cc[longest].add(edge);
				ccSize--;
			}
		}
		return cc[0].iterator();
	}

	/**
	 * Comparator for edges in Kruskals algorithm
	 */
	private class CompKruskalEdge implements Comparator<E>{

		/**
		 * Compares two edges by getWeight().
		 * @param a an edge to compare to b
		 * @param b an edge to compare to a
		 * @return -1, 0, or 1 depending on a.getWeight() - b.getWeight() is > 0, == 0, or < 0, respectively.
		 */
		@Override
		public int compare(E a, E b){
			if( a == null || b == null){
				throw new NullPointerException();
			}
			if (a.getWeight() == b.getWeight()) return 0;
			return a.getWeight() < b.getWeight() ? -1 : 1;
		}
	}

	/**
	 * Comparator for KnownPath in Dijkstras algorithm
	 */
	private class CompDijkstraPath implements Comparator<KnownPath>{

		/**
		 * Compares two known paths by cost.
		 * @param a a path to compare to b
		 * @param b a path to compare to a
		 * @return -1, 0, or 1 depending on a.cost - b.cost is > 0, == 0, or < 0, respectively.
		 */
		public int compare(KnownPath a, KnownPath b){
			if(a == null || b == null){
				throw new NullPointerException();
			}
			if (a.getCost() == b.getCost()) return 0;
			return a.getCost() < b.getCost() ? -1 : 1;
		}

	}

	/**
	 * A class describing a known path for reaching a node and it's cost.
	 * @param node The node which the path reaches
	 * @param cost The cost of the path reaching the node
	 * @param path A list of edges of the path
	 */
	
	private class KnownPath{
		private int node;
		private double cost;
		private List<E> path;

		public KnownPath(int node, double cost, List<E> path){
			this.node = node;
			this.cost = cost;
			this.path = path;
		}

		public int getNode(){
			return node;
		}

		public double getCost(){
			return cost;
		}

		public List<E> getPath(){
			return path;
		}

	}

	// Help method which fills an array with empty edge lists.
	private void fillWithEmptyEdgeLists(List<E>[] list){
		for(int i = 0; i < list.length; i++){
			list[i] = new LinkedList();
		}
	}

}
  
