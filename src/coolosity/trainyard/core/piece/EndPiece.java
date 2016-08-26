package coolosity.trainyard.core.piece;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.display.Resources;
import coolosity.trainyard.main.Settings;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;

public class EndPiece extends Piece
{

	private ArrayList<MyColor> endOrder;
	private ArrayList<Direction> directions;
	
	private ArrayList<Integer> tempFinished;
	
	public EndPiece()
	{
		super(2);
		endOrder = new ArrayList<MyColor>();
		directions = new ArrayList<Direction>();
		directions.add(Direction.UP);
	}
	
	public ArrayList<Direction> getDirections()
	{
		return directions;
	}
	
	@Override
	public Piece clone()
	{
		EndPiece p = new EndPiece();
		p.endOrder.addAll(endOrder);
		p.directions.clear();
		p.directions.addAll(directions);
		return p;
	}
	
	public void setTempFinished(ArrayList<Integer> tempFinished)
	{
		this.tempFinished = tempFinished;
	}
	
	public ArrayList<MyColor> getColors()
	{
		return endOrder;
	}

	@Override
	public void saveTo(ByteWriter writer) {
		writer.writeInt(endOrder.size(), 1);
		for(MyColor mc : endOrder)
		{
			writer.writeInt(mc.ordinal(), 1);
		}
		writer.writeInt(directions.size(), 1);
		for(Direction d : directions)
		{
			writer.writeInt(d.ordinal(), 1);
		}
	}

	@Override
	public void loadFrom(ByteReader reader) {
		int num = reader.readInt(1);
		for(int i=0;i<num;i++)
		{
			endOrder.add(MyColor.values()[reader.readInt(1)]);
		}
		num = reader.readInt(1);
		directions.clear();
		for(int i=0;i<num;i++)
		{
			directions.add(Direction.values()[reader.readInt(1)]);
		}
	}

	@Override
	public BufferedImage getImage()
	{
		BufferedImage imgg = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
		int bor = 5;
		
		BufferedImage img = imgg.getSubimage(bor, bor, imgg.getWidth()-bor*2, imgg.getHeight()-bor*2);
		Graphics2D g = (Graphics2D)img.getGraphics();
		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		if(tempFinished != null)temp.addAll(tempFinished);
		
		if(endOrder.size()<=1)
		{
			if(temp.size()==0)
			{
				for(MyColor c : endOrder)
				{
					g.setColor(c.getColor());
					g.fillOval(0, 0, img.getWidth(), img.getHeight());
				}
			}
		}
		else if(endOrder.size()<=4)
		{
			double ppx = img.getWidth()*.5;
			double ppy = img.getHeight()*.5;
			
			for(int i=0;i<endOrder.size();i++)
			{
				if(!temp.contains(i))
				{
					int cx = i%2;
					int cy = i/2;
					g.setColor(endOrder.get(i).getColor());
					g.fillOval((int)(cx*ppx), (int)(cy*ppy), (int)ppx, (int)ppy);
				}
			}
		}
		else if(endOrder.size()<=9)
		{
			double ppx = img.getWidth()*1.0/3;
			double ppy = img.getHeight()*1.0/3;
			
			for(int i=0;i<endOrder.size();i++)
			{
				if(!temp.contains(i))
				{
					int cx = i%3;
					int cy = i/3;
					g.setColor(endOrder.get(i).getColor());
					g.drawImage(Resources.getRecoloredImage("end", Settings.search, endOrder.get(i).getColor()), (int)(cx*ppx), (int)(cy*ppy), (int)ppx, (int)ppy, null);
				}
			}
		}
		
		for(Direction d : directions)
		{
			drawDirectionLine(imgg, d);
		}
		
		return imgg;
	}
	
	@Override
	public void directionPressed(Direction d)
	{
		if(directions.contains(d))
		{
			if(directions.size()>1)directions.remove(d);
		}
		else
		{
			directions.add(d);
		}
	}
	
	public void addColor(MyColor color)
	{
		if(endOrder.size()<9)
			endOrder.add(color);
	}
	
	public boolean hasDirection(Direction d)
	{
		return directions.contains(d);
	}
	
	public void removeColor()
	{
		if(endOrder.size()>0)endOrder.remove(endOrder.size()-1);
	}
}
