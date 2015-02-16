package com.zl;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.zl.interfaces.IBeanConfiguration;
import com.zl.interfaces.IDispatchJobService;
import com.zl.services.DispatchJobService;

@Component
@Configuration
public class ServiceConfiguration implements IBeanConfiguration {
	
	public IDispatchJobService createIDispatchJobService() {
    	return new DispatchJobService();
    }

}
