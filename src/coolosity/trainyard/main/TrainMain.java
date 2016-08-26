package coolosity.trainyard.main;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import coolosity.trainyard.core.game.Game;
import coolosity.trainyard.core.piece.Piece;
import coolosity.trainyard.display.Resources;
import coolosity.trainyard.display.TrainDisplay;

public class TrainMain implements MouseListener, MouseMotionListener, KeyListener
{
	
	public static void main(String[] args)
	{
		new TrainMain();
	}
	
	private static String defaultDirectory()
	{
	    String OS = System.getProperty("os.name").toUpperCase();
	    if (OS.contains("WIN"))
	        return System.getenv("APPDATA");
	    else if (OS.contains("MAC"))
	        return System.getProperty("user.home") + "/Library/Application Support";
	    else if (OS.contains("NUX"))
	        return System.getProperty("user.home");
	    return System.getProperty("user.dir");
	}
	
	public static final String GAME_DIR = defaultDirectory().replace('\\', '/')+"/Trainyard/";
	
	public static TrainMain instance;
	
	private String version = "2.0 Alpha";
	private TrainDisplay display;
	private Game game;
	
	private Point mse = new Point(0,0);
	private boolean running;
	
	private int requestTick;
	
	public TrainMain()
	{
		instance = this;
		System.out.println("Loading Trainyards v"+version);
		System.out.println("Game directory: "+GAME_DIR);
		Resources.loadResources();
		Piece.init();
		System.out.println("Loaded");
		
		display = new TrainDisplay("Trainyard Solver v"+version,this);
		game = new Game(this);
		
		new Thread(new Runnable(){
			public void run(){
				running = true;
				gameLoop();
			}
		}).start();
	}
	
	private void gameLoop()
	{
		while(running)
		{
			while(requestTick>0)
			{
				requestTick--;
				game.safeTick();
			}
			BufferedImage canvas = display.getCanvas();
			game.draw(canvas, mse.x, mse.y);
			display.update(canvas);
		}
	}
	
	public void setRequestTick(int requestTick)
	{
		this.requestTick = requestTick;
	}
	
	public String getInput(String msg, String title)
	{
		return JOptionPane.showInputDialog(display.getFrame(), msg, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	public void showMsg(String msg)
	{
		JOptionPane.showMessageDialog(display.getFrame(), msg);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		Point off = display.getOffset();
		mse.x = e.getX()-off.x;
		mse.y = e.getY()-off.y;
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Point off = display.getOffset();
		mse.x = e.getX()-off.x;
		mse.y = e.getY()-off.y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		game.mousePressed();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		game.keyPressed(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
