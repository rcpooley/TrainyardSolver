package coolosity.trainyard.core;

import java.awt.Point;
import java.util.ArrayList;

import coolosity.trainyard.core.game.Board;
import coolosity.trainyard.core.piece.Piece;
import coolosity.trainyard.core.piece.TrackPiece;

public class Location {

	private Point loc;
	private Direction dir;
	
	public Location(Point loc, Direction dir)
	{
		this.loc = loc;
		this.dir = dir;
	}
	
	public Location clone()
	{
		return new Location(new Point(loc),dir);
	}
	
	public Point getLocation()
	{
		return loc;
	}
	
	public int getX()
	{
		return loc.x;
	}
	
	public void setX(int x)
	{
		loc.x = x;
	}
	
	public int getY()
	{
		return loc.y;
	}
	
	public void setY(int y)
	{
		loc.y = y;
	}
	
	public Direction getDirection()
	{
		return dir;
	}
	
	public void setDirection(Direction d)
	{
		this.dir = d;
	}
	
	public boolean equalsLoc(Location other)
	{
		return other.loc.x==loc.x && other.loc.y == loc.y;
	}
	
	public int minDistance(Board board, Location other)
	{
		Location tgoal = new Location(other.getDirection().applyTo(other.getLocation()),other.getDirection().opposite());
		Piece p = board.pieceAt(tgoal.getLocation());
		if(p != null)
		{
			if(p instanceof TrackPiece)
			{
				TrackPiece tp = (TrackPiece)p;
				if(!tp.connectsTo(tgoal.getDirection()) && tp.numTracks()==2)return Integer.MAX_VALUE;
			}
			else
			{
				return Integer.MAX_VALUE;
			}
		}
		
		ArrayList<Point> used = new ArrayList<Point>();
		used.add(this.getLocation());
		return shortestPath(board, this, other, 0, Integer.MAX_VALUE, used);
	}
	
	private int shortestPath(Board board, Location current, Location goal, int length, int best, ArrayList<Point> used)
	{
		if(length>best)return best;
		if(current.equals(goal))
		{
			return length;
		}
		
		Location tgoal = new Location(goal.getDirection().applyTo(goal.getLocation()),goal.getDirection().opposite());
		
		if(current.equalsLoc(tgoal))
		{
			return length+1;
		}
		
		ArrayList<Direction> tryOrder = new ArrayList<Direction>();
		
		if(current.getX()<tgoal.getX())
			tryOrder.add(Direction.RIGHT);
		if(current.getX()>tgoal.getX())
			tryOrder.add(Direction.LEFT);
		if(current.getY()>tgoal.getY())
			tryOrder.add(Direction.UP);
		if(current.getY()<tgoal.getY())
			tryOrder.add(Direction.DOWN);
	
		for(Direction d : Direction.keyValues())
		{
			if(!tryOrder.contains(d))
				tryOrder.add(d);
		}
		
		for(Direction d : tryOrder)
		{
			if(d != current.getDirection())
			{
				Piece pp = board.pieceAt(current.getLocation());
				boolean valid = true;
				if(pp != null)
				{
					if(pp instanceof TrackPiece)
					{
						TrackPiece tp = (TrackPiece)pp;
						if(!tp.hasConnection(current.getDirection(), d) && tp.numTracks()==2)valid = false;
					}
					else
					{
						valid = false;
					}
				}
				if(valid)
				{
					Location nxt = new Location(d.applyTo(current.getLocation()),d.opposite());
					if(board.inBounds(nxt.getLocation()))
					{
						Piece p = board.pieceAt(nxt.getLocation());
						valid = !nxt.equalsLoc(goal) || nxt.equals(goal);
						if(p != null)
						{
							if(p instanceof TrackPiece)
							{
								TrackPiece tp = (TrackPiece)p;
								if(!tp.connectsTo(d.opposite()) && tp.numTracks()==2)valid = false;
							}
							else
							{
								valid = false;
							}
						}
						for(Point pt : used)
						{
							if(nxt.getLocation().equals(pt))valid = false;
						}
						if(valid)
						{
							ArrayList<Point> cloneUsed = new ArrayList<Point>(used);
							cloneUsed.add(nxt.getLocation());
							int ans = shortestPath(board, nxt, goal, length+1, best, cloneUsed);
							if(ans<best)best = ans;
						}
					}
				}
			}
		}
		
		return best;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof Location))return false;
		Location othe = (Location)other;
		return equalsLoc(othe) && othe.dir==dir;
	}
	
	@Override
	public String toString()
	{
		return getX()+" "+getY()+" "+getDirection();
	}
}
