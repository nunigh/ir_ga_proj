package Log;

public interface ILogger {

	void Log(String s, int island) throws Exception;
	
	void Force(int island);
}
