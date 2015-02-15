package com.zl.daemons;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import Job.JobHelper;
import Job.WebCrawlingJob;
import ServerNode.SlaveNode;
import abstracts.AJob;

import com.zl.interfaces.ISlaveManager;
import com.zl.services.DispatchJobService;
import com.zl.slave.SlaveManager;

@Configuration
@EnableAsync
@EnableAutoConfiguration
public class JobDispatchDaemonHelper {
	
	synchronized public void dispatchJob(final AJob job) {
		final SlaveNode slave = findSlave(job, SlaveManager.getInstance());				
		new DispatchJobService().dispatchJob(slave, job);
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
}
