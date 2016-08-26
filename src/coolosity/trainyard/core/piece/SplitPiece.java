package coolosity.trainyard.core.piece;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.display.Resources;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;
import coolosity.trainyard.utils.Utils;

public class SplitPiece extends Piece {

	private Direction direction;
	
	private MyColor tempInside;
	
	public SplitPiece()
	{
		super(4);
		this.direction = Direction.DOWN;
	}
	
	@Override
	public Piece clone()
	{
		SplitPiece p = new SplitPiece();
		p.direction = direction;
		return p;
	}
	
	public void setTempInside(MyColor tempInside)
	{
		this.tempInside = tempInside;
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage img = Utils.cloneImage(Resources.getImage("split"));
		drawDirectionLine(img,Direction.DOWN);
		
		if(tempInside != null)
		{
			Graphics g = img.getGraphics();
			g.setColor(tempInside.getColor());
			g.fillRect(img.getWidth()/4, img.getHeight()/4, img.getWidth()/2, img.getHeight()/2);
		}
		
		return Utils.rotate(img, direction.opposite().ordinal());
	}
	
	@Override
	public void directionPressed(Direction d)
	{
		direction = d;
	}
	
	public Direction getDirection()
	{
		return direction;
	}

	@Override
	public void saveTo(ByteWriter writer) {
		writer.writeInt(direction.ordinal(), 1);
	}

	@Override
	public void loadFrom(ByteReader reader) {
		direction = Direction.values()[reader.readInt(1)];
	}
}
