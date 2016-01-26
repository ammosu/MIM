package Graph;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class simpleGraph <E> implements Graph {
	private Map<Integer, E> Vertex = new HashMap<Integer, E>(); // vertex information
	
	private Map<Integer, List<Integer>> Links = new HashMap<Integer, List<Integer>>();	// vertex out-links
	private Map<Integer, List<Integer>> rLinks = new HashMap<Integer, List<Integer>>();	// vertex in-links
	private Map<Integer, Edge> linksProb = new HashMap<Integer, Edge>();	// edge probabilities(weight)
	
	@SuppressWarnings("unchecked")
	public void addVertex(int u)
	{
		if(this.Vertex.containsKey(u))
			return;
		this.Vertex.put(u, (E)new simpleVertex(u));
		if(!this.Links.containsKey(u))
			this.Links.put(u, new ArrayList<Integer>());
	}
	
	public void addEdge(int u, int v)
	{
		if(!this.containEdge(u, v))
		{
			if(this.Links.containsKey(u))
				this.Links.get(u).add(v);
			else
			{
				List<Integer> vlist = new ArrayList<Integer>();
				vlist.add(v);
				this.Links.put(u, vlist);
			}
		}
		this.addREdge(u, v);
	}

	protected void addREdge(int u, int v)
	{
		if(!this.rLinks.containsKey(v) || !this.rLinks.get(v).contains(u))
		if(this.rLinks.containsKey(v))
			this.rLinks.get(v).add(u);
		else
		{
			List<Integer> vlist = new ArrayList<Integer>();
			vlist.add(u);
			this.rLinks.put(v, vlist);
		}
	}
	
	public void addEdge(int u, int v, double prob)
	{
		this.addVertex(u);
		this.addVertex(v);
		this.addEdge(u, v);
		this.addREdge(u, v);
		if(prob <= 1.0 && prob > 0.0)
			if(!this.linksProb.containsKey(this.edgeIndex(u, v)))
				this.linksProb.put(this.edgeIndex(u, v), new simpleEdge(u, v, prob));
			
			else if(this.linksProb.get(this.edgeIndex(u, v)).weight()!= prob)
				this.linksProb.get(this.edgeIndex(u, v)).setWeight(prob);
	}
	
	public void setProbs(int u, int v, double p)
	{
		if(this.Vertex.containsKey(u) && this.Vertex.containsKey(v))
			if(this.linksProb.containsKey(this.edgeIndex(u, v)))
			{
				Edge e = this.linksProb.get(this.edgeIndex(u, v));
				e.setWeight(p);
			}
			else
			{
				this.linksProb.put(this.edgeIndex(u, v), new simpleEdge(u, v, p));
			}
	}
	
	public boolean containVertex(int v)
	{
		if(this.Vertex.containsKey(v))
			return true;
		return false;
	}
	
	public boolean containVertex(E v)
	{
		if(this.Vertex.containsValue(v))
			return true;
		return false;
	}

	public boolean containEdge(int u, int v)
	{
		if(this.Links.containsKey(u) && this.Links.get(u).contains(v))
			return true;
		return false;
	}
	
	public boolean containEdge(E u, E v)
	{
		if(this.Links.containsKey(((Vertex)u).getID()) && this.Links.get(((Vertex)u).getID()).contains(((Vertex)v).getID()))
			return true;
		return false;
	}
	
	public double getEdgeProb(int u, int v)
	{
		if(this.Links.containsKey(u) && this.Links.get(u).contains(v))
			return this.linksProb.get(this.edgeIndex(u, v)).weight();
		return 0.0;
	}
	
	public double getEdgeProb(E u, E v)
	{
		if(this.Links.containsKey(((Vertex)u).getID()) && this.Links.get(((Vertex)u).getID()).contains(((Vertex)v).getID()))
			return this.linksProb.get(this.edgeIndex(((Vertex)u).getID(), ((Vertex)v).getID())).weight();
		return 0.0;
	}
	
	public Collection<Edge> getlinks()
	{
		return this.linksProb.values();
	}
	
	public E vertex(int id)
	{
		return this.Vertex.get(id);
	}
	
	public List<Integer> decends(int id)
	{
		if(this.Links.containsKey(id))
			return this.Links.get(id);
		return new ArrayList<Integer>();
	}
	
	public List<Double> decendsProb(int u)
	{
		List<Double> dp = new ArrayList<Double>();
		for(Integer v : this.Links.get(u))
		{
			dp.add(this.linksProb.get(this.edgeIndex(u, v)).weight());
		}
		return dp;
	}

	public List<Integer> pars(int id) // only id
	{
		if(this.rLinks.containsKey(id))
			return this.rLinks.get(id);
		else
			return new ArrayList<Integer>();
	}
	
	public Set<E> inNeighbor(int u)
	{
		Set<E> inNbr = new HashSet<E>();
		if(this.rLinks.containsKey(u))
			for(int v :this.rLinks.get(u))
				inNbr.add(this.vertex(v));
		return inNbr;
	}
	
	public Set<E> outNeighbor(int u)
	{
		Set<E> outNbr = new HashSet<E>();
		for(int v :this.Links.get(u))
			outNbr.add(this.vertex(v));
		return outNbr;
	}
	
	public void clear()
	{
		this.Vertex.clear();
		this.Links.clear();
		this.rLinks.clear();
		this.linksProb.clear();
	}
	
	public void edgeRemove(int u, int v)
	{
		int index = -1;
		if(this.Links.containsKey(u) && this.Links.get(u).contains(v))
		{
			index = this.Links.get(u).indexOf(v);
			this.Links.get(u).remove(index);
		}
		if(index != -1)
			this.linksProb.remove(this.edgeIndex(u, v));
	}
	
	public Set<Integer> getAllIDs()
	{
		return this.Vertex.keySet();
	}
	
	public Collection<E> getAllVertex()
	{
		return this.Vertex.values();
	}
	
	public int randomSeed()
	{
		Random r = new Random();
		int randomSeed = -1;
		int rnum = r.nextInt(this.Vertex.size()), i = 0;
		
		for(Object n : this.Vertex.values())
		{
			if(rnum == i)
			{
				randomSeed = ((Vertex)n).getID();
				rnum = r.nextInt(this.Vertex.size());
				i ++;
				break;
			}
		}
		return randomSeed;
	}
	
	public Set<Integer> randomSeed(int size)
	{
		Set<Integer> randomSet = new HashSet<Integer>();
		while(randomSet.size()<size)
		{
			randomSet.add(this.randomSeed());
		}
		System.out.println(randomSet);
		return randomSet;
	}
	
	public Set<Integer> randomSeed(int size, String path)
	{
		Random r = new Random();
		Set<Integer> randomSet = new HashSet<Integer>();
		int rnum = r.nextInt(this.Vertex.size()), i = 0;
		
		while(randomSet.size() != size)
		{
			for(Object n : this.Vertex.values())
			{
				if(rnum == i)
				{
					randomSet.add(((Vertex)n).getID());
					rnum = r.nextInt(this.Vertex.size());
					i = 0;
					break;
				}
				else
					i++;
			}
		}
		
		System.out.println(randomSet);
		
		FileWriter writer = null;
		BufferedWriter bw = null;
		
		try {
			writer = new FileWriter(path,true);
		
			bw = new BufferedWriter(writer);
			for(int j : randomSet)
			{
				bw.write(String.format("%d", j));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return randomSet;
	}
	
	public Set<Integer> read2Seed(String path)
	{
		Set<Integer> seed = new HashSet<Integer>();
		FileReader fr;
		try {
			fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			try {
				while(br.ready())
					seed.add(Integer.parseInt(br.readLine()));
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return seed;
	}

	public int edgeIndex(int u, int v)
	{
		return 41*u+11*v;
	}
	@Override
	public String toString()
	{
		int index = 0;
		String vs = "", es = "";
		for(Object sv:this.Vertex.values())
		{
			if(5<index++)
				break;
			vs+=sv.toString();
		}
		index = 0;
		for(Edge se:this.linksProb.values())
		{
			if(5<index++)
				break;
			es+=se.toString();
		}
		return vs+"\n"+es;
	}
	public static void main(String[] args) {
		simpleGraph<simpleVertex> graph = new simpleGraph<simpleVertex>();
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
		System.out.println(graph.pars(3));
		System.out.println(graph.decends(3));
		System.out.println(graph);
	}

}
