package opc.sdk.core.application.operation;

public interface ICancleOperation {
	public boolean isCanceled();

	public void subTask(String name);

	public void worked(int count);

	public void done();
}
