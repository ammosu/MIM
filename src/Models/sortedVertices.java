package Models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Graph.Vertex;
import Graph.simpleVertex;


public class sortedVertices {

	private List<simpleVertex> list = new ArrayList<simpleVertex>();
	private boolean isSorted = false;
	
	public void add(simpleVertex v)
	{
		if(this.list.contains(v))
		{
			this.list.remove(v);
		}
		this.list.add(v);
		this.isSorted = false;
	}
	public void add(simpleVertex v, boolean replace)
	{
		if(this.list.contains(v) && replace)
		{
			this.list.remove(v);
			this.list.add(v);
		}
		
		this.isSorted = false;
	}
	public void add(simpleVertex...vertices)
	{
		for(simpleVertex v : vertices)
			this.list.add(v);
		this.isSorted = false;
	}
	public void add(int id, double value)
	{
		this.add(new simpleVertex(id, value));
		this.isSorted = false;
	}
	public List<simpleVertex> getList()
	{
		return this.list;
	}
	public int size()
	{
		return this.list.size();
	}
	public Vertex get(int i)
	{
		return this.list.get(i);
	}
	public Vertex remove(int index)
	{
		return list.remove(index);
	}
	
	public boolean remove(Vertex vertex)
	{
		if(this.list.contains(vertex))
		{
			List<Vertex> set = new ArrayList<Vertex>();
			set.add(vertex);
			this.list.removeAll(set);
			return true;
		}
		return false;
	}
	public void sort()
	{
		if(!this.isSorted)
			Collections.sort(this.list, new Comparator<simpleVertex>() {
				public int compare(simpleVertex v1, simpleVertex v2) {
					return Double.compare(v2.getValue(), v1.getValue());		
				}
	        });
		this.isSorted = true;
	}
	public boolean resort()
	{
		simpleVertex v = this.list.remove(this.list.size()-1);
		if(this.list.size() == 0 || v.getValue()>=this.list.get(0).getValue()) // remain on top
		{
			this.list.add(0, v);
			this.isSorted = true;
			return true;
		}
		else
		{
			for(int i = 0; i < this.list.size(); i++)
				if(v.getValue()>=this.list.get(i).getValue())
				{
					this.list.add(i, v);
					break;
				}
			return false;
		}
	}
	
	public simpleVertex max()
	{
		if(this.isSorted)
			return this.list.get(0);
		return Collections.max(this.list, new Comparator<simpleVertex>() {
            public int compare(simpleVertex v1, simpleVertex v2) {
            	return Double.compare(v1.getValue(), v2.getValue());
            }
        });
	}
	
	public void print()
	{
		System.out.println(this.list.toString());
	}
	
	public void clear()
	{
		this.list.clear();
	}
	@Override
	public String toString()
	{
		String s = "";
		for(int i = 0; i < this.list.size() && i < 10; i++)
		{
			s+=this.list.get(i).toString();
			s+="\n";
		}
		return s;
	}
	
	public static void main(String[] args) {
		sortedVertices sL = new sortedVertices();
		sL.add(new simpleVertex(1,5));
		sL.add(new simpleVertex(2,3));
		sL.add(new simpleVertex(3,4));
		
		sL.sort();
		sL.print();
		sL.add(new simpleVertex(1,3.5));
		sL.print();
		System.out.println(sL.resort());
		sL.print();
		//System.out.println(sL.max());
	}

}
