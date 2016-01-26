package Models;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class probReader {
	public final int maxLines = 2099733;
	public double[] br = new double[maxLines];
	
	public void writeProb2File(String path)
	{
		FileWriter writer = null;
		BufferedWriter bw = null;
		Random r = new Random();
		
		try {
			writer = new FileWriter(path,true);
		
			bw = new BufferedWriter(writer);
			for(int i = 0; i < maxLines; i++)
			{
				bw.write(String.format("%.2f",r.nextDouble()));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFile2Prob(String path)
	{
		FileReader FileStream;
		try {
			FileStream = new FileReader(path);
			BufferedReader br = new BufferedReader(FileStream);
			
			int i = 0;
			while(br.ready())
			{
				this.br[i++] = Double.parseDouble(br.readLine());
			}
			
			
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public double get(int i)
	{
		return this.br[i];
	}
	
	public double[] getProbs()
	{
		return this.br;
	}
	
	public static void main(String[] args) {
		probReader p = new probReader();
		p.readFile2Prob("probs");
		System.out.println(p.br[2099732]);
	}

}
