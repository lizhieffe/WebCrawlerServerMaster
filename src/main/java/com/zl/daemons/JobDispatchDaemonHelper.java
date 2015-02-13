package com.zl.daemons;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import com.zl.interfaces.ISlaveManager;
import com.zl.slave.SlaveManager;

import utils.SimpleLogger;
import Job.JobHelper;
import Job.WebCrawlingJob;
import ServerNode.SlaveNode;
import abstracts.AJob;

public class JobDispatchDaemonHelper {
					
	private static String URI =  "/addslavejob";

	public JobDispatchDaemonHelper() {
	}
	
	synchronized public void dispatchJob(final AJob job) {		
		AsyncRestTemplate rest = new AsyncRestTemplate();
		final SlaveNode slave = findSlave(job, SlaveManager.getInstance());				
		ListenableFuture<ResponseEntity<String>> future = rest.exchange(constructRequestUrl(slave),
				HttpMethod.POST, constructRequestHttpEntity(job), String.class);
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
	
	public static int hashCode(String s) {
		if (s == null)
			return 0;
		int hash = 7;
		char[] chars = s.toCharArray();
		for (char c : chars)
			hash = (hash * 31 + c) % 31;
		return hash;
	}
	
	/**
	 * find the slave to process the job
	 * 
     * @param strs: A list of strings
     * @return: A list of strings
     */
	static SlaveNode findSlave(AJob job, ISlaveManager slaveManager) {
		if (job == null || !(job instanceof WebCrawlingJob))
			return null;
		String domain = JobHelper.getDomainFromUrl(((WebCrawlingJob)job).getUrl().toString());
		int hash = hashCode(domain);
		return slaveManager.getSlave(hash % slaveManager.getNum());
	}
	
	static String constructRequestUrl(SlaveNode slave) {
		return slave.getDomain() + URI;
	}
	
	static HttpEntity<String> constructRequestHttpEntity(AJob job) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		JSONObject item = new JSONObject();
		item.put("type", "webcrawling");
		item.put("url", ((WebCrawlingJob)job).getUrl().toString());
		item.put("depth", Integer.valueOf(((WebCrawlingJob)job).getDepth()));
		return new HttpEntity<String>(item.toString(), header);
	}
}
