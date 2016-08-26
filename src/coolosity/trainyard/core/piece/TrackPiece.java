package coolosity.trainyard.core.piece;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.TrackLoc;
import coolosity.trainyard.display.Resources;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;
import coolosity.trainyard.utils.Utils;

public class TrackPiece extends Piece
{

	private Direction a1, b1, a2, b2;
	private int next = 0;
	
	private boolean tempInvert;
	
	public TrackPiece()
	{
		super(5);
		reset();
	}
	
	@Override
	public Piece clone()
	{
		TrackPiece p = new TrackPiece();
		p.a1 = a1;
		p.a2 = a2;
		p.b1 = b1;
		p.b2 = b2;
		return p;
	}
	
	public void setTempInvert(boolean tempInvert)
	{
		if(a1!=Direction.NONE && a2 != Direction.NONE)
			this.tempInvert = tempInvert;
	}

	@Override
	public void saveTo(ByteWriter writer) {
		writer.writeInt(a1.ordinal(), 1);
		writer.writeInt(b1.ordinal(), 1);
		writer.writeInt(a2.ordinal(), 1);
		writer.writeInt(b2.ordinal(), 1);
	}

	@Override
	public void loadFrom(ByteReader reader) {
		a1 = Direction.values()[reader.readInt(1)];
		b1 = Direction.values()[reader.readInt(1)];
		a2 = Direction.values()[reader.readInt(1)];
		b2 = Direction.values()[reader.readInt(1)];
	}
	
	@Override
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
		
		Color a = Color.LIGHT_GRAY;
		Color b = Color.LIGHT_GRAY;
		if(tempInvert)a = Color.DARK_GRAY;
		else b = Color.DARK_GRAY;
		Color search = new Color(178,178,178);
		
		if(!tempInvert)
		{
			BufferedImage straight = Resources.getRecoloredImage("straight", search, a);
			BufferedImage turn = Resources.getRecoloredImage("turn", search, a);
			drawTrack(img,false,straight,turn);
			
			straight = Resources.getRecoloredImage("straight", search, b);
			turn = Resources.getRecoloredImage("turn", search, b);
			drawTrack(img,true,straight,turn);
		}
		else
		{
			BufferedImage straight = Resources.getRecoloredImage("straight", search, b);
			BufferedImage turn = Resources.getRecoloredImage("turn", search, b);
			drawTrack(img,true,straight,turn);
			
			straight = Resources.getRecoloredImage("straight", search, a);
			turn = Resources.getRecoloredImage("turn", search, a);
			drawTrack(img,false,straight,turn);
		}
		
		return img;
	}
	
	private void drawTrack(BufferedImage img, boolean is1, BufferedImage straight, BufferedImage turn)
	{
		Graphics2D g = (Graphics2D)img.getGraphics();
		
		if(equal(Direction.LEFT,Direction.UP,is1))
			g.drawImage(turn, 0, 0, img.getWidth(), img.getHeight(), null);
		if(equal(Direction.LEFT,Direction.RIGHT,is1))
			g.drawImage(straight, 0, 0, img.getWidth(), img.getHeight(), null);
		if(equal(Direction.LEFT,Direction.DOWN,is1))
			g.drawImage(Utils.rotate(turn,3), 0, 0, img.getWidth(), img.getHeight(), null);
		
		if(equal(Direction.UP,Direction.RIGHT,is1))
			g.drawImage(Utils.rotate(turn,1), 0, 0, img.getWidth(), img.getHeight(), null);
		if(equal(Direction.UP,Direction.DOWN,is1))
			g.drawImage(Utils.rotate(straight,1), 0, 0, img.getWidth(), img.getHeight(), null);
		
		if(equal(Direction.RIGHT,Direction.DOWN,is1))
			g.drawImage(Utils.rotate(turn,2), 0, 0, img.getWidth(), img.getHeight(), null);
		
		for(Direction d : Direction.keyValues())
		{
			if(equal(d,d,is1))
			{
				g.setColor(new Color(255,0,0,128));
				g.fillRect(0, 0, img.getWidth(), img.getHeight());
				g.setColor(Color.BLUE);
				
				int x1 = img.getWidth()/2, y1 = img.getHeight()/2;
				Point p = d.applyTo(new Point(0,0));
				int x2 = x1+p.x*img.getWidth()/2;
				int y2 = y1+p.y*img.getHeight()/2;
				
				g.setStroke(new BasicStroke(5));
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
	
	private boolean equal(Direction a, Direction b, boolean is1)
	{
		if(is1)
		{
			if(a1==a && b1==b || a1==b && b1==a)
			{
				return true;
			}
		}
		else
		{
			if(a2==a && b2==b || a2==b && b2==a)
			{
				return true;
			}
		}
		return false;
	}
	
	public void switch12()
	{
		Direction c1 = a1;
		Direction d1 = b1;
		a1 = a2;
		b1 = b2;
		a2 = c1;
		b2 = d1;
	}
	
	public boolean connectsTo(Direction d)
	{
		return a1==d || b1==d || a2==d || b2==d;
	}
	
	public ArrayList<Direction[]> getConnections()
	{
		ArrayList<Direction[]> res = new ArrayList<Direction[]>();
		if(a1 != Direction.NONE && b1 != Direction.NONE)res.add(new Direction[]{a1,b1});
		if(a2 != Direction.NONE && b2 != Direction.NONE)res.add(new Direction[]{a2,b2});
		return res;
	}
	
	public void setConnections(ArrayList<Direction[]> conns)
	{
		if(conns.size()>0)
		{
			Direction[] a = conns.get(0);
			a1 = a[0];
			b1 = a[1];
		}
		if(conns.size()>1)
		{
			Direction[] a = conns.get(1);
			a2 = a[0];
			b2 = a[1];
		}
	}
	
	public Direction getConnection(Direction d, boolean invert)
	{
		if(!invert)
		{
			if(a1==d)return b1;
			else if(b1==d)return a1;
			else if(a2==d)return b2;
			else if(b2==d)return a2;
		}
		else
		{
			if(a2==d)return b2;
			else if(b2==d)return a2;
			if(a1==d)return b1;
			else if(b1==d)return a1;
		}
		return null;
	}
	
	public boolean isMajor(Direction d, boolean invert)
	{
		if(!invert)
			return a1==d || b1==d;
		else
			return a2==d || b2==d;
	}

	@Override
	public void directionPressed(Direction d)
	{
		if(next==0)
		{
			a1 = d;
		}
		else if(next==1)
		{
			b1 = d;
		}
		else if(next == 2)
		{
			a2 = d;
		}
		else if(next == 3)
		{
			b2 = d;
		}
		next++;
	}
	
	public void reset()
	{
		next = 0;
		a1 = Direction.NONE;
		b1 = Direction.NONE;
		a2 = Direction.NONE;
		b2 = Direction.NONE;
	}
	
	public void setTracks(TrackLoc[] tracks)
	{
		if(tracks[0] != null)
		{
			a1 = tracks[0].getA();
			b1 = tracks[0].getB();
		}
		if(tracks.length>1 && tracks[1] != null)
		{
			a2 = tracks[1].getA();
			b2 = tracks[1].getB();
		}
	}
	
	public TrackLoc[] getTracks(Point loc)
	{
		TrackLoc[] ret = new TrackLoc[2];
		int cur = 0;
		if(a1 != Direction.NONE && b1 != Direction.NONE)
		{
			ret[cur++] = new TrackLoc(loc,a1,b1);
		}
		if(a2 != Direction.NONE && b2 != Direction.NONE)
		{
			ret[cur++] = new TrackLoc(loc,a2,b2);
		}
		return ret;
	}
	
	public boolean hasConnection(Direction a, Direction b)
	{
		TrackLoc[] tracks = getTracks(null);
		for(TrackLoc tl : tracks)
		{
			if(tl != null)
			{
				if(tl.contains(a, b))return true;
			}
		}
		return false;
	}
	
	public int numTracks()
	{
		TrackLoc[] tracks = getTracks(null);
		int num = 0;
		for(TrackLoc tl : tracks)
		{
			if(tl != null)num++;
		}
		return num;
	}
}
