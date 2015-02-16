package com.zl.managers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import Job.WebCrawlingJob;
import abstracts.AJob;

import com.zl.daemons.JobDispatchDaemon;
import com.zl.interfaces.IJobManager;

@Component
public class WebCrawlingJobManager implements IJobManager {
	
	@Autowired
	public JobDispatchDaemon jobDispatchDaemon;
	
	private List<AJob> waitingJobs;
	private List<AJob> runningJobs;
	
	private WebCrawlingJobManager() {
		waitingJobs = new ArrayList<AJob>();
		runningJobs = new ArrayList<AJob>();
	}
	
	@Override
	public boolean addJob(AJob job) {
		if (!(job instanceof WebCrawlingJob))
			return false;
		synchronized (this) {
			waitingJobs.add(job);
		}
		jobDispatchDaemon.onJobToDispatchAdded();;
		return true;
	}

	public boolean moveJobToWaitingStatus(AJob job) {
		if (!(job instanceof WebCrawlingJob))
			return false;
		synchronized (this) {
			runningJobs.remove(job);
			waitingJobs.add(job);
		}
		jobDispatchDaemon.onJobToDispatchAdded();;
		return true;
	}
	
	public boolean moveJobToRunningStatus(AJob job) {
		if (!(job instanceof WebCrawlingJob))
			return false;
		synchronized (this) {
			waitingJobs.remove(job);
			runningJobs.add(job);
		}
		return true;
	}
	
	synchronized public AJob popWaitingJob() {
		if (waitingJobs.size() == 0)
			return null;
		return waitingJobs.remove(0);
	}
	
	synchronized public AJob getWaitingJob(int index) {
		if (index >= waitingJobs.size())
			return null;
		return waitingJobs.get(index);
	}
	
	synchronized public AJob removeWaitingJob(int index) {
		if (index >= waitingJobs.size())
			return null;
		return waitingJobs.remove(index);
	}
	
	synchronized public AJob getRunningJob(int index) {
		if (index >= runningJobs.size())
			return null;
		return runningJobs.get(index);
	}
	
	synchronized public AJob removeRunningJob(int index) {
		if (index >= runningJobs.size())
			return null;
		return runningJobs.remove(index);
	}
	
//	@Override
	synchronized public List<AJob> getWaitingJobs() {
		return new ArrayList<AJob>(waitingJobs);
	}

//	@Override
	synchronized public List<AJob> getRunningJobs() {
		return new ArrayList<AJob>(runningJobs);
	}

//	@Override
	synchronized public boolean hasJobWaiting() {
		return waitingJobs.size() > 0;
	}

//	@Override
	synchronized public boolean hasJobRunning() {
		return runningJobs.size() > 0;
	}

}
