package coolosity.trainyard.core.piece;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;

public abstract class Piece {

	private static ArrayList<Piece> pieces;
	
	public static void init()
	{
		pieces = new ArrayList<Piece>();
		pieces.add(new SpawnPiece());
		pieces.add(new EndPiece());
		pieces.add(new PaintPiece());
		pieces.add(new SplitPiece());
		pieces.add(new TrackPiece());
		pieces.add(new RockPiece());
	}
	
	public static Piece fromReader(ByteReader reader)
	{
		int type = reader.readInt(1);
		if(type==0)return null;
		
		Piece tospawn = null;
		for(Piece p : pieces)
		{
			if(p.getType()==type)tospawn = p;
		}
		if(tospawn==null)return null;
		
		Piece p = null;
		try {
			p = tospawn.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		p.loadFrom(reader);
		return p;
	}

	
	private int type;
	public Piece(int type)
	{
		this.type = type;
	}
	
	public abstract BufferedImage getImage();
	
	public abstract Piece clone();
	
	public int getType()
	{
		return type;
	}
	
	public abstract void saveTo(ByteWriter writer);
	
	public abstract void loadFrom(ByteReader reader);
	
	public void directionPressed(Direction d) {}
	
	protected void drawDirectionLine(BufferedImage img, Direction d)
	{
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setStroke(new BasicStroke(16));
		g.setColor(Color.CYAN);
		switch(d)
		{
		case UP:
			g.drawLine(0, 0, img.getWidth(), 0);
			break;
		case DOWN:
			g.drawLine(0, img.getHeight()-1, img.getWidth(), img.getHeight()-1);
			break;
		case LEFT:
			g.drawLine(0, 0, 0, img.getHeight());
			break;
		case RIGHT:
			g.drawLine(img.getWidth()-1, 0, img.getWidth()-1, img.getHeight());
			break;
		case NONE:
			break;
		}
	}
}
