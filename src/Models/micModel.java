package Models;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Graph.loadGraph;
import Graph.simpleVertex;
import MIMA.mimaGraph;


public class micModel {
	private Map<Integer, Set<Integer>> time2ActNodes = new HashMap<Integer, Set<Integer>>(); //time t -> activated nodes of time t
	private int MC = 1000; //default iteration
	
	public void micProcess(mimaGraph g, Set<Integer> seeds) // mic process record at time2ActNodes table
	{
		randomProcess r = new randomProcess();
		Set<Integer> activatedNodes = new HashSet<Integer>();
		Set<Integer> newlyActivatedNodes = new HashSet<Integer>();
		int time = 0;
		
		newlyActivatedNodes.addAll(seeds);
				
		while(!newlyActivatedNodes.isEmpty())
		{
			Set<Integer> s = new HashSet<Integer>();
			s.addAll(newlyActivatedNodes);
			this.time2ActNodes.put(time++, s);
			activatedNodes.clear();
			for(int v : newlyActivatedNodes)
				if(g.containVertex(v)&&g.vertex(v).wantRetweet())
					activatedNodes.add(v);
			
			newlyActivatedNodes = r.randNbrs(g, activatedNodes);
			
		}
		
	}
	
	public int infSize() //influence size
	{
		int total = 0;
		for(Set<Integer> s : this.time2ActNodes.values())
			total += s.size();
		return total;
	}
	
	public int infSizeAtTime(int t)
	{
		return this.time2ActNodes.get(t).size();
	}
	
	public void printActResult(int lines, String type)
	{
		if(this.time2ActNodes.size() <= lines)
			lines = this.time2ActNodes.size();
		for(int i = 0; i < lines; i++)
			if(type.toLowerCase().equals("set"))
				System.out.println(i+"="+this.time2ActNodes.get(i));
			else if(type.toLowerCase().equals("size"))
				System.out.println(i+"="+this.time2ActNodes.get(i).size());
			else
			{
				System.out.println("unknown type");
				break;
			}
	}
	
	public double avgMic(int num, Set<Integer> seeds, mimaGraph g)
	{
		double avg = 0.0;
		for(int i = 0; i < num; i++)
		{
			this.micProcess(g, seeds);
			avg += (double)this.infSize();
			this.time2ActNodes.clear();
			g.vertexReset();
		}
		return avg/(double)num;
	}
	
	public double avgMic(Set<Integer> seeds, mimaGraph g)
	{
		double avg = 0.0;
		for(int i = 0; i < this.MC; i++)
		{
			this.micProcess(g, seeds);
			avg += (double)this.infSize();
			this.time2ActNodes.clear();
			g.vertexReset();
		}
		return avg/(double)this.MC;
	}
	
	public void test()
	{
		mimaGraph graph = new mimaGraph();
		loadGraph load = new loadGraph();
		load.readData(graph, "Brightkite_edges.txt", 0, "\t");
		load.wcProb(graph);
		graph.setUniformShareProb(0.9);
		
		int[] seed = new int[] {29024, 16518, 35558, 34792, 33833, 8201, 1517, 10382, 33806, 23728, 35026, 84, 19380, 24310, 34295, 2106, 17114, 4698, 827, 16188};
		Set<Integer> seedSet = new HashSet<Integer>();
		for(int s : seed)
			seedSet.add(s);
		seedSet = graph.read2Seed("BrightkiteSeed.txt");
		
		System.out.println("avg="+this.avgMic(1000, seedSet, graph));
		this.printActResult(10, "set");
	}
	
	
	public Set<Integer> greedyMIC(int k, String dataset, String cascadeProb, double retweetAgainProb)
	{
		mimaGraph graph = new mimaGraph();
		loadGraph load = new loadGraph();
		load.readData(graph, dataset, 1, "\t");
		if(cascadeProb.toLowerCase().contains("w"))
			load.wcProb(graph);
		else if(cascadeProb.toLowerCase().contains("un"))
			load.uniformProb(graph, 0.3);
		graph.setUniformShareProb(retweetAgainProb);
		
		/////////////////////////////
		System.out.println("This is greedy algorithm in MIC model.");
		System.out.println("Data: "+dataset+"\tcascade: "+ cascadeProb + "\tretweet probability: " + retweetAgainProb);
		Set<Integer> seed = new HashSet<Integer>();
		
		sortedVertices sv = new sortedVertices();

		for(int i = 0; i < k; i++)
		{
			for(int id : graph.getAllIDs())
			{
				if(seed.contains(id))
					continue;
				seed.add(id);
				sv.add(new simpleVertex(id, this.avgMic(seed, graph)));
				seed.remove(id);
			}
			seed.add(sv.max().getID());
			System.out.print(sv.max().getID()+" ");
			sv.clear();
		}
		
		return seed;
	}
	
	
	
	public static void main(String[] args) {
		
		//new micModel().test();
		new micModel().greedyMIC(100, "hep.txt", "wc", 0.1);
		
	}

}
