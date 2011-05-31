using System;
namespace Tweed.LD
{
	/// <summary>
	/// Thrown when no function can be found by an IFunctionProvider.
	/// </summary>
	public class NoSuchFunctionException: Exception
	{
		/// <summary>
		/// Creates a new instance.
		/// </summary>
		public NoSuchFunctionException (string message, params object[] args) : base(message){
		}
	}
}

