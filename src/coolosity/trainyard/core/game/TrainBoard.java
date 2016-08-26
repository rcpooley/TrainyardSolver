package coolosity.trainyard.core.game;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.Location;
import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.core.TrainData;
import coolosity.trainyard.core.piece.EndPiece;
import coolosity.trainyard.core.piece.PaintPiece;
import coolosity.trainyard.core.piece.Piece;
import coolosity.trainyard.core.piece.SpawnPiece;
import coolosity.trainyard.core.piece.SplitPiece;
import coolosity.trainyard.core.piece.TrackPiece;

public class TrainBoard {

	private Board board;
	private int[][] inverted;
	private HashMap<Point,ArrayList<Integer>> endData;
	private ArrayList<Train> trains;
	
	private Point hover = new Point(0,0);
	private int currentTick;
	private boolean testing;
	private boolean crashed;
	
	public TrainBoard()
	{
		this(new Board());
	}
	
	public TrainBoard(Board board)
	{
		this.board = board;
		resetTesting();
	}
	
	public void resetTesting()
	{
		this.inverted = new int[board.getWidth()][board.getHeight()];
		this.endData = new HashMap<Point,ArrayList<Integer>>();
		this.trains = new ArrayList<Train>();
		this.testing = false;
		this.crashed = false;
	}
	
	public void startTesting()
	{
		resetTesting();
		testing = true;
	}
	
	public ArrayList<Integer> getEndData(int x, int y)
	{
		for(Point p : endData.keySet())
		{
			if(p.x==x && p.y==y)return endData.get(p);
		}
		Point n = new Point(x,y);
		endData.put(n, new ArrayList<Integer>());
		return endData.get(n);
	}
	
	public int getData(int x, int y)
	{
		return inverted[x][y];
	}
	
	public void updateTrains()
	{
		currentTick++;
		//move existing trains first major, then minor
		ArrayList<Train> major = new ArrayList<Train>(), minor = new ArrayList<Train>();
		for(Train t : trains)
		{
			Location to = new Location(t.getDirection().applyTo(t.getLoc()),t.getDirection().opposite());
			Piece p = board.pieceAt(to.getLocation());
			if(!(p instanceof TrackPiece))
			{
				major.add(t);
			}
			else
			{
				TrackPiece tp = (TrackPiece)p;
				boolean invert = inverted[to.getX()][to.getY()]==1;
				if(tp.isMajor(to.getDirection(), invert))
					major.add(t);
				else
					minor.add(t);
			}
		}
		
		ArrayList<Train> trns = new ArrayList<Train>();
		trns.addAll(major);
		trns.addAll(minor);
		for(Train t : trns)
		{
			trains.remove(t);
			ArrayList<Train> ret = moveTrain(t);
			if(ret==null)
				crashed = true;
			else
			{
				for(Train r : ret)
				{
					trains.add(r);
				}
			}
		}
		
		//spawn trains
		ArrayList<Point> spawns = board.getAllPieces(SpawnPiece.class);
		for(Point p : spawns)
		{
			SpawnPiece sp = (SpawnPiece)board.pieceAt(p);
			if(inverted[p.x][p.y]<sp.getColors().size())
			{
				Train temp = new Train(p,sp.getDirection(),sp.getColors().get(inverted[p.x][p.y]),currentTick);
				ArrayList<Train> ret = moveTrain(temp);
				if(ret==null)
					crashed = true;
				else
				{
					for(Train r : ret)
					{
						trains.add(r);
					}
				}
				inverted[p.x][p.y]++;
			}
		}
		
		//combine trains in same spot
		for(int i=trains.size()-2;i>=0;i--)
		{
			Train a = trains.get(i);
			for(int j=trains.size()-1;j>i;j--)
			{
				Train b = trains.get(j);
				if(a.getLocation().equals(b.getLocation()))
				{
					trains.remove(j);
					a.setColor(a.getColor().combine(b.getColor()));
				}
			}
		}
		
		for(Train t : trains)
		{
			t.setTimeOffset(currentTick);
		}
	}
	
	//returns true when all trains have either crashed or went into end piece
	public boolean isFinished()
	{
		if(trains.size()>0)return false;
		
		ArrayList<Point> pts = board.getAllPieces(SpawnPiece.class);
		for(Point p : pts)
		{
			SpawnPiece sp = (SpawnPiece)board.pieceAt(p);
			if(inverted[p.x][p.y]<sp.getColors().size())return false;
		}
		return true;
	}
	
	//returns true when isFinshed without crashes and all end pieces have been satisfied
	public boolean isComplete()
	{
		if(!isFinished() || crashed)return false;
		
		ArrayList<Point> pts = board.getAllPieces(EndPiece.class);
		for(Point p : pts)
		{
			EndPiece sp = (EndPiece)board.pieceAt(p);
			ArrayList<Integer> end = getEndData(p.x,p.y);
			if(end.size()<sp.getColors().size())return false;
		}
		return true;
	}
	

	private ArrayList<Train> moveTrain(Train train)
	{
		ArrayList<Train> ret = new ArrayList<Train>();
		ret.add(train);
		Point cur = new Point(train.getX(),train.getY());
		Point to = train.getDirection().applyTo(train.getLoc());
		Location toloc = new Location(to, train.getDirection().opposite());
		Piece p = board.pieceAt(to);
		boolean inv = inverted[to.x][to.y]==1;
		if(board.pieceAt(cur) instanceof TrackPiece)
			inverted[cur.x][cur.y] = 1-inverted[cur.x][cur.y];
		
		if(p instanceof TrackPiece)
		{
			TrackPiece tp2 = (TrackPiece)p;
			train.setLocation(toloc.getX(), toloc.getY(), tp2.getConnection(toloc.getDirection(), inv));
			Train pass = trainAt(toloc);
			if(pass != null)
			{
				MyColor c = train.getColor().combine(pass.getColor());
				train.setColor(c);
				pass.setColor(c);
			}
			if(!tp2.connectsTo(toloc.getDirection()))
			{
				return null;
			}
		}
		else if(p instanceof SplitPiece)
		{
			SplitPiece sp = (SplitPiece)p;
			if(sp.getDirection() == toloc.getDirection())
			{
				MyColor[] spl = train.getColor().getSplit();
				ret.clear();
				ret.add(new Train(toloc.getLocation(),sp.getDirection().cw(),spl[0],currentTick));
				ret.add(new Train(toloc.getLocation(),sp.getDirection().ccw(),spl[1],currentTick));
			}
			else
			{
				return null;
			}
		}
		else if(p instanceof PaintPiece)
		{
			PaintPiece pp = (PaintPiece)p;
			if(pp.connectsTo(toloc.getDirection()))
			{
				train.setLocation(toloc.getX(),toloc.getY(),pp.getConnection(toloc.getDirection()));
				train.setColor(pp.getColor());
			}
			else
			{
				return null;
			}
		}
		else if(p instanceof EndPiece)
		{
			EndPiece ep = (EndPiece)p;
			if(!ep.hasDirection(toloc.getDirection()))
			{
				return null;
			}
			
			ArrayList<Integer> end = getEndData(to.x, to.y);
			ArrayList<MyColor> cols = ep.getColors();
			
			//get remaining colors
			ArrayList<MyColor> remaining = new ArrayList<MyColor>(cols);
			for(int ii : end)
			{
				remaining.remove(cols.get(ii));
			}
			if(remaining.contains(train.getColor()))
			{
				int free = -1;
				for(int j=0;j<cols.size()&&free==-1;j++)
				{
					if(!end.contains(j) && cols.get(j)==train.getColor())free = j;
				}
				end.add(free);
				ret.clear();
				return ret;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		
		return ret;
	}
	
	private Train trainAt(Location loc)
	{
		for(Train t : trains)
		{
			if(t.getLocation().equals(loc))return t;
		}
		return null;
	}
	
	public ArrayList<TrainData> getInputs()
	{
		ArrayList<TrainData> data = new ArrayList<TrainData>();
		for(Piece p : )
	}
	
	public void mousePressed(int hoverButton)
	{
		//TODO add button functionality
	}
	
	//called from Drawing
	public void setHover(Point hover)
	{
		this.hover = hover;
	}
	
	public void setHoverTo(Piece piece)
	{
		if(hover != null)
		{
			board.setPieceAt(hover.x, hover.y, piece);
		}
	}
	
	public void addColor(MyColor color)
	{
		if(hover != null)
		{
			Piece p = board.pieceAt(hover);
			if(p != null)
			{
				if(p instanceof SpawnPiece)
				{
					SpawnPiece sp = (SpawnPiece)p;
					sp.addColor(color);
				}
				else if(p instanceof EndPiece)
				{
					EndPiece ep = (EndPiece)p;
					ep.addColor(color);
				}
				else if(p instanceof PaintPiece)
				{
					PaintPiece pp = (PaintPiece)p;
					pp.setColor(color);
				}
			}
		}
	}
	
	public void removeColor()
	{
		if(hover != null)
		{
			Piece p = board.pieceAt(hover);
			if(p != null)
			{
				if(p instanceof SpawnPiece)
				{
					SpawnPiece sp = (SpawnPiece)p;
					sp.removeColor();
				}
				else if(p instanceof EndPiece)
				{
					EndPiece ep = (EndPiece)p;
					ep.removeColor();
				}
				else if(p instanceof TrackPiece)
				{
					TrackPiece tp = (TrackPiece)p;
					tp.reset();
				}
			}
		}
	}
	
	public void directionPressed(Direction d)
	{
		if(hover != null)
		{
			Piece p = board.pieceAt(hover);
			if(p != null)
			{
				p.directionPressed(d);
			}
		}
	}
	
	public void enterPressed()
	{
		if(hover != null)
		{
			Piece p = board.pieceAt(hover);
			if(p != null)
			{
				if(p instanceof TrackPiece)
				{
					TrackPiece tp = (TrackPiece)p;
					tp.switch12();
				}
			}
		}
	}
	
	public int getWidth()
	{
		return board.getWidth();
	}
	
	public int getHeight()
	{
		return board.getHeight();
	}
	
	public ArrayList<Train> getTrains()
	{
		return new ArrayList<Train>(trains);
	}
	
	public Piece pieceAt(int x, int y)
	{
		return board.pieceAt(x, y);
	}
	
	public boolean isTesting()
	{
		return testing;
	}
	
	public boolean hasCrashed()
	{
		return crashed;
	}
	
	public void save(File file)
	{
		board.save(file);
	}
	
	public void load(File file)
	{
		board.load(file);
	}
}
