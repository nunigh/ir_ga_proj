package Log;

public class CmdLogger implements ILogger {

	@Override
	public void Log(String s, int island) {
		System.out.println(String.format("-- (%s) --", island));
		System.out.println(s);
	}

	@Override
	public void Force(int island) {
		
	}
	
	

}
