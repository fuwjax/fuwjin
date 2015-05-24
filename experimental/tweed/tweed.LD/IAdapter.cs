using System;
namespace Tweed.LD
{
	/// <summary>
	/// Manages transformation of objects into other types.
	/// </summary>
	public interface IAdapter
	{
		/// <summary>
		/// Transforms the value into an instance of the type.
		/// </summary>
		/// <param name="value">
		/// The <see cref="System.Object"/> to transform
		/// </param>
		/// <param name="type">
		/// The <see cref="Type"/> to transform into
		/// </param>
		/// <returns>
		/// The transformed <see cref="System.Object"/>
		/// </returns>
		/// <exception cref="AdaptException">
		/// if the value cannot be transformed into the type
		/// </exception>
		object Adapt(object value, Type type);
	}
}

