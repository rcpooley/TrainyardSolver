package coolosity.trainyard.solver.trans;

import java.util.ArrayList;
import java.util.HashMap;

import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.core.TrainData;

public class TransSetSearch
{
	
	private ArrayList<TrainData> input;
	private ArrayList<MyColor> output;
	private ArrayList<Integer> tools;
	
	private ArrayList<TransSet> solutions;
	
	public TransSetSearch(ArrayList<TrainData> input, ArrayList<MyColor> output, ArrayList<Integer> tools)
	{
		this.input = input;
		this.output = output;
		this.tools = tools;
	}
	
	public void generateTransSets()
	{
		solutions = new ArrayList<TransSet>();
		
		Transform t = new Transform(input);
		HashMap<TrainData[],HashMap<Integer,TrainData[]>> calc = t.calcOutputs(tools);
		for(TrainData[] inp : calc.keySet())
		{
			for(TrainData d : inp)
				System.out.print(d+", ");
			System.out.println(": ");
			for(int tool : calc.get(inp).keySet())
			{
				System.out.print("   "+tool+": ");
				for(TrainData d : calc.get(inp).get(tool))
					System.out.print(d+", ");
				System.out.println();
			}
		}
	}
	
	
}
