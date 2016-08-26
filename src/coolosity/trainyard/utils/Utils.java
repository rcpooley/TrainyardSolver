package coolosity.trainyard.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Utils {

	public static BufferedImage rotate(BufferedImage img, int dir)
	{
		BufferedImage fin = null;
		switch(dir)
		{
		case 0:
			fin = img;
			break;
		case 1:
			fin = new BufferedImage(img.getHeight(),img.getWidth(),img.getType());
			for(int x=0;x<img.getWidth();x++)
			{
				for(int y=0;y<img.getHeight();y++)
				{
					fin.setRGB(img.getHeight()-y-1, x, img.getRGB(x, y));
				}
			}
			break;
		case 2:
			fin = new BufferedImage(img.getWidth(),img.getHeight(),img.getType());
			for(int x=0;x<img.getWidth();x++)
			{
				for(int y=0;y<img.getHeight();y++)
				{
					fin.setRGB(img.getWidth()-x-1, img.getHeight()-y-1, img.getRGB(x, y));
				}
			}
			break;
		case 3:
			fin = new BufferedImage(img.getHeight(),img.getWidth(),img.getType());
			for(int x=0;x<img.getWidth();x++)
			{
				for(int y=0;y<img.getHeight();y++)
				{
					fin.setRGB(y, img.getWidth()-x-1, img.getRGB(x, y));
				}
			}
			break;
		}
		return fin;
	}

	public static BufferedImage cloneImage(BufferedImage img)
	{
		BufferedImage fin = new BufferedImage(img.getWidth(),img.getHeight(),img.getType());
		for(int x=0;x<img.getWidth();x++)
		{
			for(int y=0;y<img.getHeight();y++)
			{
				fin.setRGB(x, y, img.getRGB(x, y));
			}
		}
		return fin;
	}
	
	public static BufferedImage replaceColor(BufferedImage img, Color search, Color replace)
	{
		BufferedImage fin = new BufferedImage(img.getWidth(),img.getHeight(),img.getType());
		for(int x=0;x<img.getWidth();x++)
		{
			for(int y=0;y<img.getHeight();y++)
			{
				Color c = new Color(img.getRGB(x, y), true);
				if(c.getRGB()==search.getRGB())
					fin.setRGB(x, y, replace.getRGB());
				else
					fin.setRGB(x, y, c.getRGB());
			}
		}
		return fin;
	}
}
