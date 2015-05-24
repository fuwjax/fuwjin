using System;
namespace Tweed.LD.Sample
{
	public class Sample
	{
		private string name;
		
		public Sample (string name)
		{
			this.name = name;
		}
		
		public string doSomething() {
			return name+":do";
		}
	}
}

