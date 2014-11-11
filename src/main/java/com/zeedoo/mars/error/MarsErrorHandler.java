package com.zeedoo.mars.error;

import io.netty.channel.ChannelHandlerContext;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.base.Throwables;
import com.zeedoo.mars.message.MessageGateway;

/** 
 *  Encapsulates most of the Mars error handling logic 
 *  As a service, we should be error-tolerant, therefore the exception is classified into two classes
 *  Fatal exception indicates something really bad has happened, and we need to close the client connection
 *  Otherwise, non-fatal exceptions will be logged and alerts will be sent
 */
@Component
@ManagedResource
public class MarsErrorHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MarsErrorHandler.class);
	
	// Error count
	private AtomicInteger totalErrorCount;
	
	private AtomicInteger fatalErrorCount;
	
	private AtomicInteger nonFatalErrorCount;
	
	// Error reset timestamps
	private DateTime lastTotalErrorCountReset;
	
	// Fatal exception set
	private final Set<Class <? extends Throwable>> fatalExceptionClasses = new CopyOnWriteArraySet<>();
	
	@PostConstruct
	public void initialize() {
		// Add the fatal exception classes
		fatalExceptionClasses.add(JsonParseException.class); // This means the message is a not valid JSON, we need to close the connection
		// TODO: Make this persistent and save to disk?
		totalErrorCount = new AtomicInteger(0);
		fatalErrorCount = new AtomicInteger(0);
		nonFatalErrorCount = new AtomicInteger(0);
	}

	@ManagedOperation
	public Set<Class <? extends Throwable>> getFatalExceptionClasses() {
		return fatalExceptionClasses;
	}
	
	@ManagedOperation
	public void resetAllErrorCounts() {
		totalErrorCount.set(0);
		fatalErrorCount.set(0);
		nonFatalErrorCount.set(0);
	}

	@ManagedAttribute
	public int getTotalErrorCount() {
		return totalErrorCount.get();
	}

	@ManagedAttribute
	public int getFatalErrorCount() {
		return fatalErrorCount.get();
	}

	@ManagedAttribute
	public int getNonFatalErrorCount() {
		return nonFatalErrorCount.get();
	}
	
	@ManagedAttribute
	public String getLastTotalErrorCountReset() {
		return lastTotalErrorCountReset != null? lastTotalErrorCountReset.toString() : null;
	}
	
	/**
	 * Main method to handle all the errors
	 * @param context
	 * @param throwable
	 */
	public void handleError(ChannelHandlerContext context, Throwable throwable) {
		// need to close connection if it's a fatal exception
		final Throwable rootCause = Throwables.getRootCause(throwable);
		boolean isFatalException = isFatalException(rootCause);
		if (isFatalException) {
			LOGGER.error("Root cause of throwable = {} is a fatal exception, CLOSING CONNECTION", rootCause.getClass().getName(), throwable.getClass().getName());
			// TODO: Add check here to ensure the channel is closed
			//context.channel().eventLoop().shutdownGracefully();
			context.close();
		}

		logException(throwable, rootCause, isFatalException);
	}
	
	/**
	 * Log exception and increment count
	 * @param t
	 * @param isFatalException
	 */
	private void logException(Throwable t, Throwable rootCause, boolean isFatalException) {
		if (isFatalException) {
			LOGGER.error(String.format("Fatal exception handled, rootCause=%s, exception=", rootCause.getClass()), t);
			fatalErrorCount.getAndIncrement();
		} else {
			LOGGER.error(String.format("Non-fatal exception handled, rootCause=%s, exception=", rootCause.getClass()), t);
			nonFatalErrorCount.getAndIncrement();
		}
		totalErrorCount.getAndIncrement();
	}
	
	/**
	 * Determines if an exception is fatal and the connection needs to be closed
	 * @param t
	 * @return
	 */
	private boolean isFatalException(Throwable rootCause) {
		if (this.fatalExceptionClasses.contains(rootCause.getClass())) {
			return true;
		}
		return false;
	}

}
