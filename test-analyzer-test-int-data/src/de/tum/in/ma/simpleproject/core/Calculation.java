package de.tum.in.ma.simpleproject.core;

import java.util.LinkedList;
import java.util.List;

import de.tum.in.ma.simpleproject.lite.CalculationLite;

public class Calculation extends CalculationLite
{
	public Calculation()
	{
		this(0);
	}
	
	public Calculation(int seed)
	{
		setResult(seed);
	}
	
	public static Calculation parse(String s)
	{
		return new Calculation(Integer.parseInt(trimString(s)));
	}
	
	private static String trimString(String s)
	{
		return s.trim();
	}
	
	public int[] getResultAsArray()
	{
		return new int[] { getResult() };
	}
	
	public List<Integer> getResultAsList()
	{
		List<Integer> list = new LinkedList<>();
		list.add(getResult());
		return list;
	}

	public boolean isPositive()
	{
		return isPositive(getResult());
	}
	
	public void sub(int x)
	{
		add(-x);
	}

	public void mult(int x)
	{
		if (x == 0)
		{
			setResult(0);
		}
		else if (!isPositive(x))
		{
			mult(-x);
			setResult(-(getResult())); 
		}
		else
		{
			int temp = 0;
			
			for (int i = 0; i < x; i++)
			{
				temp += getResult();
			}
			
			setResult(temp);
		}
	}

	public void clear()
	{
		setResult(0);
	}
	
	private static boolean isPositive(int x)
	{
		return x >= 0;
	}
	
	public Comparable<Calculation> getComparable()
	{
		return new Comparable<Calculation>()
		{
			@Override
			public int compareTo(Calculation o)
			{
				return 0;
			}
		};
	}
}
