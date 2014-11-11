package com.zeedoo.mars.task;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@ManagedResource()
@Component
public class TaskManager implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	// Default to true
	private boolean sensorDataSyncTaskEnabled = true;

	@ManagedAttribute(description = "Whether SensorDataSyncTask is enabled for all connections")
	public boolean isSensorDataSyncTaskEnabled() {
		return sensorDataSyncTaskEnabled;
	}

	@ManagedAttribute(description = "Sets whether SensorDataSyncTask is enabled for all connections")
	public void setSensorDataSyncTaskEnabled(boolean sensorDataSyncTaskEnabled) {
		this.sensorDataSyncTaskEnabled = sensorDataSyncTaskEnabled;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	// Creates a new SensorDataSyncTask
	public SensorDataSyncTask createNewSensorDataSyncTask(ChannelHandlerContext context) {
		// Since it's scoped to prototype, we will create a new instance everytime
		SensorDataSyncTask sensorDataSyncTask = (SensorDataSyncTask)applicationContext.getBean(SensorDataSyncTask.class);
		// Setting this mandatory field
		sensorDataSyncTask.setChannelHandlerContext(context);
		return sensorDataSyncTask;
	}
}
