package exceptions;

public class ConnectionException extends Exception
{
	private Exception exception;
	private String problem;
	
	public ConnectionException(String msg, Exception e)
	{
		super(msg);
		this.exception = e;
	}
	
	public ConnectionException(String msg, String o)
	{
		super(msg);
		this.problem = o;
	}
	
	public ConnectionException(String msg)
	{
		super(msg);
	}	
	
	public Exception getException()
	{
		return exception;
	}
	
	public String getProblem()
	{
		return problem;
	}
}
