package Models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import Graph.Vertex;
import Graph.loadGraph;
import Graph.simpleGraph;
import Graph.simpleVertex;



public class IndepCascade {

	private int MC = 1000;
	private simpleGraph<Vertex> graph;
	private List<Set<Vertex>> currentActive = new ArrayList<Set<Vertex>>();
	private sortedVertices celfArray = new sortedVertices();
	
	public IndepCascade()
	{
		graph = new simpleGraph<Vertex>();
	}
	
	public simpleGraph<Vertex> getGraph()
	{
		return this.graph;
	}
	
	public void setMC(int iter)
	{
		this.MC = iter;
	}
	
	public Map<Integer, Integer> singleDiffusion(List<Vertex> seed) // directly evaluate
	{
		Random rand = new Random();
		Set<Vertex> active = new HashSet<Vertex>(); // active nodes
		Stack<Vertex> target = new Stack<Vertex>(); // unprocessed nodes
		Map<Integer, Integer> result = new HashMap<Integer, Integer>(); //store the results
		for(Vertex s : seed)
		{
			target.push(s);
			while(target.size() > 0)
			{
				Vertex node = target.pop();
				active.add(node);
				for(Object follower : graph.outNeighbor(node.getID()))
				{
					double randnum = rand.nextDouble();
					if(randnum <= graph.getEdgeProb(node.getID(), ((Vertex)follower).getID()))
						if(!active.contains((Vertex)follower))
							target.push((Vertex)follower);
				}
			}
			result.put(result.size() + 1, active.size());
		}
		return result;
	}
	
	public int setDiffusionResultOfSeed(List<Vertex> seed, int m) // remember m-iteration seed activated results
	{
		Random rand = new Random();
		Set<Vertex> active = this.currentActive.get(m); // active nodes
		Stack<Vertex> target = new Stack<Vertex>(); // unprocessed nodes
		Map<Integer, Integer> result = new HashMap<Integer, Integer>(); //store the results
		for(Vertex s : seed)
		{
			target.push(s);
			while(target.size() > 0)
			{
				Vertex node = target.pop();
				active.add(node);
				for(Object follower : graph.outNeighbor(node.getID()))
				{
					double randnum = rand.nextDouble();
					if(randnum <= graph.getEdgeProb(node.getID(), ((Vertex)follower).getID()))
						if(!active.contains((Vertex)follower))
							target.push((Vertex)follower);
				}
			}
			result.put(result.size() + 1, active.size());
		}
		return active.size();
	}
	
	public double setAllDiffusionResultOfSeed(List<Vertex> seed, int iter) // clear results and remember iter-times precomputed results
	{
		this.currentActive.clear();
		double avg = 0.0;
		for(int i = 0; i < iter; i++)
		{
			this.currentActive.add(new HashSet<Vertex>());
			avg+=(double)this.setDiffusionResultOfSeed(seed, i);
		}
		return avg/(double)iter;
	}
	
	public int addSeedSingleDiffusion(Vertex v, int m) // based on precomputed result and calculate single result
	{
		Random rand = new Random();
		Set<Vertex> active = new HashSet<Vertex>();
		active.addAll(this.currentActive.get(m)); // active nodes (get precalculated results)
		Stack<Vertex> target = new Stack<Vertex>(); // unprocessed nodes
		
		target.push(v);
		while(target.size() > 0)
		{
			Vertex node = target.pop();
			active.add(node);
			for(Object follower : graph.outNeighbor(node.getID()))
			{
				double randnum = rand.nextDouble();
				if(randnum <= graph.getEdgeProb(node.getID(), ((Vertex)follower).getID()))
					if(!active.contains((Vertex)follower))
						target.push((Vertex)follower);
			}
		}
		return active.size();
	}
	
	public double getResult(Vertex v, int iter) // get (based on precomputed) diffusion result
	{
		double value = 0.0;
		for(int i = 0; i < iter; i++)
		{
			value += this.addSeedSingleDiffusion(v, i);
		}
		return value/(double)iter;
	}
	
	public double[] getResult(int iter, List<Vertex> seed)
	{
		double[] avg = new double[10000];
		List<Map<Integer, Integer>> results = new ArrayList<Map<Integer, Integer>>();
		for(int i = 0; i < iter; i++)
		{
			results.add(singleDiffusion(seed)); // iter-times singleDiffusion results 
			for(int j = 1; j <= seed.size(); j++) // sum
				avg[j-1] += results.get(i).get(j);
		}
		
		for(int i = 0; i < seed.size(); i++) // avg
			avg[i] = avg[i]/(double)iter;
		
		return avg;
	}
	
	public double[] getResult(List<Vertex> seed)
	{
		double[] avg = new double[100];
		List<Map<Integer, Integer>> results = new ArrayList<Map<Integer, Integer>>();
		for(int i = 0; i < this.MC; i++)
		{
			results.add(singleDiffusion(seed));
			for(int j = 1; j <= seed.size(); j++)
				avg[j-1] += results.get(i).get(j);
		}
		
		for(int i = 0; i < seed.size(); i++)
			avg[i] = avg[i]/(double)this.MC;
		
		return avg;
	}
	
	public List<Vertex> greedyAlg(int k)
	{
		List<Vertex> seeds = new ArrayList<Vertex>();
		
		for(int i = 0; i < k ; i++)
		{
			this.setAllDiffusionResultOfSeed(seeds, this.MC);
			for(Vertex v : this.graph.getAllVertex()) // put to celf
			{
				if(seeds.contains(v))
					continue;
				
				double seedInfluence = this.getResult(v, this.MC);
				this.celfArray.add(v.getID(), seedInfluence);
				
			}
			this.celfArray.sort();
			System.out.println(this.celfArray);
			seeds.add(new simpleVertex(this.celfArray.max()));
			
			this.celfArray.clear();
			
		}
		System.out.println(seeds);
		return seeds;
	}
	
	public List<Vertex> celfAlg(int k)
	{
		List<Vertex> seeds = new ArrayList<Vertex>();
		
		for(int i = 0; i < k; i++)
		{
			double d = this.setAllDiffusionResultOfSeed(seeds, this.MC);
			if(i == 0)
			{
				for(Vertex v : this.graph.getAllVertex())
				{
					if(seeds.contains(v)) // if v in seeds
						continue;
					double seedInfluence = this.getResult(v, this.MC);
					this.celfArray.add(v.getID(), seedInfluence);
				}
				this.celfArray.sort();
				Vertex v = this.celfArray.get(0); // max
				seeds.add(new simpleVertex(v.getID(), this.getResult(v, this.MC)));
				this.celfArray.remove(0);
			}
			else
			{
				for(int j = 0; j < this.celfArray.size(); j++)
				{
					Vertex v = this.celfArray.get(0);
					if(seeds.contains(v)) // if v in seeds
						continue;
					double seedInfluence = this.getResult(v, this.MC);
					this.celfArray.add(v.getID(), seedInfluence-d); // margin of add seed a
					
					if(this.celfArray.resort()) // true if v is a maximum margin
					{
						seeds.add(new simpleVertex(this.celfArray.get(0).getID(), seedInfluence));
						this.celfArray.remove(0);
						break;
					}
					
				}
			}
		}
		System.out.println(seeds);
		return seeds;
	}
	
	public void objSerialize(String fileName, Object obj)
	{
		try {
			FileOutputStream fs = new FileOutputStream(fileName);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(obj);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void svDeserialize(String fileName)
	{
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				this.celfArray = (sortedVertices)ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testIC()
	{
		loadGraph load = new loadGraph();
		load.readData(this.graph, "hep.txt", 1, " ");
		load.wcProb(this.graph);
		
		/*
		Greedy: [v2119=91.722, v6024=181.277, v37=231.742]
		CELF: [v267=91.234, v6024=180.277, v37=233.544]  Time: 13.288sec.
		
		Brightkite 
		MC:10, celf, [v6677=13452.2], Time: 1759.581sec.
		MC:10, greedy, [v13361=14128.6], Time: 1692.73sec.
		MC:1000, celf, [v8090=13274.741], Time: 146288.193sec.
		*/
		List<Vertex> list = new ArrayList<Vertex>();
		list.add(new simpleVertex(1));
		double startTime = System.currentTimeMillis();
		//System.out.println(this.diffusionTimes(seed, 100));
		//this.celfAlg(1);
		//this.objSerialize("celf", this.celfArray);
		this.svDeserialize("celf");
		//this.greedyAlg(1);
		System.out.println(this.celfArray);
		double endTime = System.currentTimeMillis();
		System.out.println("Time: " + (endTime-startTime)/1000 + "sec.");
	}
	public static void main(String[] args) {
		
		new IndepCascade().testIC();
		
	}

}