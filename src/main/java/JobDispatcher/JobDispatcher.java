package JobDispatcher;

import interfaces.ISlaveManager;
import java.util.concurrent.ExecutionException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import utils.SimpleLogger;
import Job.WebCrawlingJob;
import ServerNode.SlaveNode;
import Slave.SlaveManager;
import abstracts.AJob;

public class JobDispatcher {
	
	private static JobDispatcher instance;
	private ISlaveManager slaveManager;
				
	private JobDispatcher() {
		this.slaveManager = SlaveManager.getInstance();
	}
	
	synchronized public static JobDispatcher getInstance() {
		if (instance == null)
			instance = new JobDispatcher();
		return instance;
	}
	
	synchronized public void dispatchJob(final AJob job) {		
		AsyncRestTemplate rest = new AsyncRestTemplate();
		final SlaveNode slave = JobDispatcherHelper.findSlave(job, this.slaveManager);				
		ListenableFuture<ResponseEntity<String>> future = rest.exchange(JobDispatcherHelper.constructRequestUrl(slave),
				HttpMethod.POST, JobDispatcherHelper.constructRequestHttpEntity(job), String.class);
		SimpleLogger.info("[Dispatcher] Dispatching to SLAVE (" + slave.getDomain() +") about job: URL=" 
				+ ((WebCrawlingJob)job).getUrl() + ", depth=" 
				+ ((WebCrawlingJob)job).getDepth());
		try {
			ResponseEntity<String> response = future.get();
			SimpleLogger.info("[Dispatcher] Dispatching finished (" + response.getBody() 
					+ ") to SLAVE (" + slave.getDomain() +") about job: URL=" + ((WebCrawlingJob)job).getUrl() + ", depth=" 
					+ ((WebCrawlingJob)job).getDepth());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
