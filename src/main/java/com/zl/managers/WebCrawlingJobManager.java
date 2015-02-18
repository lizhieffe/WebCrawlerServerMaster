package com.zl.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.zl.abstracts.AJob;
import com.zl.interfaces.IJobDispatchDaemon;
import com.zl.interfaces.IJobManager;
import com.zl.jobs.WebCrawlingJob;

@Component
public class WebCrawlingJobManager implements IJobManager {
	
	@Autowired
	@Lazy
	public IJobDispatchDaemon jobDispatchDaemon;
	
	private CopyOnWriteArrayList<AJob> waitingJobs;
	private CopyOnWriteArrayList<AJob> runningJobs;
	
	private WebCrawlingJobManager() {
		waitingJobs = new CopyOnWriteArrayList<AJob>();
		runningJobs = new CopyOnWriteArrayList<AJob>();
	}
	
	@Override
	public boolean addJob(AJob job) {
		if (!(job instanceof WebCrawlingJob))
			return false;
		waitingJobs.add(job);
		jobDispatchDaemon.onJobToDispatchAdded();
		return true;
	}

	public boolean moveJobToWaitingStatus(AJob job) {
		if (!(job instanceof WebCrawlingJob))
			return false;
		runningJobs.remove(job);
		waitingJobs.add(job);
		jobDispatchDaemon.onJobToDispatchAdded();
		return true;
	}
	
	public boolean moveJobToRunningStatus(AJob job) {
		if (!(job instanceof WebCrawlingJob))
			return false;
		waitingJobs.remove(job);
		runningJobs.add(job);
		return true;
	}
	
	public boolean removeJobFromRunningStatus(AJob job) {
		if (!(job instanceof WebCrawlingJob))
			return false;
		boolean result = runningJobs.remove(job);
		return result;
	}
	
	public AJob popWaitingJob() {
		try {
			return waitingJobs.remove(0);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public AJob getWaitingJob(int index) {
		try {
			return waitingJobs.get(index);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public AJob removeWaitingJob(int index) {
		try {
			return waitingJobs.remove(index);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public AJob getRunningJob(int index) {
		try {
			return runningJobs.get(index);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public AJob removeRunningJob(int index) {
		try {
			return runningJobs.remove(index);
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
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
	public boolean hasJobWaiting() {
		return waitingJobs.size() > 0;
	}

//	@Override
	public boolean hasJobRunning() {
		return runningJobs.size() > 0;
	}

}
