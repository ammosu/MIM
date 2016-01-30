package Models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Graph.Graph;
import Graph.Vertex;
import Graph.loadGraph;
import Graph.simpleGraph;
import Graph.simpleVertex;
import MIMA.mimaGraph;


public class algs {

	public Map<Integer, Integer> dijkstra(simpleGraph<? extends Vertex> graph, Integer start)  // return previous node
	{
		Map<Integer, Integer> parent = new HashMap<Integer, Integer>();
		Map<Integer, Double> dist = new HashMap<Integer, Double>();
		sortedVertices queue = new sortedVertices();
		
		// initialize distances from s
		for(Integer v : graph.getAllIDs())
			dist.put(v, Double.MAX_VALUE);
		dist.put(start, 0.0);
		parent.put(start, start);
		queue.add(start, dist.get(start));
		
		while(queue.size()!=0)
		{
			Integer minU = -1;
			
			queue.sort();
			minU = queue.get(queue.size()-1).getID(); // minimum distance
			queue.remove(queue.size()-1); // extract minimum
			
			for(int v : graph.decends(minU)) // for each out-link (minU,v) do
			{
				if(dist.get(v) > dist.get(minU) + graph.getEdgeProb(minU, v))
				{
					dist.put(v, dist.get(minU) + graph.getEdgeProb(minU, v));
					parent.put(v, minU);
					queue.add(new simpleVertex(v, dist.get(minU) + graph.getEdgeProb(minU, v)));
				}
			}
		}
		return parent;
	}
	
	public Map<Integer, Double> dijkstraDist(simpleGraph<? extends Vertex> graph, Integer s)  // return previous node
	{
		Map<Integer, Integer> parent = new HashMap<Integer, Integer>();
		Map<Integer, Double> dist = new HashMap<Integer, Double>();
		sortedVertices queue = new sortedVertices();
		
		// initialize distances from s
		for(Integer v : graph.getAllIDs())
			dist.put(v, Double.MAX_VALUE);
		dist.put(s, 0.0);
		parent.put(s, s);
		queue.add(new simpleVertex(s, dist.get(s)));
		
		
		while(queue.size()!=0)
		{
			Integer minU = -1;
			
			queue.sort();
			minU = queue.get(queue.size()-1).getID();
			queue.remove(queue.size()-1); // extract minimum
			
			for(int v : graph.decends(minU)) // for each out-link (minU,v) do
			{
				
				if(dist.get(v) > dist.get(minU) + graph.getEdgeProb(minU, v))
				{
					dist.put(v, dist.get(minU) + graph.getEdgeProb(minU, v));
					parent.put(v, minU);
					queue.add(new simpleVertex(v, dist.get(minU) + graph.getEdgeProb(minU, v)));
				}
			}

		}
		return dist;
	}
	
	public void printDijk(Map<Integer, Double> map)
	{
		Map<Integer, Double> m = new HashMap<Integer, Double>(); 
		for(Entry<Integer, Double> e : map.entrySet())
		{
			if(e.getValue() != Double.MAX_VALUE)
				m.put(e.getKey(), e.getValue());
		}
		System.out.println(m);
	}
	
	public Set<Integer> bfsSet(Graph graph, int root) //exclude s
	{
		List<Integer> fifoQueue = new ArrayList<Integer>();
		Map<Integer, Integer> distance = new HashMap<Integer, Integer>();
		Map<Integer, Integer> par = new HashMap<Integer, Integer>();
		
		fifoQueue.add(root);
		distance.put(root, 0);
		
		while(!fifoQueue.isEmpty())
		{
			Integer v = fifoQueue.remove(0); //dequeue
			
			List<Integer> descends = graph.decends(v);
			
			for(int des : descends)
			{
				if(!distance.containsKey(des))
				{
					distance.put(des, distance.get(v) + 1);
					par.put(des, v);
					fifoQueue.add(des);
				}
			}
		}
		return distance.keySet();
	}
	
	public static void main(String[] args) {
		
		mimaGraph graph = new mimaGraph();
		loadGraph load = new loadGraph();
		load.readData(graph, "hep.txt", 1, " ");
		load.wcProb(graph);
		algs alg = new algs();
		alg.printDijk(alg.dijkstraDist(graph, 4));
		
	}

}
