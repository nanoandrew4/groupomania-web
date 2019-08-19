package com.greenapper.logging;

import com.greenapper.exceptions.NotFoundException;
import com.greenapper.models.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LogManager {

	@Autowired
	private MongoTemplate mongoTemplate;

	private static Map<String, Logger> loggerMap = new HashMap<>();

	/**
	 * Determines the class that requested logging, to return a logger for the caller class, which will be created
	 * if it does not exist.
	 *
	 * @return {@link Logger} for the caller class
	 */
	private Logger getCallerClassLogger() {
		final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		String caller = null;
		boolean recordCaller = false;
		for (StackTraceElement stackTraceElement : stackTrace) {
			if (this.getClass().getName().equals(stackTraceElement.getClassName()))
				recordCaller = true;
			if (recordCaller && !stackTraceElement.getClassName().equals(this.getClass().getName())) {
				caller = stackTraceElement.getClassName();
				break;
			}
		}

		if (caller == null)
			throw new NotFoundException("Caller method not found, no logger can be returned");

		if (!loggerMap.containsKey(caller))
			loggerMap.put(caller, LoggerFactory.getLogger(caller));
		return loggerMap.get(caller);
	}

	public void info(final String message) {
		getCallerClassLogger().info(message);
		mongoTemplate.insert(new LogMessage(message, "INFO"), "logs");
	}

	public void warn(final String message) {
		getCallerClassLogger().warn(message);
		mongoTemplate.insert(new LogMessage(message, "WARN"), "logs");
	}

	public void error(final String message) {
		getCallerClassLogger().error(message);
		mongoTemplate.insert(new LogMessage(message, "ERROR"), "logs");
	}

	public void error(final String message, final Throwable t) {
		getCallerClassLogger().error(message, t);
		mongoTemplate.insert(new LogMessage(message, t, "ERROR"), "logs");
	}
}
