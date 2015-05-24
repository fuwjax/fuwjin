using System;
using System.Text;
namespace Tweed.LD.Signature
{
	public class ArgCountSignature: NameOnlySignature
	{
		private int count;
		
		public ArgCountSignature (string name, int count):base(name)
		{
			this.count = count;
		}
		
		public override IFunctionSignature Accept (int required)
		{
			if(Count != required){
				throw new ArgumentException(string.Format("Expected {0} args, not {0}",Count,required));
			}
			return this;
		}

		public override bool MatchesFixed (params Type[] parameters)
		{
			return parameters.Length == Count;
		}

		public override bool MatchesVarArgs (params Type[] parameters)
		{
			return parameters.Length <= Count + 1;
		}		
		
		protected virtual int Count {
			get{
				return count;
			}
		}
		
		public override string ToString ()
		{
			StringBuilder builder = new StringBuilder();
			builder.Append(Name);
			string delim = "(";
			if(Count == 0){
				builder.Append(delim);
			}else{
				for(int i=0;i < Count;i++){
					builder.Append(delim).Append("?");
					delim = ", ";
				}
			}
			return builder.Append(")").ToString();
		}
	}
}

