using System;
using System.Collections.Generic;
using System.Reflection;
using Tweed.LD.Function;

namespace Tweed.LD.Provider
{
	public abstract class FunctionProvider: IFunctionProvider
	{
		protected static void Add(IDictionary<string, Function.Function> functions, Function.Function function){
			string name = function.Name;
			Function.Function func = functions[name];
			if(func == null) {
				functions[name] = function;
			} else {
				functions[name] = func.Join(function);
			}
		}
		
		public IFunction GetFunction(IFunctionSignature signature){
			Function.Function function = GetFunctions(signature.Category)[signature.Name];
			if(function == null){
				throw new NoSuchFunctionException("No function found for %s",signature);
			}
			function = function.Restrict(signature);
			if(Function.Function.Null.Equals(function)){
				throw new NoSuchFunctionException("No function matches arguments for %s",signature);
			}
			return function;
		}
				
		public abstract IDictionary<string, Function.Function> GetFunctions(string category);
	}
}

