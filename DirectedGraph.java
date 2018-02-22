
import java.util.*;

public class DirectedGraph<E extends Edge> {


	public DirectedGraph(int noOfNodes) {
		;
	}

	public void addEdge(E e) {
		;
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
  
