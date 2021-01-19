package com.bichler.astudio.editor.pubsub.nodes;

public enum PubSubRTLevel {
	UA_PUBSUB_RT_NONE(0), UA_PUBSUB_RT_DIRECT_VALUE_ACCESS(1), UA_PUBSUB_RT_FIXED_SIZE(2),
	UA_PUBSUB_RT_DETERMINISTIC(4);

	private final int value;

	private PubSubRTLevel(int value) {
		this.value = value;
	}
}
