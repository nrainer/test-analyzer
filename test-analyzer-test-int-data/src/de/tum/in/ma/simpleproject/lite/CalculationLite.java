package de.tum.in.ma.simpleproject.lite;

public class CalculationLite
{
	private int result;

	public CalculationLite()
	{
		setResult(0);
	}
	
	protected void setResult(int x)
	{
		this.result = x;
	}

	public int getResult()
	{
		return this.result;
	}

	public String getResultAsString()
	{
		return String.valueOf(getResult());
	}

	public boolean isEven()
	{
		return getResult() % 2 == 0;
	}

	public void add(int x)
	{
		setResult(getResult() + x);
	}

	public void increment()
	{
		add(1);
	}
}
