package coolosity.trainyard.core;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public enum Direction {

	UP(KeyEvent.VK_UP),
	RIGHT(KeyEvent.VK_RIGHT),
	DOWN(KeyEvent.VK_DOWN),
	LEFT(KeyEvent.VK_LEFT),
	NONE(-1);
	
	public static Direction[] keyValues()
	{
		return new Direction[]{UP,RIGHT,DOWN,LEFT};
	}
	
	public static ArrayList<Direction[]> combosOf(int num)
	{
		ArrayList<Direction[]> ans = new ArrayList<Direction[]>();
		calcCombosOf(new Direction[num],0,ans);
		return ans;
	}
	
	private static void calcCombosOf(Direction[] cur, int ind, ArrayList<Direction[]> combos)
	{
		if(ind==cur.length)
		{
			combos.add(cur);
			return;
		}
		
		ArrayList<Direction> all = new ArrayList<Direction>();
		for(Direction d : keyValues())all.add(d);
		for(int i=0;i<ind;i++)
		{
			all.remove(cur[i]);
		}
		for(Direction d : all)
		{
			Direction[] clone = new Direction[cur.length];
			for(int i=0;i<ind;i++)
			{
				clone[i] = cur[i];
			}
			clone[ind] = d;
			calcCombosOf(clone,ind+1,combos);
		}
	}
	
	private int keyCode;
	
	Direction(int keyCode)
	{
		this.keyCode = keyCode;
	}
	
	public Direction opposite()
	{
		if(this==UP)return DOWN;
		else if(this==DOWN)return UP;
		else if(this==RIGHT)return LEFT;
		else if(this==LEFT)return RIGHT;
		return null;
	}
	
	public Direction ccw()
	{
		if(this==UP)return LEFT;
		else if(this==DOWN)return RIGHT;
		else if(this==RIGHT)return UP;
		else if(this==LEFT)return DOWN;
		return null;
	}
	
	public Direction cw()
	{
		return ccw().opposite();
	}
	
	public Point applyTo(Point pp)
	{
		Point p = new Point(pp.x,pp.y);
		if(this==UP)p.y--;
		else if(this==DOWN)p.y++;
		else if(this==LEFT)p.x--;
		else if(this==RIGHT)p.x++;
		return p;
	}
	
	public static Direction fromKeyCode(int keyCode)
	{
		for(Direction d : Direction.values())
		{
			if(d.keyCode==keyCode)return d;
		}
		return null;
	}
}
