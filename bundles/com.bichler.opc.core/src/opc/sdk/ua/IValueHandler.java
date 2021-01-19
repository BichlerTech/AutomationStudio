package opc.sdk.ua;

public interface IValueHandler<T> {
	T getValue();

	void setValue(T value);
}
