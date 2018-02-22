
import java.util.*;

public class DirectedGraph<E extends Edge> {

	List<E>[] nodeList;

	public DirectedGraph(int noOfNodes) {
		nodeList = new List<E>[noOfNodes];
	}

	public void addEdge(E e) {
		if (nodeList[e.from] == null){
			nodeList[e.from] = new LinkedList<>();
		}
		nodeList[e.from].add(e);

		if (nodeList[e.to] == null){
			nodeList[e.to] = new LinkedList<>();
		}
		nodeList[e.to].add(e);
	}

	public Iterator<E> shortestPath(int from, int to) {	
		return CompDijkstraPath.computePath().iterator();
		// DIJKSTRA
	}
		
	public Iterator<E> minimumSpanningTree() {
		return CompKruskalEdge.computeEdges().iterator();
		// KRUSKAL
	}

}
  
