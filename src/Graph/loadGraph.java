package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import MIMA.mamVertex;
import MIMA.mimaGraph;


public class loadGraph {

	public void readData(simpleGraph<? extends Vertex> g, String network, int skipLines, String sepaStr) // load vertex
	{
		FileReader FileStream;
		try {
			FileStream = new FileReader(network);
			BufferedReader BufferedStream = new BufferedReader(FileStream);
			for(int i = 0; i < skipLines; i++)
				BufferedStream.readLine();
			while(BufferedStream.ready())
			{
				String readline = BufferedStream.readLine();
				if(sepaStr.contentEquals(" ")&&readline.contains("\t"))
					sepaStr = "\t";
				else if(sepaStr.contentEquals("\t")&&readline.contains(" "))
					sepaStr = " ";
				String[] readlines = readline.split(sepaStr); 
				int u = Integer.parseInt(readlines[0]);
				int v = Integer.parseInt(readlines[1]);
				g.addVertex(u);
				g.addVertex(v);
				g.addEdge(u, v);
			}
			BufferedStream.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void readProb(mimaGraph g, String probData) // read probability from file
	{
		FileReader fr;
		try {
			fr = new FileReader(probData);
			BufferedReader br = new BufferedReader(fr);
			try {
				for(mamVertex u : g.getAllmamVertex())
				{
					for(int v : g.decends(u.getID()))
					{
						if(br.ready())
						{
							g.setProbs(u.getID(), v, Double.parseDouble(br.readLine()));
						}
					}
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void uniformProb(mimaGraph g, double p)
	{
		for(mamVertex u : g.getAllmamVertex())
		{
			for(int v : g.decends(u.getID()))
			{
				g.setProbs(u.getID(), v, p);
			}
		}
	}
	
	public void wcProb(simpleGraph<? extends Vertex> g)
	{
		for(Object u : g.getAllVertex())
		{
			for(int v : g.decends(((Vertex)u).getID()))
			{
				g.setProbs(((Vertex)u).getID(), v, (double)1.0/g.pars(v).size());
			}
		}
	}
	
	public static void main(String[] args) {
		mimaGraph graph = new mimaGraph();
		loadGraph load = new loadGraph();
		load.readData(graph, "hep.txt", 1, "\t");
		load.wcProb(graph);
		//load.uniformProb(graph,0.3);
		graph.testGraph();

	}

}
