package coolosity.trainyard.display;

import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import coolosity.trainyard.main.TrainMain;

public class TrainDisplay {

	private JFrame frame;
	private JLabel label;
	
	public TrainDisplay(String title, TrainMain input)
	{
		frame = new JFrame(title);
		frame.setSize(600, 650);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addMouseListener(input);
		frame.addMouseMotionListener(input);
		frame.addKeyListener(input);
		label = new JLabel();
		frame.add(label);
		frame.setVisible(true);
	}
	
	public BufferedImage getCanvas()
	{
		return new BufferedImage(label.getWidth(),label.getHeight(),BufferedImage.TYPE_INT_ARGB);
	}
	
	public void update(BufferedImage img)
	{
		label.setIcon(new ImageIcon(img));
	}
	
	public Point getOffset()
	{
		int xoff = (frame.getWidth()-label.getWidth())/2;
		int yoff = frame.getHeight()-label.getHeight()-xoff;
		return new Point(xoff,yoff);
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
}
