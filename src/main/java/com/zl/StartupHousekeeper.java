package com.zl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import utils.SimpleLogger;

import com.zl.daemons.JobDispatchDaemon;
import com.zl.daemons.ThreadPoolDaemon;

@Component
public class StartupHousekeeper implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	public ThreadPoolDaemon threadPoolDaemon;
	
	@Autowired
	public JobDispatchDaemon jobDispatchDaemon;
	
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		SimpleLogger.info("Application has started");
        startServices();
	}
	
	private void startServices() {
    	SimpleLogger.info("Starting services:");
    	threadPoolDaemon.start();
    	jobDispatchDaemon.start(ThreadPoolDaemon.getInstance());
    }
}