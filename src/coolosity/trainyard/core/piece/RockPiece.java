package coolosity.trainyard.core.piece;

import java.awt.image.BufferedImage;

import coolosity.trainyard.display.Resources;
import coolosity.trainyard.utils.ByteReader;
import coolosity.trainyard.utils.ByteWriter;

public class RockPiece extends Piece {

	public RockPiece() {
		super(6);
	}
	
	@Override
	public Piece clone()
	{
		return new RockPiece();
	}

	@Override
	public BufferedImage getImage() {
		return Resources.getImage("rock");
	}

	@Override
	public void saveTo(ByteWriter writer) {}

	@Override
	public void loadFrom(ByteReader reader) {}
}
