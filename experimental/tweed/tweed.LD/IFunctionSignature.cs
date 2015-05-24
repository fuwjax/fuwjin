using System;
namespace Tweed.LD
{
	/// <summary>
	/// 
	/// </summary>
	public interface IFunctionSignature
	{
		/// <summary>
		/// Returns a similar signature that can accept the specified number of arguments.
		/// </summary>
		/// <param name="count">
		/// The required number of arguments
		/// </param>
		/// <returns>
		/// The new <see cref="IFunctionSignature"/>
		/// </returns>
		/// <exception cref="IllegalArgumentException">if an appropriate signature cannot be produced.</exception>
		IFunctionSignature Accept(int count);
		
		/// <summary>
		/// Returns the category (usually the declaring type) for the signature.
		/// </summary>
		string Category { get; }
		
		/// <summary>
		/// Returns true if the signature matches the set of fixed parameters.
		/// </summary>
		/// <param name="parameters">
		/// The set of parameters
		/// </param>
		/// <returns>
		/// true if the signature matches, false otherwise
		/// </returns>
		bool MatchesFixed(params Type[] parameters);
		
		/// <summary>
		/// Returns true if this signature matches the set of variable parameters. The last parameter type should be an array.
		/// </summary>
		/// <param name="parameters">
		/// the set of parameters
		/// </param>
		/// <returns>
		/// true if the signature matches, false otherwise
		/// </returns>
		bool MatchesVarArgs(params Type[] parameters);

		/// <summary>
		/// The full name of the signature.
		/// </summary>
		string Name{ get; }
	}
}

