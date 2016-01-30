package MIMA;
import java.util.ArrayList;
import java.util.List;

import Graph.simpleGraph;
import Models.probReader;


public class mimaGraph extends simpleGraph <mamVertex> {
	
	private boolean hasSetShareProb = false;
	
	public mimaGraph()
	{
		
	}
	
	/*public int totalWeight()
	{
		int total = 0;
		for(mamVertex m : super.getAllVertex())
		{
			total+=m.getNumBuy();
		}
		return total;
	}
	*/
	public void setUniformShareProb(double d)
	{
		for(mamVertex m : super.getAllVertex())
			m.setShareProb(d);
		this.hasSetShareProb = true;
	}
	
	public void setBuyProb()
	{
		probReader probs = new probReader();
		probs.readFile2Prob("probs"); //read file
		int i = 0;
		
		for(mamVertex m : super.getAllVertex())
		{
			for(int j = 0; j < super.outNeighbor(m.getID()).size(); j++)
				m.setShareProb(probs.get(i++));
		}
		this.hasSetShareProb = true;
	}
	
	public boolean hasSetShareProb()
	{
		return this.hasSetShareProb;
	}
/*	public void setBuyProb()
	{
		probReader probs = new probReader();
		probs.readFile2Prob("probs"); //read file
		int i = 0;
		
		for(mamVertex m : super.getAllVertex())
		{
			for(int j = 0; j < super.outNeighbor(m.getID()).size(); j++)
				m.addBuyProb(probs.get(i++));
		}
	}
	*/
	
	public void vertexReset()
	{
		for(mamVertex m : super.getAllVertex())
		{
			m.resetVertex();
		}
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
		System.out.println(graph.getAllVertex());
		System.out.println(graph.getEdgeProb(3, 2)==0.6);
		System.out.println(graph.decends(3).equals(des));
		System.out.println(graph.pars(3).equals(pars));
		System.out.println(graph);
	}
	public static void main(String[] args) {
		
		new mimaGraph().testGraph();
		
	}

}
