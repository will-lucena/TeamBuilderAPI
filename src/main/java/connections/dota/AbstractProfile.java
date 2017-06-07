package connections.dota;

public class AbstractProfile
{
	private String name;
	private long id;
	private long level;

	public AbstractProfile(String name, long id, long level) {
		super();
		this.name = name;
		this.id = id;
		this.level = level;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getLevel()
	{
		return level;
	}

	public void setLevel(long level)
	{
		this.level = level;
	}
}
