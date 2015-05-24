using System;
using System.Collections.Generic;
using System.Text;
namespace Tweed.LD.Signature
{
	public class TypedArgsSignature:ArgCountSignature
	{
		private IList<Type> args = new List<Type>();
		public TypedArgsSignature (string name):base(name,0)
		{
			
		}
		
		protected Type ArgType(int index){
			if(index < args.Count){
				return args[index];
			}
			return null;
		}
		
		protected override int Count {
			get {
				return base.Count;
			}
		}
		
		public TypedArgsSignature AddArg(string typeName){
			if(typeName == null || "null".Equals(typeName)){
				args.Add(null);
			}else{
				args.Add(Type.GetType(typeName));
			}
			return this;
		}
		
		public override IFunctionSignature Accept (int count)
		{
			return base.Accept (count);
		}

		public override bool MatchesFixed (params Type[] parameters)
		{
			if(!base.MatchesFixed (parameters)){
				return false;
			}
			for(int i=0;i<parameters.Length;++i){
				if(!parameters[i].IsAssignableFrom(ArgType(i))){
					return false;
				}
			}
			return true;
		}

		public override bool MatchesVarArgs (params Type[] parameters)
		{
			if(MatchesFixed(parameters)){
				return true;
			}
			if(!base.MatchesVarArgs(parameters)){
				return false;
			}
			for(int i=0;i<parameters.Length -1;++i){
				if(!parameters[i].IsAssignableFrom(ArgType(i))){
					return false;
				}
			}
			Type componentType = parameters[parameters.Length-1].GetElementType();
			for(int index = parameters.Length-1;index < Count; ++index){
				if(!componentType.IsAssignableFrom(ArgType(index))){
					return false;
				}
			}
			return true;
		}	
		
		public override string ToString ()
		{
			StringBuilder builder = new StringBuilder();
			builder.Append(Name);
			string delim = "(";
			if(Count == 0){
				builder.Append(delim);
			}else{
				foreach(Type type in args){
					builder.Append(delim).Append(type.Name);
					delim = ", ";
				}
			}
			return builder.Append(")").ToString();
		}
	}
}

