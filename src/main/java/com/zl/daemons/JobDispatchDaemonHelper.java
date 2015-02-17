package com.zl.daemons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.zl.abstracts.AJob;
import com.zl.interfaces.IDispatchJobService;
import com.zl.jobs.JobHelper;
import com.zl.jobs.WebCrawlingJob;
import com.zl.managers.SlaveManager;
import com.zl.server.nodes.SlaveNode;

@Component
public class JobDispatchDaemonHelper {
	
	@Autowired
	public SlaveManager slaveManager;
	
	@Autowired
	private ApplicationContext appContext;
	
	synchronized public void dispatchJob(final AJob job) {
		final SlaveNode slave = findSlave(job);		
		
		/**
		 * getBean() method together with "prototype" scope bean make sure each service is a new bean
		 */
		appContext.getBean(IDispatchJobService.class).dispatchJob(slave, job);
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
	synchronized private SlaveNode findSlave(AJob job) {
		if (job == null || !(job instanceof WebCrawlingJob))
			return null;
		String domain = JobHelper.getDomainFromUrl(((WebCrawlingJob)job).getUrl().toString());
		int hash = hashCode(domain);
		return slaveManager.getSlave(hash % slaveManager.getNum());
	}
}
