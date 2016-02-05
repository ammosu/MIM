package Models;

import Graph.loadGraph;

public class Main {

	public void basicIC()
	{
		IndepCascade ic = new IndepCascade();
		loadGraph load = new loadGraph();
		load.readData(ic.getGraph(), "Brightkite_edges.txt", 0, "\t");
		load.wcProb(ic.getGraph());
		
		ic.setMC(1000);
		
		double startTime = System.currentTimeMillis();
		ic.svDeserialize("celf"); // first seed propagation results
		//this.celfAlg(1);
		ic.greedyAlg(1);
		double endTime = System.currentTimeMillis();
		System.out.println("Time: " + (endTime-startTime)/1000 + "sec.");
	}
	public static void main(String[] args) {
		new Main().basicIC();

	}

}
