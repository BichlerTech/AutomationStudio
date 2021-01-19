package com.bichler.astudio.log.server.core;

import java.util.Arrays;

public class ASLogCollection {

	private final ASLog[] logs;

	private final int capacity;

	private int head = -1;

	private int tail = -1;

	public ASLogCollection(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Collection capacity should be a positive number: " + capacity);
		}
		this.capacity = capacity;
		logs = new ASLog[capacity];
	}

	public synchronized void add(ASLog log) {
		if (this.size() == 0) {
			head++;
			tail++;
			logs[head] = log;
		} else {
			tail++;

			if (tail == capacity) {
				tail = 0;
			}
			logs[tail] = log;

			if (head + 1 == capacity) {
				head = 0;
			} else if (head == tail) {
				head++;
			}
		}
	}

	public synchronized ASLog get(int index) {
		int size = this.size();

		if (size == 0 || index >= size || index < 0 || index >= capacity) {
			throw new IllegalArgumentException("Array index is out of bounds: " + index);
		}
		int position = (head + index) % capacity;
		return logs[position];
	}

	public synchronized int size() {
		if (tail < 0 || head < 0) {
			return 0;
		}
		if (head == tail) {
			return 1;
		} else if (head < tail) {
			return tail - head + 1;
		} else {
			int afterHead = capacity - head;
			int beforeTail = tail + 1;
			return afterHead + beforeTail;
		}
	}

	public synchronized ASLog[] toArray() {
		if (this.size() == 0) {
			return new ASLog[] {};
		} else if (head == tail) {
			return new ASLog[] { logs[head] };
		} else if (head < tail) {
			int length = this.size();
			ASLog[] array = new ASLog[length];
			System.arraycopy(logs, head, array, 0, length);
			return array;
		} else {
			int length = this.size();
			ASLog[] array = new ASLog[length];
			System.arraycopy(logs, head, array, 0, capacity - head);
			System.arraycopy(logs, 0, array, capacity - head, tail + 1);
			return array;
		}
	}

	public synchronized void clear() {
		head = -1;
		tail = -1;

		for (int i = 0; i < capacity; i++) {
			logs[i] = null;
		}
	}

	@Override
	public String toString() {
		return "LogCollection [logs=" + Arrays.toString(logs) + "]";
	}
}
