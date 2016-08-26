package coolosity.trainyard.core.piece;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.Location;
import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.display.Resources;
import coolosity.trainyard.main.Settings;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;

public class SpawnPiece extends Piece
{

	private ArrayList<MyColor> spawnOrder;
	private Direction direction;
	
	private ArrayList<MyColor> toadd;
	private boolean remove;
	
	private int tempSent;
	
	public SpawnPiece()
	{
		super(1);
		spawnOrder = new ArrayList<MyColor>();
		toadd = new ArrayList<MyColor>();
		this.direction = Direction.UP;
	}
	
	@Override
	public Piece clone()
	{
		SpawnPiece p = new SpawnPiece();
		p.spawnOrder.addAll(spawnOrder);
		p.direction = direction;
		return p;
	}
	
	public void setTempSent(int tempSent)
	{
		this.tempSent = tempSent;
	}

	@Override
	public void saveTo(ByteWriter writer) {
		writer.writeInt(spawnOrder.size(), 1);
		for(MyColor mc : spawnOrder)
		{
			writer.writeInt(mc.ordinal(), 1);
		}
		writer.writeInt(direction.ordinal(), 1);
	}

	@Override
	public void loadFrom(ByteReader reader) {
		int num = reader.readInt(1);
		for(int i=0;i<num;i++)
		{
			spawnOrder.add(MyColor.values()[reader.readInt(1)]);
		}
		direction = Direction.values()[reader.readInt(1)];
	}

	@Override
	public BufferedImage getImage() {
		if(remove && spawnOrder.size()>0)
		{
			spawnOrder.remove(spawnOrder.size()-1);
			remove = false;
		}
		
		while(toadd.size()>0)
		{
			spawnOrder.add(toadd.get(0));
			toadd.remove(0);
		}
		
		BufferedImage imgg = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
		int bor = 5;
		
		BufferedImage img = imgg.getSubimage(bor, bor, imgg.getWidth()-bor*2, imgg.getHeight()-bor*2);
		Graphics2D g = (Graphics2D)img.getGraphics();
		
		
		if(spawnOrder.size()<=1)
		{
			if(tempSent==0)
			{
				for(MyColor c : spawnOrder)
				{
					g.setColor(c.getColor());
					g.drawImage(Resources.getRecoloredImage("spawn", Settings.search, c.getColor()), 0, 0, img.getWidth(), img.getHeight(), null);
				}
			}
		}
		else if(spawnOrder.size()<=4)
		{
			double ppx = img.getWidth()*.5;
			double ppy = img.getHeight()*.5;
			
			for(int i=0;i<spawnOrder.size();i++)
			{
				if(tempSent<=i)
				{
					int cx = i%2;
					int cy = i/2;
					g.setColor(spawnOrder.get(i).getColor());
					g.drawImage(Resources.getRecoloredImage("spawn", Settings.search, spawnOrder.get(i).getColor()), (int)(cx*ppx), (int)(cy*ppy), (int)ppx, (int)ppy, null);
				}
			}
		}
		else if(spawnOrder.size()<=9)
		{
			double ppx = img.getWidth()*1.0/3;
			double ppy = img.getHeight()*1.0/3;
			
			for(int i=0;i<spawnOrder.size();i++)
			{
				if(tempSent<=i)
				{
					int cx = i%3;
					int cy = i/3;
					g.setColor(spawnOrder.get(i).getColor());
					g.drawImage(Resources.getRecoloredImage("spawn", Settings.search, spawnOrder.get(i).getColor()), (int)(cx*ppx), (int)(cy*ppy), (int)ppx, (int)ppy, null);
				}
			}
		}
		
		drawDirectionLine(imgg, direction);
		
		return imgg;
	}
	
	@Override
	public void directionPressed(Direction d)
	{
		direction = d;
	}
	
	public void addColor(MyColor color)
	{
		if(spawnOrder.size()<9)
			toadd.add(color);
	}
	
	public void removeColor()
	{
		remove = true;
	}
	
	public ArrayList<MyColor> getColors()
	{
		return spawnOrder;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public Location getExitLoc(Point spawnLoc)
	{
		Point to = direction.applyTo(spawnLoc);
		return new Location(to,direction.opposite());
	}
}
