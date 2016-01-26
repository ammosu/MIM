package Graph;


public class simpleEdge implements Edge {
	
	private int s;
	private int t;
	private double w = 0.0;
	
	public simpleEdge(int s, int t)
	{
		this.s = s;
		this.t = t;
	}
	
	public simpleEdge(int s, int t, double w)
	{
		this.s = s;
		this.t = t;
		this.w = w;
	}

	public int start() {
		return s;
	}

	public int end() {
		return t;
	}
	public double weight()
	{
		return this.w;
	}
	
	public void setWeight(double w)
	{
		this.w = w;
	}
	
	@Override
	public boolean equals(Object o)
	{
		simpleEdge v = (simpleEdge) o;
		if(v.start() == this.s && v.end() == this.t)
			return true;
		return false;
	}
	@Override 
    public int hashCode() {
        return 41 * (41 + this.s) + this.t;
    }
	
	public int compareTo(Object o)
	{
		simpleEdge v = (simpleEdge) o;
		return (this.w-v.weight())>0.0? 0:1;
	}
	
	public String toString()
	{
		return "("+this.s+","+this.t+"):"+this.w+";";
	}
	public static void main(String[] args) {
		
	}
}
