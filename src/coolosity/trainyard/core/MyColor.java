package coolosity.trainyard.core;

import java.awt.Color;
import java.awt.event.KeyEvent;

public enum MyColor {

	RED(Color.RED,KeyEvent.VK_R),
	ORANGE(Color.ORANGE,KeyEvent.VK_O),
	YELLOW(Color.YELLOW,KeyEvent.VK_Y),
	GREEN(Color.GREEN,KeyEvent.VK_G),
	BLUE(Color.BLUE,KeyEvent.VK_B),
	PURPLE(new Color(102,0,204),KeyEvent.VK_P),
	BROWN(new Color(77,51,0),KeyEvent.VK_V);
	
	private Color color;
	private int keyCode;
	
	MyColor(Color color, int keyCode)
	{
		this.color = color;
		this.keyCode = keyCode;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public MyColor combine(MyColor other)
	{
		if(this==other)return this;
		if(matches(RED,BLUE,other))
			return PURPLE;
		else if(matches(RED,YELLOW,other))
			return ORANGE;
		else if(matches(BLUE,YELLOW,other))
			return GREEN;
		return BROWN;
	}
	
	public MyColor[] getSplit()
	{
		if(this==RED || this==BLUE || this==YELLOW || this==BROWN)return new MyColor[]{this,this};
		if(this==PURPLE)return new MyColor[]{BLUE,RED};
		if(this==GREEN)return new MyColor[]{BLUE,YELLOW};
		if(this==ORANGE)return new MyColor[]{YELLOW,RED};
		return null;
	}
	
	private boolean matches(MyColor a, MyColor b, MyColor other)
	{
		return this==a && other==b || this==b && other==a;
	}
	
	public static MyColor fromKeyCode(int keyCode)
	{
		for(MyColor mc : MyColor.values())
		{
			if(mc.keyCode==keyCode)return mc;
		}
		return null;
	}
}
