import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import daemons.JobDispatcherDaemon;
import daemons.ThreadPoolDaemon;
import utils.SimpleLogger;

@Component
public class StartupHousekeeper implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		SimpleLogger.info("Application has started");
        startServices();
	}
	
	private void startServices() {
    	SimpleLogger.info("Starting services:");
    	ThreadPoolDaemon.getInstance().start();
        JobDispatcherDaemon.getInstance().start(ThreadPoolDaemon.getInstance());
    }
}