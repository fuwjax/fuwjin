using System;
namespace Tweed.LD.Function
{
	public class Function: IFunction
	{
		public static readonly Function Null = null; 
		
		public Function Join (Function function)
		{
			throw new System.NotImplementedException ();
		}

		public Function ()
		{
		}
		
		public object Invoke(params object[] args){
			return null;
		}
		
		public string Name{
			get{
			return "hi";
			}
		}
		
		public Function Restrict (IFunctionSignature signature)
		{
			throw new System.NotImplementedException ();
		}

	}
}

