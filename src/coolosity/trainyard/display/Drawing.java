package coolosity.trainyard.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import coolosity.trainyard.core.game.Train;
import coolosity.trainyard.core.game.TrainBoard;
import coolosity.trainyard.core.piece.EndPiece;
import coolosity.trainyard.core.piece.SpawnPiece;
import coolosity.trainyard.core.piece.TrackPiece;

public class Drawing {
	
	public static final int BUTTON_NEW = 0;
	public static final int BUTTON_LOAD = 1;
	public static final int BUTTON_SAVE = 2;
	public static final int BUTTON_ONLINE = 3;
	
	private TrainBoard board;
	private int hoverButton;
	
	public Drawing()
	{
		hoverButton = -1;
	}
	
	public void setBoard(TrainBoard board)
	{
		this.board = board;
	}
	
	public void draw(BufferedImage ori, int mx, int my)
	{
		if(board==null)return;
		Graphics2D gg = (Graphics2D) ori.getGraphics();
		gg.setColor(new Color(230,230,230));
		gg.fillRect(0, 0, ori.getWidth(), ori.getHeight());
		int bor = 15;

		int wid = ori.getWidth()-bor*2;
		int boardy = ori.getHeight()-bor-wid;
		drawHeader(ori.getSubimage(0, 0, ori.getWidth(), boardy),mx,my);
		
		BufferedImage img = ori.getSubimage(bor, boardy, wid, wid);
		drawBoard(img,mx-bor,my-boardy);
		
		//grid
		double ppx = img.getWidth()*1.0/board.getWidth();
		double ppy = img.getHeight()*1.0/board.getHeight();
		gg.setStroke(new BasicStroke(2));
		gg.setColor(Color.BLACK);
		for(int x=0;x<=board.getWidth();x++)
		{
			gg.drawLine(bor+(int)(x*ppx), boardy, bor+(int)(x*ppx), boardy+img.getHeight());
		}
		for(int y=0;y<=board.getHeight();y++)
		{
			gg.drawLine(bor, boardy+(int)(y*ppy), bor+img.getWidth(), boardy+(int)(y*ppy));
		}
	}
	
	private void drawHeader(BufferedImage img, int mx, int my)
	{
		int xbor = 5;
		int ybor = 7;
		int bwid = 70;
		int curx = xbor;
		
		int hoverButton = -1;
		
		Object[] buts = {
				"New", BUTTON_NEW,
				"Load", BUTTON_LOAD,
				"Save", BUTTON_SAVE
		};
		
		for(int i=0;i<buts.length;i+=2)
		{
			if(drawButton(buts[i].toString(), img.getSubimage(curx, ybor, bwid, img.getHeight()-ybor*2), mx-curx, my-ybor, 30))
			{
				hoverButton = (int)buts[i+1];
			}
			curx += bwid+xbor;
		}
		
		curx = img.getWidth()-xbor-bwid;
		if(drawButton("Online",img.getSubimage(curx, ybor, bwid, img.getHeight()-ybor*2),mx-curx,my-ybor,30))
		{
			hoverButton = BUTTON_ONLINE;
		}
		this.hoverButton = hoverButton;
	}
	
	private boolean drawButton(String txt, BufferedImage img, int mx, int my, int arc)
	{
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.RED);
		boolean hover = false;
		if(mx>=0 && my>=0 && mx<img.getWidth() && my<img.getHeight())
		{
			g.setColor(Color.GREEN);
			hover = true;
		}
		g.fillRoundRect(0, 0, img.getWidth(), img.getHeight(),arc,arc);

		Font f = new Font("Arial",Font.PLAIN,18);
		FontMetrics fm = g.getFontMetrics(f);
		
		g.setColor(Color.BLACK);
		g.setFont(f);
		
		g.drawString(txt, (img.getWidth()-fm.stringWidth(txt))/2, fm.getHeight()-2);
		return hover;
	}
	
	private void drawBoard(BufferedImage img, int mx, int my)
	{
		ArrayList<Train> tempTrains = board.getTrains();
		
		Graphics2D g = (Graphics2D) img.getGraphics();
		double ppx = img.getWidth()*1.0/board.getWidth();
		double ppy = img.getHeight()*1.0/board.getHeight();
		int w = (int)ppx+1;
		int h = (int)ppy+1;
		
		//draw board
		Point hover = null;
		for(int x=0;x<board.getWidth();x++)
		{
			for(int y=0;y<board.getHeight();y++)
			{
				int xx = (int)(x*ppx);
				int yy = (int)(y*ppy);
				
				if(mx>=xx && mx<xx+w && my>=yy && my<yy+h)
				{
					hover = new Point(x,y);
				}
				
				if(board.pieceAt(x, y) != null)
				{
					if(board.pieceAt(x,y) instanceof TrackPiece)
					{
						((TrackPiece)board.pieceAt(x,y)).setTempInvert(board.getData(x, y)==1);
					}
					else if(board.pieceAt(x,y) instanceof SpawnPiece)
					{
						((SpawnPiece)board.pieceAt(x,y)).setTempSent(board.getData(x, y));
					}
					else if(board.pieceAt(x,y) instanceof EndPiece)
					{
						ArrayList<Integer> end = board.getEndData(x, y);
						((EndPiece)board.pieceAt(x,y)).setTempFinished(end.size()>0?end:null);
					}
					BufferedImage imgg = board.pieceAt(x,y).getImage();
					if(imgg != null)
					{
						g.drawImage(imgg, xx, yy, w, h, null);
					}
				}
			}
		}
		board.setHover(hover);
		
		//draw trains
		int tsize = 25;
		int toff = (int)(ppx/2-tsize/2)-1;
		for(Train train : tempTrains)
		{
			if(train!=null)
			{
				Point p = train.getDirection().applyTo(new Point(0,0));
				int xx = (int)((train.getX()+0.5)*ppx)+p.x*toff-tsize/2;
				int yy = (int)((train.getY()+0.5)*ppy)+p.y*toff-tsize/2;
				g.setColor(train.getColor().getColor());
				g.fillRect(xx, yy, tsize, tsize);
			}
		}
		
		//draw hover square
		if(hover != null)
		{
			int xx = (int)(hover.x*ppx);
			int yy = (int)(hover.y*ppy);
			g.setColor(new Color(0,0,255,128));
			g.fillRect(xx, yy, w, h);
		}
	}
	
	public int getHoverButton()
	{
		return hoverButton;
	}
}
