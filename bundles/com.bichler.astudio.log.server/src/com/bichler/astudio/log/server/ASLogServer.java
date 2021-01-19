package com.bichler.astudio.log.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ASLogServer extends Thread {
	/** timeout waiting for incoming message */
	private static final int SOCKET_TIMEOUT_INFINITE = 0;
	/** server socket listening log messages */
	private ServerSocket serverSocket = null;
	/** log message executions */
	private final ExecutorService executor = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/**
	 * Server listening for log4j messages.
	 * 
	 * @param Port
	 *            Port to listen.
	 * @throws IOException
	 *             Unable to listen on port.
	 */
	public ASLogServer(int port) throws IOException {
		if (port < 0) {
			throw new IllegalArgumentException(
					"Port value should be a positive number. Current port: "
							+ port);
		}
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new IOException("Could not listen on port: " + port, e);
		}
	}

	@Override
	public void run() {
		// listen on port
		while (!this.serverSocket.isClosed()) {
			try {
				// wait for incoming message
				Socket socket = this.serverSocket.accept();
				// max timeout for blocking socket
				socket.setSoTimeout(SOCKET_TIMEOUT_INFINITE);
				// execute incoming message
				this.executor.execute(new ASLogHandler(socket));
			} catch (SocketException e) {
				// closing socket
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Close server socket.
	 */
	public void shutdown() {
		try {
			// close listening server socket
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// exits thread executions
			this.executor.shutdown();

			try {
				// waits to terminate tasks
				if (!executor.awaitTermination(Integer.MAX_VALUE,
						TimeUnit.NANOSECONDS)) {
					// terminate tasks
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
