package coolosity.trainyard.core.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import coolosity.trainyard.core.Direction;
import coolosity.trainyard.core.MyColor;
import coolosity.trainyard.core.piece.EndPiece;
import coolosity.trainyard.core.piece.PaintPiece;
import coolosity.trainyard.core.piece.RockPiece;
import coolosity.trainyard.core.piece.SpawnPiece;
import coolosity.trainyard.core.piece.SplitPiece;
import coolosity.trainyard.core.piece.TrackPiece;
import coolosity.trainyard.display.Drawing;
import coolosity.trainyard.main.TrainMain;
import coolosity.trainyard.solver.trans.TransSetSearch;

public class Game {

	public static Game instance;
	
	private TrainMain main;
	private TrainBoard board;
	private Drawing drawing;
	
	public Game(TrainMain main)
	{
		this.main = main;
		instance = this;
		board = new TrainBoard();
		drawing = new Drawing();
		drawing.setBoard(board);
	}
	
	public void draw(BufferedImage img, int mx, int my)
	{
		drawing.draw(img, mx, my);
		
		if(board.isTesting())
		{
			int cury = 11;
			Graphics g = img.getGraphics();
			Font font = new Font("Arial",Font.PLAIN,14);
			FontMetrics fm = g.getFontMetrics(font);
			g.setFont(font);
			g.setColor(Color.BLACK);
			g.drawString("Testing", 1, cury);
			
			int curx = img.getWidth();
			
			if(board.hasCrashed())
			{
				g.setColor(Color.RED);
				String str = "Crashed";
				curx -= 5+fm.stringWidth(str);
				g.drawString(str, curx, cury);
			}
			
			if(board.isFinished())
			{
				g.setColor(Color.BLACK);
				String str = "Finished";
				curx -= 5+fm.stringWidth(str);
				g.drawString(str, curx, cury);
			}
			
			if(board.isComplete())
			{
				g.setColor(new Color(0, 153, 51));
				String str = "Complete";
				curx -= 5+fm.stringWidth(str);
				g.drawString(str, curx, cury);
			}
		}
	}
	
	public void mousePressed()
	{
		board.mousePressed(drawing.getHoverButton());
	}
	
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		if(!board.isTesting())
		{
			if(code == KeyEvent.VK_1)
			{
				board.setHoverTo(null);
			}
			else if(code == KeyEvent.VK_2)
			{
				board.setHoverTo(new SpawnPiece());
			}
			else if(code == KeyEvent.VK_3)
			{
				board.setHoverTo(new EndPiece());
			}
			else if(code == KeyEvent.VK_4)
			{
				board.setHoverTo(new SplitPiece());
			}
			else if(code == KeyEvent.VK_5)
			{
				board.setHoverTo(new PaintPiece());
			}
			else if(code == KeyEvent.VK_6)
			{
				board.setHoverTo(new TrackPiece());
			}
			else if(code == KeyEvent.VK_7)
			{
				board.setHoverTo(new RockPiece());
			}
			else if(code == KeyEvent.VK_BACK_SPACE)
			{
				board.removeColor();
			}
			else if(code == KeyEvent.VK_ENTER)
			{
				board.enterPressed();
			}
			else if(Direction.fromKeyCode(code) != null)
			{
				board.directionPressed(Direction.fromKeyCode(code));
			}
			else if(MyColor.fromKeyCode(code) != null)
			{
				board.addColor(MyColor.fromKeyCode(code));
			}
		}
		else
		{
			if(code == KeyEvent.VK_ENTER)
			{
				main.setRequestTick(1);
			}
		}
		
		if(code == KeyEvent.VK_SPACE)
		{
			if(board.isTesting())
			{
				board.resetTesting();
			}
			else
			{
				board.startTesting();
			}
		}
		else if(code == KeyEvent.VK_EQUALS)
		{
			if(!board.isTesting())
			{
				board.startTesting();
				main.setRequestTick(35);
			}
		}
		else if(code == KeyEvent.VK_S)
		{
			save();
		}
		else if(code == KeyEvent.VK_N)
		{
			doNew();
		}
		else if(code == KeyEvent.VK_L)
		{
			load();
		}
		else if(code == KeyEvent.VK_MINUS)
		{
			new Thread(new Runnable(){
				public void run(){
					System.out.println("Searching for solution...");
					TransSetSearch search = new TransSetSearch()
					System.out.println("Finished");
				}
			}).start();
		}
	}
	
	public void doNew()
	{
		if(save())
		{
			board = new TrainBoard();
			drawing.setBoard(board);
		}
	}
	
	public boolean save()
	{
		File dir = new File(TrainMain.GAME_DIR+"maps");
		dir.mkdirs();
		String name = main.getInput("Name of map","Save map");
		if(name == null || name.length()==0)
		{
			return false;
		}
		else
		{
			board.save(new File(TrainMain.GAME_DIR+"maps"+"/"+name+".mp"));
			return true;
		}
	}
	
	public void load()
	{
		final String name = main.getInput("Name of map to load","Load map");
		if(name == null || name.length()==0)
		{
			
		}
		else
		{
			new Thread(new Runnable(){
				public void run(){
					board.load(new File(TrainMain.GAME_DIR+"maps"+"/"+name+".mp"));
				}
			}).start();
		}
	}
	
	public void safeTick()
	{
		board.updateTrains();
	}
}
