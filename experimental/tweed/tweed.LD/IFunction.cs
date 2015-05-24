using System;
namespace Tweed.LD
{
	/// <summary>
	/// The central reflection abstraction interface. All methods, constructors, properties, virtual methods, field mutation/access, and other such invocation targets are exposed through this interface outside of tweed.LD.
	/// </summary>
	public interface IFunction
	{
		/// <summary>
		/// Invokes this function with the supplied arguments.
		/// </summary>
		/// <param name="args">
		/// The set of arguments
		/// </param>
		/// <returns>
		/// The invocation result
		/// </returns>
		/// <exception cref="AdaptException">if the arguments could not be adapted to the underlying invocation target.</exception>
		/// <exception cref="InvocationTargetException">if the underlying invocation target throws an exception.</exception>
		object Invoke(params object[] args);
		
		/// <summary>
		/// The name of this function. The same invocation target may have several names.
		/// </summary>
		string Name { get; }
	}
}

