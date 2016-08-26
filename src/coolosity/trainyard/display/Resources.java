package coolosity.trainyard.display;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import coolosity.trainyard.utils.Utils;

public class Resources {
	
	private static final String[] toload = {"rock","brush","end","spawn","split","straight","turn"};
	
	private static HashMap<String,BufferedImage> images;

	
	private static ArrayList<Cache> caches;

	private static class Cache
	{
		public String str;
		public Color search;
		public Color replace;
		public BufferedImage result;
		
		public Cache(String str, Color search, Color replace, BufferedImage result)
		{
			this.str = str;
			this.search = search;
			this.replace = replace;
			this.result = result;
		}
	}
	
	public static void loadResources()
	{
		images = new HashMap<String,BufferedImage>();
		caches = new ArrayList<Cache>();
		
		for(String s : toload)
		{
			images.put(s, loadImage(s));
		}
	}
	
	private static BufferedImage loadImage(String s)
	{
		try
		{
			//return ImageIO.read(new File("res/"+s+".png"));
			return ImageIO.read(Resources.class.getResourceAsStream("/res/"+s+".png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage getImage(String str)
	{
		return images.get(str);
	}
	
	public static BufferedImage getRecoloredImage(String str, Color search, Color replace)
	{
		for(Cache c : caches)
		{
			if(c.str.equals(str) && c.search.getRGB()==search.getRGB() && c.replace.getRGB()==replace.getRGB())return c.result;
		}
		BufferedImage res = Utils.replaceColor(getImage(str), search, replace);
		Cache c = new Cache(str,search,replace,res);
		caches.add(c);
		return res;
	}
}
