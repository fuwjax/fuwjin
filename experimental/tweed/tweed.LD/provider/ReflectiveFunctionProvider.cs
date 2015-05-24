using System;
using Tweed.LD;
using Tweed.LD.Adapter;
namespace Tweed.LD.Provider
{
	public class ReflectiveFunctionProvider:FunctionProvider
	{
		private IAdapter adapter;
		
		public ReflectiveFunctionProvider ()
		{
			this(new Adapter.Adapter());
		}
		
		public ReflectiveFunctionProvider(IAdapter adapter){
			this.adapter = adapter;
		}

		public override System.Collections.Generic.IDictionary<string, Function.Function> GetFunctions (string category)
		{
			throw new System.NotImplementedException();
		}
	}
}

