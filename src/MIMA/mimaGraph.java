package MIMA;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Graph.Edge;
import Graph.simpleGraph;
import Models.probReader;


public class mimaGraph extends simpleGraph <mamVertex> {

	private Map<Integer, mamVertex> vertexInfo = new HashMap<Integer, mamVertex>(); // vertex information
	
	@Override
	public void addVertex(int u)
	{
		if(this.vertexInfo.containsKey(u))
			return;
		this.vertexInfo.put(u, new mamVertex(u));
	}
	
	public int totalWeight()
	{
		int total = 0;
		for(mamVertex m : this.vertexInfo.values())
		{
			total+=m.getNumBuy();
		}
		return total;
	}
	
	public void setUniformRetweetAgainProb(double d)
	{
		for(int id : this.vertexInfo.keySet())
			this.vertexInfo.get(id).setRetweetProb(d);
	}
	
	public void setBuyProb()
	{
		probReader probs = new probReader();
		probs.readFile2Prob("probs"); //read file
		int i = 0;
		
		for(int u : this.vertexInfo.keySet())
		{
			for(int j = 0; j < this.decends(u).size(); j++)
				this.vertexInfo.get(u).addBuyProb(probs.get(i++));
		}
	}
	
	public Set<mamVertex> getAllmamVertex()
	{
		Set<mamVertex> vertice = new HashSet<mamVertex>();
		vertice.addAll(this.vertexInfo.values());
		return vertice;
	}
	
	public mamVertex vertexInfo(int id)
	{
		return this.vertexInfo.get(id);
	}
	
	public void vertexReset()
	{
		for(mamVertex m : this.vertexInfo.values())
		{
			m.resetVertex();
		}
	}
	
	
	@Override
	public String toString()
	{
		int index = 0;
		String vs = "", es = "";
		for(mamVertex mv:this.vertexInfo.values())
		{
			if(5<++index)
				break;
			vs+=mv.toString();
		}
		index = 0;
		for(Edge se:super.getlinks())
		{
			if(5<++index)
				break;
			es+=se.toString();
		}
		return vs+"\n"+es;
	}
	
	public void testGraph()
	{
		mimaGraph graph = new mimaGraph();
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addVertex(5);
		graph.addVertex(8);
		graph.addVertex(6);
		graph.addVertex(10);
		graph.addEdge(2, 3, 0.2);
		graph.addEdge(3, 2, 0.5);
		graph.addEdge(3, 2, 0.6);
		graph.addEdge(3, 8, 0.1);
		graph.addEdge(7, 2, 0.4);
		graph.addEdge(8, 2, 0.02);
		List<Integer> des = new ArrayList<Integer>(), pars = new ArrayList<Integer>();
		des.add(2);des.add(8);pars.add(2);
		System.out.println(graph.vertexInfo.keySet());
		System.out.println(graph.getEdgeProb(3, 2)==0.6);
		System.out.println(graph.decends(3).equals(des));
		System.out.println(graph.pars(3).equals(pars));
		System.out.println(graph);
	}
	public static void main(String[] args) {
		
		new mimaGraph().testGraph();
		
	}

}
