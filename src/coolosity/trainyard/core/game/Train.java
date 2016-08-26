package coolosity.trainyard.core.game;

import java.awt.Point;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.Location;
import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.core.TrainData;

public class Train
{

	private Location tloc;
	private TrainData tdata;
	
	public Train(int x, int y, Direction d, MyColor color, int timeOffset)
	{
		this(new Location(new Point(x,y),d),color,timeOffset);
	}
	
	public Train(Point p, Direction d, MyColor color, int timeOffset)
	{
		this(new Location(p,d),color,timeOffset);
	}
	
	public Train(Location loc, MyColor color, int timeOffset)
	{
		this.tloc = loc.clone();
		this.tdata = new TrainData(color,timeOffset,true);
	}
	
	public Location getLocation()
	{
		return tloc.clone();
	}
	
	public void setLocation(int x, int y, Direction d)
	{
		tloc.setX(x);
		tloc.setY(y);
		tloc.setDirection(d);
	}
	
	public void setLocation(Location tloc)
	{
		setLocation(tloc.getX(),tloc.getY(),tloc.getDirection());
	}
	
	public Point getLoc()
	{
		return (Point) tloc.getLocation().clone();
	}
	
	public int getX()
	{
		return tloc.getX();
	}
	
	public int getY()
	{
		return tloc.getY();
	}
	
	public Direction getDirection()
	{
		return tloc.getDirection();
	}
	
	public TrainData getData()
	{
		return tdata;
	}
	
	public MyColor getColor()
	{
		return tdata.getColor();
	}
	
	public void setColor(MyColor c)
	{
		tdata.setColor(c);
	}
	
	public void setTimeOffset(int timeoffset)
	{
		tdata.setTimeOffset(timeoffset);
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof Train))return false;
		Train t = (Train)other;
		return tloc.equals(t.tloc) && tdata.getColor()==t.getData().getColor();
	}
	
	@Override
	public String toString()
	{
		return tdata.getTimeOffset()+" "+tdata.getColor()+" "+tloc;
	}
}
