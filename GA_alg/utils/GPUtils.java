package utils;

import java.lang.reflect.Constructor;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import GP.Structure.Concrete.Constant;
import GP.Structure.Concrete.Cosine;
import GP.Structure.Concrete.Divide;
import GP.Structure.Concrete.E;
import GP.Structure.Concrete.IMQ;
import GP.Structure.Concrete.MQ;
import GP.Structure.Concrete.Minus;
import GP.Structure.Concrete.Multiply;
import GP.Structure.Concrete.Plus;
import GP.Structure.Concrete.Power;
import GP.Structure.Concrete.Rotation;
import GP.Structure.Concrete.Sine;
import GP.Structure.Concrete.Variable;
import GP.Structure.Interface.INode;

	
@SuppressWarnings("rawtypes")
public class GPUtils {

	private  static Random rand = new Random();
	
	// arrays of the possible operators and possible nullary operators
	public static final Class[] operators = { Constant.class,  Plus.class, Minus.class, Variable.class,  Multiply.class, Divide.class,Cosine.class, Sine.class, Rotation.class, Power.class};//,  IMQ.class, MQ.class, E.class};
	public static final Class[] nullaryOperators = { Constant.class, Variable.class, E.class};
	public static final Class[] binaryOperators = { Plus.class, Minus.class, Multiply.class, Divide.class, Power.class};
	public static final Class[] thirdOperators = { };//IMQ.class, MQ.class};
	//public static final Class[] MultiOperators = { IMQ.class, MQ.class, Minus.class,Plus.class, Multiply.class, Divide.class, Power.class, Cosine.class, Sine.class, Rotation.class};
	public static final Class[] MultiOperators = { Plus.class, Minus.class, Multiply.class, Divide.class, Cosine.class, Sine.class,Constant.class, Rotation.class, Power.class};//, IMQ.class, MQ.class};
	public static final Class[] unaryOperators = { Cosine.class, Sine.class, Rotation.class}; 
	public static final Class[] validateOperators = { Plus.class, Minus.class, Constant.class, Multiply.class, Divide.class,Cosine.class, Sine.class, Rotation.class, Power.class};//, IMQ.class, MQ.class};

	/**
	 * @return random operator node
	 */
	@SuppressWarnings("unchecked")
	public static INode getRandomOperator(int i) {
		INode node = null;
		int index;
		try {
			index = rand.nextInt(operators.length);
			Class<INode> c = operators[index];
				
			Constructor<INode> cc = c.getConstructor(Integer.TYPE);
			node = cc.newInstance(i);
		
			
			
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return node;
	}
	
	/**
	 * @return random nullary operator (constant or variable)
	 */
	public static INode getRandomNullaryOperator(int i) {
		INode node = null;
		int index = 0;
		try {
			//index = rand.nextInt(nullaryOperators.length);			
			if (rand.nextDouble() > 0.8) // $$
				index = 1;
			@SuppressWarnings("unchecked")
			Class<INode> c = nullaryOperators[index];
			//node = c.newInstance();
			
			Constructor<INode> cc = c.getConstructor(Integer.TYPE);
			node = cc.newInstance(i);
			
		} catch (Exception e) {	
			e.printStackTrace(); 
		}
		return node;
	}
	
	/**
	 * @return random nullary operator (constant or variable)
	 */
	public static INode getRandomBinaryOperator(int i) {
		INode node = null;
		try {
			int index = rand.nextInt(binaryOperators.length);
			@SuppressWarnings("unchecked")
			Class<INode> c = binaryOperators[index];
			Constructor<INode> cc = c.getConstructor(Integer.TYPE);
			node = cc.newInstance(i);
			
		} catch (Exception e) {	
			e.printStackTrace(); 
		}
		return node;
	}
	
	/**
	 * @return random nullary operator (constant or variable)
	 */
	public static INode getRandomUnaryOperator(int i) {
		INode node = null;
		try {
			int index = rand.nextInt(unaryOperators.length);
			@SuppressWarnings("unchecked")
			Class<INode> c = unaryOperators[index];
			Constructor<INode> cc = c.getConstructor(Integer.TYPE);
			node = cc.newInstance(i);
		} catch (Exception e) {	
			e.printStackTrace(); 
		}
		return node;
	}
	
	/**
	 * @return random nullary operator (constant or variable)
	 */
	public static INode getRandomMultiOperator(int i) {
		INode node = null;
		try {
			int index = rand.nextInt(MultiOperators.length);
			@SuppressWarnings("unchecked")
			Class<INode> c = MultiOperators[index];
			Constructor<INode> cc = c.getConstructor(Integer.TYPE);
			node = cc.newInstance(i);
			
		} catch (Exception e) {	
			e.printStackTrace(); 
		}
		return node;
	}
	
	/**
	 * @return random nullary operator (constant or variable)
	 */
public static INode getRandomThirdOperator(int i) {
		INode node = null;
		try {
			int index = rand.nextInt(thirdOperators.length);
			@SuppressWarnings("unchecked")
			Class<INode> c = thirdOperators[index];
			Constructor<INode> cc = c.getConstructor(Integer.TYPE);
			node = cc.newInstance(i);
			
		} catch (Exception e) {	
			e.printStackTrace(); 
		}
		return node;
	}

public static INode getRandomValidOperator(int i) {
	INode node = null;
	try {
		int index = rand.nextInt(validateOperators.length);
		@SuppressWarnings("unchecked")
		Class<INode> c = validateOperators[index];
		Constructor<INode> cc = c.getConstructor(Integer.TYPE);
		node = cc.newInstance(i);
		
	} catch (Exception e) {	
		e.printStackTrace(); 
	}
	return node;
}

public static boolean IsValid(INode old)
{
	for (int i=0; i< validateOperators.length; i++)
	{
		String a = validateOperators[i].getName();
		if (old.getClass().getName().compareTo(a)==0)
			return true;
	}
	return false;
}

public static INode GetValidNode(int index, INode old)
{
	old = getRandomValidOperator(index);
	old.generateSubTree(index, 1);
	return old;
}

public static double[] GetValue(String sx, String sy, double x, double y) throws ScriptException
{
	double[] newPos = new double[2];
	
	//newPosX = GPTransformations.GetValue(sx, sensedPoints.get(j).x, sensedPoints.get(j).y);
	newPos[0] = GetValue(sx, x, y);
	//newPosY = GPTransformations.GetValue(sy, sensedPoints.get(j).x, sensedPoints.get(j).y);
	newPos[1] = GetValue(sy, x, y);
	
	return newPos;
}

public static double GetValue(String f, double col, double row) throws ScriptException
{
 ScriptEngineManager mgr = new ScriptEngineManager();
	ScriptEngine engine = mgr.getEngineByName("JavaScript");

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
	
}
