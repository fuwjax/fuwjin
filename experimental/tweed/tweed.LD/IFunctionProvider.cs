using System;
namespace Tweed.LD
{
	public interface IFunctionProvider
	{
		/// <summary>
		/// Returns the function corresponding to the signature.
		/// </summary>
		/// <param name="signature">
		/// The required function's <see cref="IFunctionSignature"/>
		/// </param>
		/// <returns>
		/// The corresponding <see cref="IFunction"/>
		/// </returns>
		/// <exception cref="NoSuchFunctionException">if the signature does not map to a function on this provider.</exception>
		IFunction GetFunction(IFunctionSignature signature);
	}
}

