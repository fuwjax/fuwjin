using System;
namespace Tweed.LD.Signature
{
	public class NameOnlySignature: IFunctionSignature
	{
		private string name;
		
		public NameOnlySignature (string name)
		{
			this.name = name;
		}

		public virtual IFunctionSignature Accept (int count)
		{
			return new ArgCountSignature(name, count);
		}
		
		
		public virtual bool MatchesFixed (params Type[] parameters)
		{
			return true;
		}
		
		
		public virtual bool MatchesVarArgs (params Type[] parameters)
		{
			return true;
		}
		
		
		public string Category {
			get {
				return name.Substring(0, name.LastIndexOf('.'));
			}
		}
		
		
		public string Name {
			get {
				return name;
			}
		}
		
		public override string ToString ()
		{
			return name +"(*)";
		}		
		
	}
}

