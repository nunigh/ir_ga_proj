package utils;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class GPTransformations {

	//##########################   RMSE     ############################################
	
		public static double[] T(String fx, String fy, double col, double row) throws ScriptException
		{		
			double newCol = GetValue(fx, col, row);
			double newRow = GetValue(fy, col, row);
			
			return new double[] { newCol, newRow };
		}
		
		static ScriptEngineManager mgr = new ScriptEngineManager();
		static ScriptEngine engine = mgr.getEngineByName("JavaScript");
		public static double GetValue(String f, double col, double row) throws ScriptException
		{
			double val = 0;
			try
			{
				engine.put("x", col);
				engine.put("y", row);
			    String foo = f;
				Object o = engine.eval(foo);
			    val = Double.parseDouble(o.toString());
			}
			catch (Exception e) {	
				System.out.print(e.getMessage());
			}
		    return val;
		}
		
		@SuppressWarnings("unused")
		private static int[] T(int col, int row) throws ScriptException
		{		
			int newCol = (int) Math.round(GetValue(0, col, row));
			int newRow = (int) Math.round(GetValue(1, col, row));	
			return new int[] { newCol, newRow };
		}
		
		public static CompiledScript scriptX = null;
		public static CompiledScript scriptY = null;
		static Bindings bindings = engine.createBindings();
		public static Compilable compilingEngine = (Compilable)engine;
		public static double GetValue(int index, double col, double row) throws ScriptException
		{
			bindings.clear();
			bindings.put("x", col);
			bindings.put("y", row);
			Object o = null;
			if (index == 0)
				o = scriptX.eval(bindings);
			else
				o = scriptY.eval(bindings);
		    double val = Double.parseDouble(o.toString());
		    return val;
		}
		
}
