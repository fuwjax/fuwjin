package org.fuwjin.diioc;

public class ExtenedContext extends AbstractContext {
	private Context context;

	public ExtenedContext(Context context) {
		this.context = context;
	}

	@Override
	public <T> Binding<T> bind(Key<T> key) throws Exception {
		return context.bind(key);
	}
}
