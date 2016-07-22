package de.tum.in.ma.simpleproject.special;

public class Special implements Comparable<Integer>
{
	public int noReturnStatement()
	{
		throw new IllegalArgumentException();
	}

	@Override
	public int compareTo(Integer o)
	{
		return o;
	}
	
	public int tryCatchBlock(String s)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException ex)
		{
			return Integer.MAX_VALUE;
		}
	}
	
	public int add(int x, int y, int z)
	{
		return x + y + z;
	}
	
	public static int staticMethod()
	{
		return 2;
	}
}
