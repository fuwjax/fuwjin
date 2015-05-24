using System;
using NUnit.Framework;
using Tweed.LD;
using Tweed.LD.Provider;
using Tweed.LD.Signature;
namespace Tweed.LD.Test
{
	[TestFixture()]
	public class ReflectiveFunctionProviderTest
	{
		private ReflectiveFunctionProvider provider;
		
		[SetUp]
		public void SetUp(){
			provider = new ReflectiveFunctionProvider();
		}	
		 
		[Test()]
		public void TestMethod ()
		{
			IFunction function = provider.GetFunction(new TypedArgsSignature("Tweed.LD.Sample.Sample.doSomething").AddArg("Tweed.LD.Sample.Sample"));
			Sample.Sample sample = new Sample.Sample("test");
			object test = function.Invoke(sample);
			Assert.AreEqual(test, "test:do");
		}
	}
}

