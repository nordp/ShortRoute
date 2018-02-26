import java.util.*;
import java.lang.*;

public class DirectedGraph<E extends Edge> {

	List<E>[] nodeList;

	public DirectedGraph(int noOfNodes) {
		nodeList = new LinkedList[noOfNodes];
		fillWithEmptyEdgeLists(nodeList);
	}

	public void addEdge(E e) {
		nodeList[e.from].add(e);
		nodeList[e.to].add(e);
	}

	public Iterator<E> shortestPath(int from, int to) {	
		PriorityQueue<KnownPath> p = new PriorityQueue<>(new CompDijkstraPath());
		boolean[] visited = new boolean[nodeList.length];
		p.add(new KnownPath(from, 0, new LinkedList<E>()));
		while(!p.isEmpty()){
			KnownPath q = p.poll();
			if(!visited[q.getNode()]){
				if(q.getNode() == to){
					return q.getPath().iterator();
				}
				else{
					visited[q.getNode()] = true;
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
		return null;
	}

	public Iterator<E> minimumSpanningTree() {
		List<E>[] cc = new LinkedList[nodeList.length];
		fillWithEmptyEdgeLists(cc);

		PriorityQueue<E> pq = new PriorityQueue(new CompKruskalEdge());
		for (int i = 0 ; i < nodeList.length; i++){
			for(E e : nodeList[i]){
				pq.add(e);
			}
		}

		int ccSize = cc.length;

		while (!pq.isEmpty() && ccSize > 1){
			E edge = pq.poll();

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
		// KRUSKAL
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
  
