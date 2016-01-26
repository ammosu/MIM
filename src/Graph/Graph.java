package Graph;
import java.util.List;

public interface Graph {
	
	public void addVertex(int u);
	public void addEdge(int u, int v);
	public void addEdge(int u, int v, double w);
	public void edgeRemove(int u, int v);
	public boolean containEdge(int u, int v);
	public boolean containVertex(int v);
	public List<Integer> decends(int id);
	public List<Integer> pars(int id);
	public void clear();
	
	public static void main(String[] args) {
		
	}

}
