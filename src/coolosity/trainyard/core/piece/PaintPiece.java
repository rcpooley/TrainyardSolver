package coolosity.trainyard.core.piece;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.display.Resources;
import coolosity.trainyard.main.Settings;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;
import coolosity.trainyard.utils.Utils;

public class PaintPiece extends Piece {

	private MyColor color;
	private Direction a, b;
	private int last = 1;
	
	private MyColor tempInside;
	
	public PaintPiece()
	{
		super(3);
		this.color = MyColor.RED;
		this.a = Direction.LEFT;
		this.b = Direction.RIGHT;
	}
	
	@Override
	public Piece clone()
	{
		PaintPiece p = new PaintPiece();
		p.color = color;
		p.a = a;
		p.b = b;
		return p;
	}
	
	public void setTempInside(MyColor tempInside)
	{
		this.tempInside = tempInside;
	}
	
	public Direction getConnection(Direction d)
	{
		if(a==d)return b;
		return a;
	}
	
	public boolean connectsTo(Direction d)
	{
		return a==d || b==d;
	}
	
	public MyColor getColor()
	{
		return color;
	}

	@Override
	public void saveTo(ByteWriter writer) {
		writer.writeInt(color.ordinal(), 1);
		writer.writeInt(a.ordinal(), 1);
		writer.writeInt(b.ordinal(), 1);
	}

	@Override
	public void loadFrom(ByteReader reader) {
		color = MyColor.values()[reader.readInt(1)];
		a = Direction.values()[reader.readInt(1)];
		b = Direction.values()[reader.readInt(1)];
	}

	@Override
	public BufferedImage getImage()
	{
		BufferedImage img = Utils.cloneImage(Resources.getRecoloredImage("brush", Settings.search, color.getColor()));
		drawDirectionLine(img,a);
		drawDirectionLine(img,b);
		if(tempInside != null)
		{
			Graphics g = img.getGraphics();
			g.setColor(tempInside.getColor());
			g.fillRect(img.getWidth()/4, img.getHeight()/4, img.getWidth()/2, img.getHeight()/2);
		}
		return img;
	}
	
	@Override
	public void directionPressed(Direction d)
	{
		if(last==1)
		{
			if(b!=d)a = d;
		}
		else
		{
			if(a!=d)b = d;
		}
		last = 1-last;
	}
	
	public void setColor(MyColor color)
	{
		this.color = color;
	}
}
