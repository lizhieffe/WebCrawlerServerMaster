package com.zl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import com.zl.daemons.JobDispatchDaemon;
import com.zl.daemons.ThreadPoolDaemon;
import com.zl.interfaces.IBeanConfiguration;

@Component
@Configuration
public class DaemonConfiguration implements IBeanConfiguration {
	
	public ThreadPoolDaemon createThreadPoolDaemon() {
		return new ThreadPoolDaemon();
	}
	
	public JobDispatchDaemon createJobDispatchDaemon() {
		return new JobDispatchDaemon();
	}
}
