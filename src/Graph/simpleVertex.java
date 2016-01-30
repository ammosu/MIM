package Graph;

import java.io.Serializable;


public class simpleVertex implements Vertex, Serializable{
	private static final long serialVersionUID = 1L;
	private int ID = -1;
	private double value = 0.0; 
	
	public simpleVertex()
	{}
	public simpleVertex(simpleVertex v)
	{
		this.ID = v.ID;
		this.value = v.getValue();
	}
	public simpleVertex(int id)
	{
		this.ID = id;
	}
	public simpleVertex(int id, double value)
	{
		this.ID = id;
		this.value = value;
	}
	
	public int getID()
	{
		return this.ID;
	}
	public double getValue()
	{
		return this.value;
	}
	public void setValue(double v)
	{
		this.value = v;
	}
	
	@Override
	public boolean equals(Object o)
	{
		simpleVertex v = (simpleVertex) o;
		if(v.ID == this.ID)
			return true;
		return false;
	}
	@Override 
    public int hashCode() {
        return 41 * (41 + this.ID) + 3;
    }
	
	public int compareTo(Object o)
	{
		simpleVertex v = (simpleVertex) o;
		return (this.value-v.getValue())>0.0? 0:1;
	}
	
	@Override
	public String toString()
	{
		return "v"+this.ID+"="+this.value; 
	}
	public static void main(String[] args) {
		
	}

}
