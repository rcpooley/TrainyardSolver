package coolosity.trainyard.core;

public class TrainData {

	private MyColor color;
	private int timeOffset;
	private boolean ab;
	
	public TrainData(MyColor color, int timeOffset, boolean ab)
	{
		this.color = color;
		this.timeOffset = timeOffset;
		this.ab = ab;
	}
	
	public MyColor getColor()
	{
		return color;
	}
	
	public void setColor(MyColor color)
	{
		this.color = color;
	}
	
	public int getTimeOffset()
	{
		return timeOffset;
	}
	
	public void setTimeOffset(int timeOffset)
	{
		this.timeOffset = timeOffset;
	}
	
	public boolean getAB()
	{
		return ab;
	}
	
	@Override
	public String toString()
	{
		return color+" "+timeOffset+" "+ab;
	}
}
