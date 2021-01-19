package com.bichler.astudio.log.server.listener;

import com.bichler.astudio.log.server.core.ASLog;

public interface ASLogEventListener {

	void handleLogEvent(ASLog logEvent);

}
