import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import services.JobDispatcherService;
import services.ThreadPoolService;
import utils.ConfigUtil;
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
        ThreadPoolService.getInstance().start();
        JobDispatcherService.getInstance().start(ThreadPoolService.getInstance());
//        ConfigUtil.get();
    }
}