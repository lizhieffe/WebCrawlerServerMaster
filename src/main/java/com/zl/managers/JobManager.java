package com.zl.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zl.jobs.WebCrawlingJob;
import com.zl.abstracts.AJob;

import com.zl.interfaces.IJobManager;

@Component
public class JobManager implements IJobManager {
	
	@Autowired
	public WebCrawlingJobManager webCrawlingJobManager;
	
	public JobManager() {
	}
	
	@Override
	public boolean addJob(AJob job) {
		if (job == null)
			return false;
		if (job instanceof WebCrawlingJob)
			return webCrawlingJobManager.addJob(job);
		return false;
	}

	@Override
	public boolean moveJobToWaitingStatus(AJob job) {
		if (job instanceof WebCrawlingJob)
			return webCrawlingJobManager.moveJobToWaitingStatus(job);
		return false;
	}

	@Override
	public boolean moveJobToRunningStatus(AJob job) {
		if (job instanceof WebCrawlingJob)
			return webCrawlingJobManager.moveJobToRunningStatus(job);
		return false;
	}
	
	@Override
	public boolean removeJobFromRunningStatus(AJob job) {
		if (job instanceof WebCrawlingJob)
			return webCrawlingJobManager.removeJobFromRunningStatus(job);
		return false;
	}
	
	public AJob popWaitingWebCrawlingJob() {
		return webCrawlingJobManager.popWaitingJob();
	}
	
	public AJob getWaitingWebCrawlingJob(int index) {
		return webCrawlingJobManager.getWaitingJob(index);
	}
	
	public int getWaitingWebCrawlingJobNum() {
		return webCrawlingJobManager.getWaitingJobs().size();
	}
	
	public AJob getRunningWebCrawlingJob(int index) {
		return webCrawlingJobManager.getRunningJob(index);
	}
	
	public int getRunningWebCrawlingJobNum() {
		return webCrawlingJobManager.getRunningJobs().size();
	}
}
