package Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoggerManager {

	List<ILogger> _logs = new ArrayList<ILogger>();
	File _outputPath;
	
	public LoggerManager(File outputPath, int islands) throws IOException
	{
		this(outputPath, islands, true,true);
	}
	
	public LoggerManager(File outputPath, int islands, boolean cmdOut, boolean fileOut) throws IOException
	{
		_outputPath = outputPath;
		
		if (cmdOut)
			_logs.add(new CmdLogger());
		
		if (fileOut)
			_logs.add(new FileLogger(outputPath, islands));
		
	}
	
	public void Log(String str, int island)
	{
		for (ILogger log :_logs)
		{
			try
			{
				log.Log(str, island);
			}
			catch (Exception ex)
			{
				System.out.println("Log error: " + ex.getMessage());
			}
		}
	}
	
	public void Force(int island)
	{
		for (ILogger log :_logs)
		{
			log.Force(island);
		}
	}
	
}
