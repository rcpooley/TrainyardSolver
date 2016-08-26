package coolosity.trainyard.core.game;

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.piece.Piece;
import coolosity.trainyard.core.piece.TrackPiece;
import coolosity.trainyard.main.TrainMain;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;

public class Board
{
	
	private static final int SIZE = 7;
	
	private Piece[][] board;
	
	public Board()
	{
		board = new Piece[SIZE][SIZE];
	}
	
	public ArrayList<Point> getAllPieces(Class<?> type)
	{
		ArrayList<Point> pts = new ArrayList<Point>();
		for(int x=0;x<board.length;x++)
		{
			for(int y=0;y<board[0].length;y++)
			{
				if(board[x][y] != null && board[x][y].getClass() == type)
				{
					pts.add(new Point(x,y));
				}
			}
		}
		return pts;
	}
	
	public <T> ArrayList<T> getAllPieces()
	{
		//System.out.println(T.class);
		return null;
	}
	
	public boolean inBounds(Point p)
	{
		return inBounds(p.x,p.y);
	}
	
	public boolean inBounds(int x, int y)
	{
		return x>=0 && y>=0 && x<board.length && y<board[0].length;
	}
	
	public Piece pieceAt(Point p)
	{
		return pieceAt(p.x,p.y);
	}
	
	public Piece pieceAt(int x, int y)
	{
		if(!inBounds(x,y))return null;
		return board[x][y];
	}
	
	public void setPieceAt(int x, int y, Piece p)
	{
		board[x][y] = p;
	}
	
	//returns the points with thresh or more connections
	public ArrayList<Point> getPointsWithXConns(int thresh)
	{
		ArrayList<Point> totry = new ArrayList<Point>();
		for(int x=0;x<getWidth();x++)
		{
			for(int y=0;y<getHeight();y++)
			{
				Point l = new Point(x,y);
				Piece p = pieceAt(l);
				if(p==null || p instanceof TrackPiece)
				{
					int numExits = 0;
					for(Direction d : Direction.keyValues())
					{
						Point check = d.applyTo(l);
						Piece pp = pieceAt(check);
						if(inBounds(check) && (pp==null || pp instanceof TrackPiece))
							numExits++;
					}
					if(numExits>=thresh)
					{
						totry.add(l);
					}
				}
			}
		}
		return totry;
	}
	
	public int getWidth()
	{
		return board.length;
	}
	
	public int getHeight()
	{
		return board[0].length;
	}
	
	public void clearTrack()
	{
		ArrayList<Point> tracks = getAllPieces(TrackPiece.class);
		for(Point p : tracks)
		{
			board[p.x][p.y] = null;
		}
	}
	
	public Board clone()
	{
		Board b = new Board();
		for(int x=0;x<board.length;x++)
		{
			for(int y=0;y<board[0].length;y++)
			{
				if(board[x][y] != null)
					b.board[x][y] = board[x][y].clone();
			}
		}
		return b;
	}
	
	public byte[] getBytes()
	{
		ByteWriter writer = new ByteWriter();
		writer.writeInt(board.length, 1);
		writer.writeInt(board[0].length, 1);
		for(int x=0;x<board.length;x++)
		{
			for(int y=0;y<board[0].length;y++)
			{
				if(board[x][y]==null)
				{
					writer.writeInt(0, 1);
				}
				else
				{
					writer.writeInt(board[x][y].getType(), 1);
					board[x][y].saveTo(writer);
				}
			}
		}
		return writer.getBytes();
	}
	
	public void save(File file)
	{
		byte[] data = getBytes();
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
		}
		catch(IOException e)
		{
			System.err.println("Could not save world to file "+file.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	public void load(File file)
	{
		if(!file.exists())
		{
			TrainMain.instance.showMsg("Board \""+file.getName()+"\" does not exist");
			return;
		}
		Path path = Paths.get(file.getAbsolutePath());
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		ByteReader reader = new ByteReader(data);
		board = new Piece[reader.readInt(1)][reader.readInt(1)];
		for(int x=0;x<board.length;x++)
		{
			for(int y=0;y<board[0].length;y++)
			{
				board[x][y] = Piece.fromReader(reader);
			}
		}
	}
}
