package coolosity.trainyard.utils;

import java.util.ArrayList;

public class ByteWriter {

	private ArrayList<Byte> bytes;
	
	public ByteWriter()
	{
		bytes = new ArrayList<Byte>();
	}
	
	public void writeInt(int v, int nb)
	{
		for(int i=0;i<nb;i++)
		{
			int val = (int) Math.pow(256, nb-i-1);
			int todo = v/val;
			v -= todo*val;
			byte b = (byte) (todo-128);
			bytes.add(b);
		}
	}
	
	public void writeSignedInt(int v, int nb)
	{
		int minVal = (int) (Math.pow(256, nb)/2);
		writeInt(v+minVal,nb);
	}
	
	public void writeDouble(double d, int nb, int numDecimals)
	{
		int di = (int)d;
		writeInt(di,nb);
		int toWrite = (int)((d-di)*Math.pow(10, numDecimals));
		int maxVal = (int) Math.pow(10,numDecimals);
		nb = 1;
		while(Math.pow(256, nb)<=maxVal)nb++;
		writeInt(toWrite,nb);
	}
	
	public void writeLong(long v, int nb)
	{
		for(int i=0;i<nb;i++)
		{
			long val = (long) Math.pow(256, nb-i-1);
			long todo = v/val;
			v -= todo*val;
			byte b = (byte) (todo-128);
			bytes.add(b);
		}
	}
	
	public void writeBytes(byte[] bs)
	{
		for(byte b : bs)
			bytes.add(b);
	}
	
	public void writeString(String str)
	{
		byte[] bts = str.getBytes();
		writeInt(bts.length,3);
		writeBytes(bts);
	}
	
	public byte[] getBytes()
	{
		byte[] arr = new byte[bytes.size()];
		for(int i=0;i<arr.length;i++)
			arr[i] = bytes.get(i);
		return arr;
	}
}
