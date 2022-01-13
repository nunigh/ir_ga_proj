package Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger implements ILogger{

	BufferedWriter[] out = null;
	FileWriter[] writer; 
	//FileOutputStream[] f;
	private int _islands = 0;
	public FileLogger(File outputPath, int islands) throws IOException
	{
		_islands = islands;
		writer = new FileWriter[2*islands+1];
		out = new BufferedWriter[2*islands+1];
		//f = new FileOutputStream[2*islands+1];
			
	    SimpleDateFormat sdfr = new SimpleDateFormat("HHmmss");
	    String  dateString = sdfr.format( new Date() );
		   
		for (int i = 0; i < islands; i++) {
			writer[i] = new FileWriter(String.format("%s%s%s%d%s%s", outputPath.getAbsolutePath(), File.separatorChar, "out_",i, dateString, ".txt"));
			out[i] = new BufferedWriter(writer[i]);
			//f[i] = new FileOutputStream(String.format("%s%s%s%d%s", outputPath.getAbsolutePath(), File.separatorChar, "out_",i,".txt"));
		}
		for (int i = islands; i < 2*islands; i++) {
			writer[i] = new FileWriter(String.format("%s%s%s%d%s", outputPath.getAbsolutePath(), File.separatorChar, "out_short_",i,".txt"));
			out[i] = new BufferedWriter(writer[i]);
			//f[i] = new FileOutputStream(String.format("%s%s%s%d%s", outputPath.getAbsolutePath(), File.separatorChar, "out_short_",i,".txt"));
		} 
		writer[2*islands] = new FileWriter(String.format("%s%sout_general.txt", outputPath.getAbsolutePath(), File.separatorChar));
		out[2*islands] = new BufferedWriter(writer[2*islands]);
		//f[2*islands] = new FileOutputStream(String.format("%s%sout_general.txt", outputPath.getAbsolutePath(), File.separatorChar));
	}
	
	@Override
	public void Log(String s, int island) throws IOException {
		if (island < 0) island = out.length-1; // general
		//if (island < 0) island = f.length-1; // general
		
		if (s.startsWith("*")) island = _islands + island; // short
		
		//f[island].write(s.to)
		//f[island].flush();
		out[island].write(s);
		out[island].flush();
	}
	
	

	@Override
	public void Force(int island) {
		try
		{			
			//f[island].getFD().sync();
		}
		finally
		{}
	}

	@Override
	protected void finalize() throws Throwable {
		for (int j = 0; j < out.length; j++) {
			if (out[j] != null) out[j].close();	
		}
		
		//for (int j = 0; j < f.length; j++) {
		//	if (f[j] != null) f[j].close();	
		//}
		
		super.finalize();
	}
	
	

	
}
