package com.zl.daemons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import utils.SimpleLogger;
import abstracts.AJob;

import com.zl.interfaces.IDaemon;
import com.zl.interfaces.IJobToDispatchMonitor;
import com.zl.interfaces.ISlaveMonitor;
import com.zl.interfaces.IThreadPoolDaemon;
import com.zl.managers.JobManager;
import com.zl.managers.SlaveManager;

@Component
public class JobDispatchDaemon implements IDaemon, IJobToDispatchMonitor, ISlaveMonitor {
	
	@Autowired
	public JobDispatchDaemonHelper helper;

	@Autowired
	public JobManager jobManager;
	
	@Autowired
	public SlaveManager slaveManager;
	
	private boolean started;
	
	public JobDispatchDaemon() {
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
				while (jobManager.getWaitingWebCrawlingJobNum() == 0
						|| slaveManager.getNum() == 0) {
					wait();
				}
				AJob webCrawlingJob = jobManager.popWaitingWebCrawlingJob();
				if (webCrawlingJob != null) {
					jobManager.moveJobToRunningStatus(webCrawlingJob);
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
