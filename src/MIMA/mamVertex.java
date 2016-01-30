package MIMA;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Graph.Vertex;
import Graph.simpleVertex;


public class mamVertex extends simpleVertex {
	private static final long serialVersionUID = 1L;
	//private List<Double> buyProb = new ArrayList<Double>(); //buy probability
	//private int numMessage = 0;
	private double shareProb = 0.0;
	private boolean isRetweeted = false;
	
	public mamVertex(int id)
	{
		super(id);
	}
	
	public mamVertex(int id, double prob)
	{
		super(id);
		this.shareProb = prob;
	}
	
	
	/*
	
	public void addBuyProb(double bp)
	{
		this.buyProb.add(bp);
	}
	public double getBuyProb(int i)
	{
		return this.buyProb.get(i);
	}
	
	public boolean canBuy()
	{
		Random r = new Random();
		if(r.nextDouble() <= this.buyProb.get(this.total))
		{
			this.total++;
			return true;
		}
		return false;
	}
	*/
	public boolean wantRetweet()
	{
		Random r = new Random();
		if(!this.isRetweeted || r.nextDouble() <= this.shareProb)
		{
			this.isRetweeted = true;
			return true;
		}
		return false;
	}
	
	public void resetVertex()
	{
		this.isRetweeted = false;
	}
	
	public void setShareProb(double p)
	{
		this.shareProb = p;
	}
	
	public double shareProb()
	{
		return this.shareProb;
	}
	
	
	public static void main(String[] args) {
				
		List<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(new mamVertex(1));
	}

}
