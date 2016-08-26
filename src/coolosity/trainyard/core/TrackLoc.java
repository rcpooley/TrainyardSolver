package coolosity.trainyard.core;

import java.awt.Point;

public class TrackLoc {

	private Location location;
	private Direction destination;
	
	public TrackLoc(Point p, Direction a, Direction b)
	{
		this(new Location(p,a),b);
	}
	
	public TrackLoc(Location loc, Direction dest)
	{
		this.location = loc;
		this.destination = dest;
	}
	
	public boolean contains(Direction c, Direction d)
	{
		return getA()==c && getB()==d || getA()==d && getB()==c;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public Direction getDestination()
	{
		return destination;
	}
	
	public Direction getA()
	{
		return location.getDirection();
	}
	
	public Direction getB()
	{
		return destination;
	}
}
