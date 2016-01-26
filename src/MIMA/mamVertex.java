package MIMA;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Graph.Vertex;
import Graph.simpleVertex;


public class mamVertex extends simpleVertex {

	private List<Double> buyProb = new ArrayList<Double>(); //buy probability
	private int total = 0;
	private double retweetProb = 0.0;
	private boolean isRetweeted = false;
	
	public mamVertex(int id)
	{
		super(id);
	}
	
	public mamVertex(int id, double prob)
	{
		super(id);
		this.retweetProb = prob;
	}
	
	public void setTotalBuy(int num)
	{
		this.total = num;
	}
	public void buy()
	{
		this.total++;
		this.isRetweeted = false;
	}
	public int getNumBuy()
	{
		return this.total;
	}
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
	
	public boolean wantRetweet()
	{
		Random r = new Random();
		if(!this.isRetweeted || r.nextDouble() <= this.retweetProb)
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
	
	public void setRetweetProb(double p)
	{
		this.retweetProb = p;
	}
	
	public double retweetProb()
	{
		return this.retweetProb;
	}
	
	
	public static void main(String[] args) {
				
		List<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(new mamVertex(1));
	}

}
