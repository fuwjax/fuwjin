using System;
using System.Collections.Generic;
using Tweed.LD.Function;
namespace Tweed.LD.Provider
{
	public class CachedFunctionProvider: FunctionProvider
	{
		private FunctionProvider[] providers;
		private IDictionary<string, string> categories = new Dictionary<string, string>();
		private IDictionary<string, Function.Function> functions = new Dictionary<string, Function.Function>();

		public CachedFunctionProvider (params FunctionProvider[] providers)
		{
			this.providers = providers;
		}
		
		public override IDictionary<string, Function.Function> GetFunctions (string category)
		{
			if(!categories.ContainsKey(category))
			{
				foreach(FunctionProvider provider in providers){
					foreach(Function.Function func in provider.GetFunctions(category).Values){
						Add(functions, func);
					}
				}
				categories.Add(category, category);
			}
			return functions;
		}
		
		
	}
}

