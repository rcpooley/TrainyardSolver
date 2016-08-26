package coolosity.trainyard.utils;


public class ByteReader {

	private byte[] bytes;
	private int pos;
	
	public ByteReader(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public int readInt(int nb)
	{
		int sum = 0;
		for(int i=0;i<nb;i++)
		{
			int val = (int) Math.pow(256, nb-i-1);
			sum += (bytes[pos++]+128)*val;
		}
		return sum;
	}
	
	public int readSignedInt(int nb)
	{
		int minVal = (int) (Math.pow(256, nb)/2);
		return readInt(nb)-minVal;
	}
	
	public double readDouble(int nb, int numDecimals)
	{
		double sum = readInt(nb);
		int maxVal = (int) Math.pow(10,numDecimals);
		nb = 1;
		while(Math.pow(256, nb)<=maxVal)nb++;
		int deci = readInt(nb);
		sum += deci*1.0/maxVal;
		return sum;
	}
	
	public long readLong(int nb)
	{
		long sum = 0;
		for(int i=0;i<nb;i++)
		{
			long val = (long) Math.pow(256, nb-i-1);
			sum += (bytes[pos++]+128)*val;
		}
		return sum;
	}
	
	public byte[] readBytes(byte[] arr)
	{
		for(int i=0;i<arr.length;i++)
		{
			arr[i] = bytes[pos++];
		}
		return arr;
	}
	
	public String readString()
	{
		int length = readInt(3);
		return new String(readBytes(new byte[length]));
	}
	
	public boolean hasNext()
	{
		return pos<bytes.length;
	}
	
	public ByteWriter toByteWriter()
	{
		ByteWriter writer = new ByteWriter();
		writer.writeBytes(bytes);
		return writer;
	}
	
	public int numBytes()
	{
		return bytes.length;
	}
}
