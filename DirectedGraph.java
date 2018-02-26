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
		PriorityQueue<QueueObject> p = new PriorityQueue<>(new CompDijkstraPath());
		boolean[] visited = new boolean[nodeList.length];
		p.add(new QueueObject(from, 0, new LinkedList<E>()));
		while(!p.isEmpty()){
			QueueObject q = p.poll();
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
							p.add(new QueueObject(edge.to, q.cost + edge.getWeight(), path));
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

	private class CompKruskalEdge implements Comparator<E>{

		@Override
		public int compare(E a, E b){
			if( a == null || b == null){
				throw new NullPointerException();
			}
			if (a.getWeight() == b.getWeight()) return 0;
			return a.getWeight() < b.getWeight() ? -1 : 1;
		}
	}

	private class CompDijkstraPath implements Comparator<QueueObject>{

		public int compare(QueueObject a, QueueObject b){
			if(a == null || b == null){
				throw new NullPointerException();
			}
			if (a.cost == b.cost) return 0;
			return a.cost < b.cost ? -1 : 1;
		}

	}




	//INRE KLASSER
	private class QueueObject{
		private int node;
		private double cost;
		private List<E> path;

		public QueueObject(int node, double cost, List<E> path){
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

		public void setNode(int node){
			this.node = node;
		}

		public void setCost(double cost){
			this.cost = cost;
		}

		public List<E> getPath(){
			return this.path;
		}

	}

	private void fillWithEmptyEdgeLists(List<E>[] list){
		for(int i = 0; i < list.length; i++){
			list[i] = new LinkedList();
		}
	}

}
  
