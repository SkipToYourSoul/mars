package com.zeedoo.mars.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.MBeanExporterListener;

public class HttpAdaptorMgr implements MBeanExporterListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpAdaptorMgr.class);
	
	/**
	 * Default to the platform MBean Server
	 */
	private MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

	/**
	 * This name must match the specified name in jmxBeans.xml
	 */
	private String adaptorName = "Server:name=HttpAdaptor";
	
	/**
	 * Make sure we only start up once
	 */
	private static boolean isStarted = false;
	
	protected synchronized void start(ObjectName objectName) {
		if (!isStarted) {
            try {
                mbeanServer.invoke(objectName, "start", null, null);
            } catch (Exception e) {
                LOGGER.error("Can't start HttpAdaptor", e);
            }
            isStarted = true;
        }
	}
	
	protected synchronized void stop(ObjectName objectName) {
        // do this even if the server isn't started
        if (isStarted) {
            try {
                mbeanServer.invoke(objectName, "stop", null, null);
            } catch (Exception e) {
                LOGGER.error("Can't stop HttpAdaptor", e);
            }
            isStarted = false;
        }
    }
	
	@Override
	public void mbeanRegistered(ObjectName objectName) {
		if (adaptorName.equals(objectName.getCanonicalName())) {
			start(objectName);
		}
	}

	@Override
	public void mbeanUnregistered(ObjectName objectName) {
		if (adaptorName.equals(objectName.getCanonicalName())) {
			stop(objectName);
		}
	}

	public MBeanServer getmbeanServer() {
		return mbeanServer;
	}

	public void setmbeanServer(MBeanServer mbeanServer) {
		this.mbeanServer = mbeanServer;
	}

	public String getAdaptorName() {
		return adaptorName;
	}

	public void setAdaptorName(String adaptorName) {
		this.adaptorName = adaptorName;
	}	
}
