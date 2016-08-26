package coolosity.trainyard.solver.trans;

import java.util.ArrayList;
import java.util.HashMap;

import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.core.TrainData;

public class Transform
{

	public static final int COLORRED = 0;
	public static final int COLORORANGE = 1;
	public static final int COLORYELLOW = 2;
	public static final int COLORGREEN = 3;
	public static final int COLORBLUE = 4;
	public static final int COLORPURPLE = 5;
	public static final int COLORBROWN = 6;
	public static final int SPLIT = 7;
	public static final int COMBINE = 8;
	public static final int PASS = 9;
	
	private int type;
	private ArrayList<TrainData> input, output;
	
	public Transform(ArrayList<TrainData> input)
	{
		this();
		this.input.addAll(input);
	}
	
	public Transform()
	{
		input = new ArrayList<TrainData>();
		output = new ArrayList<TrainData>();
	}
	
	public HashMap<TrainData[],HashMap<Integer,TrainData[]>> calcOutputs(ArrayList<Integer> tools)
	{
		HashMap<TrainData[],HashMap<Integer,TrainData[]>> out = new HashMap<TrainData[],HashMap<Integer,TrainData[]>>();
		for(TrainData inp : input)
		{
			TrainData[] arr = {inp};
			HashMap<Integer,TrainData[]> trans = new HashMap<Integer,TrainData[]>();
			for(int tool : tools)
			{
				//color
				if(tool<=6)
				{
					MyColor c = MyColor.values()[tool];
					trans.put(tool, new TrainData[]{new TrainData(c,0,inp.getAB())});
				}
				else if(tool == SPLIT)
				{
					MyColor[] c = inp.getColor().getSplit();
					TrainData[] d = {
						new TrainData(c[0],0,inp.getAB()),
						new TrainData(c[1],0,inp.getAB())
					};
					trans.put(tool, d);
				}
			}
			out.put(arr, trans);
		}
		for(int i=0;i<input.size()-1;i++)
		{
			for(int j=i+1;j<input.size();j++)
			{
				TrainData a = input.get(i);
				TrainData b = input.get(j);
				MyColor c = a.getColor().combine(b.getColor());
				TrainData[] arr = {a,b};
				HashMap<Integer,TrainData[]> trans = new HashMap<Integer,TrainData[]>();
				//combine
				TrainData[] d = {new TrainData(c,0,a.getAB())};
				trans.put(COMBINE, d);
				
				//pass
				if(a.getAB()==b.getAB())
				{
					d = new TrainData[]{
						new TrainData(c,0,a.getAB()),
						new TrainData(c,0,a.getAB())
					};
				}
				out.put(arr, trans);
			}
		}
		return out;
	}
	
	public int getType()
	{
		return type;
	}
}
