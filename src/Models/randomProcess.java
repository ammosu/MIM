package Models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Graph.loadGraph;
import MIMA.mimaGraph;


public class randomProcess extends algs {
	private Map<Integer, Set<Integer>> randomGraph = new HashMap<Integer, Set<Integer>>();
	
	public void createRandGraph(mimaGraph g)
	{
		for(int u : g.getAllIDs())
		{
			Random r = new Random();
			for(int v : g.decends(u))
			{
				double edgeProb = g.getEdgeProb(u, v);
				double rand = r.nextDouble();
				if(this.randomGraph.containsKey(u) && rand <= edgeProb) // succeed influence
					this.randomGraph.get(u).add(v);
				else if(!this.randomGraph.containsKey(u) && rand <= edgeProb)
				{
					Set<Integer> s = new HashSet<Integer>();
					s.add(v);
					this.randomGraph.put(u, s);
				}
			}
		}
	}
	
	public Set<Integer> randNbrs(int v)
	{
		Set<Integer> nbrs = new HashSet<Integer>();
		if(this.randomGraph.containsKey(v))
			nbrs = this.randomGraph.get(v);
		
		return nbrs;
	}
	
	public Set<Integer> randNbrs(mimaGraph g, int v)
	{
		Random r = new Random();
		Set<Integer> nbrs = new HashSet<Integer>();
		for(int dec : g.decends(v))
		{
			if(r.nextDouble() < g.getEdgeProb(v, dec))
				nbrs.add(dec);
		}
		return nbrs;
	}
	
	public Set<Integer> randNbrs(mimaGraph g, Set<Integer> set)
	{
		Random r = new Random();
		Set<Integer> nbrs = new HashSet<Integer>();
		for(int v : set)
		{
			for(int dec : g.decends(v))
			{
				if(r.nextDouble() < g.getEdgeProb(v, dec))
					nbrs.add(dec);
			}
		}
		return nbrs;
	}
	
	public void clearRandGraph()
	{
		this.randomGraph.clear();
	}
	
	public Set<Integer> randBfsSet(int root) /** random influence set from root **/
	{
		List<Integer> fifoQueue = new ArrayList<Integer>();
		Map<Integer, Integer> distance = new HashMap<Integer, Integer>();
		Map<Integer, Integer> par = new HashMap<Integer, Integer>();
		
		fifoQueue.add(root);
		distance.put(root, 0);
		
		while(!fifoQueue.isEmpty())
		{
			Integer v = fifoQueue.remove(0); //dequeue
			
			Set<Integer> descends = new HashSet<Integer>();
			if(this.randomGraph.containsKey(v))
				descends = this.randomGraph.get(v);
			
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
	
	public Set<Integer> randBfsSet(Set<Integer> roots)
	{
		Set<Integer> infSet = new HashSet<Integer>();
		for(int root : roots)
			if(!infSet.contains(root))
				infSet.addAll(this.randBfsSet(root));
		return infSet;
	}
	
	public Set<Integer> randInfSet(mimaGraph g, int root)
	{
		this.createRandGraph(g);
		Set<Integer> s = this.randBfsSet(root);
		this.clearRandGraph();
		return s;
	}
	
	public Set<Integer> randInfSet(mimaGraph g, Set<Integer> roots)
	{
		this.createRandGraph(g);
		Set<Integer> s = this.randBfsSet(roots);
		this.clearRandGraph();
		return s;
	}
	
	public double avgInfNumber(int iter, mimaGraph g, Set<Integer> roots)
	{
		double avg = 0.0;
		for(int i = 0; i < iter; i++)
		{
			avg += (double)this.randInfSet(g, roots).size();
		}
		return avg / (double) iter;
	}
	
	public static void main(String[] args) {
		mimaGraph graph = new mimaGraph();
		loadGraph load = new loadGraph();
		load.readData(graph,"hep.txt", 1, " ");
		load.wcProb(graph);
		randomProcess r = new randomProcess();
		
		// seed
		int[] seed = new int[] {3,4,5,33,8,60};
		Set<Integer> seedSet = new HashSet<Integer>();
		for(int s:seed)
			seedSet.add(s);
		
		// average influential node size and execution time
		double startTime = System.currentTimeMillis();
		System.out.println("avg="+r.avgInfNumber(100, graph, seedSet));
		double endTime = System.currentTimeMillis();
		System.out.println("Time: " + (endTime-startTime)/1000 + "sec.");
	}

}
