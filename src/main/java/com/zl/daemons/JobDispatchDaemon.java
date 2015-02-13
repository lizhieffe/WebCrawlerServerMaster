package com.zl.daemons;

import com.zl.interfaces.IJobToDispatchMonitor;
import com.zl.interfaces.ISlaveMonitor;
import com.zl.job.JobManager;
import com.zl.slave.SlaveManager;

import interfaces.IDaemon;
import interfaces.IThreadPoolDaemon;
import utils.SimpleLogger;
import abstracts.AJob;

public class JobDispatchDaemon implements IDaemon, IJobToDispatchMonitor, ISlaveMonitor {
	
	private boolean started;
	
	private static JobDispatchDaemon instance;
	private JobDispatchDaemonHelper helper;
	
	public static JobDispatchDaemon getInstance() {
		if (instance == null)
			instance = new JobDispatchDaemon();
		return instance;
	}
	
	private JobDispatchDaemon() {
		this.helper = new JobDispatchDaemonHelper();
	}

	@Override
	synchronized public boolean isStarted() {
		return this.started;
	}
	
	@Override
	public void start(IThreadPoolDaemon threadPoolDaemon) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				start();
			}
		};
		threadPoolDaemon.submit(task);
	}
	
	synchronized public void start() {
		if (started) {
			SimpleLogger.logServiceAlreadyStarted(this);
			return;
		}
		else
			started = true;
		
		final String serviceName = this.getClass().getName();
		try {
			while (started) {
				while (JobManager.getInstance().getWaitingWebCrawlingJobNum() == 0
						|| SlaveManager.getInstance().getNum() == 0) {
					wait();
				}
				AJob webCrawlingJob = JobManager.getInstance().popWaitingWebCrawlingJob();
				if (webCrawlingJob != null) {
					JobManager.getInstance().moveJobToRunningStatus(webCrawlingJob);
					helper.dispatchJob(webCrawlingJob);
				}
			}
			SimpleLogger.logServiceStopSucceed(serviceName);
		} catch (InterruptedException e) {
			e.printStackTrace();
			SimpleLogger.logServiceStopFail(serviceName);
		}
	}

	@Override
	public void stop() {
		if (!this.started)
			return;
		else
			this.started = false;
	}

	@Override
	synchronized public void onJobToDispatchAdded() {
		this.notifyAll();
	}

	@Override
	synchronized public void onSlaveAdded() {
		this.notifyAll();
	}

	@Override
	synchronized public void onSlaveRemoved() {
		return;
	}
}
