package coolosity.trainyard.solver.trans;

import java.util.ArrayList;

public class TransSet extends Transform
{

	private ArrayList<Transform> input;
	
	public TransSet(ArrayList<Transform> input)
	{
		this.input = new ArrayList<Transform>(input);
	}
}
