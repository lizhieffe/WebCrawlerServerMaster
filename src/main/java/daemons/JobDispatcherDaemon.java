package daemons;

import interfaces.IJobToDispatchMonitor;
import interfaces.IDaemon;
import interfaces.ISlaveMonitor;
import interfaces.IThreadPoolDaemon;
import utils.SimpleLogger;
import Job.JobManager;
import JobDispatcher.JobDispatcher;
import Slave.SlaveManager;
import abstracts.AJob;

public class JobDispatcherDaemon implements IDaemon, IJobToDispatchMonitor, ISlaveMonitor {
	
	private boolean started;
	
	private static JobDispatcherDaemon instance;
	
	public static JobDispatcherDaemon getInstance() {
		if (instance == null)
			instance = new JobDispatcherDaemon();
		return instance;
	}
	
	private JobDispatcherDaemon() {
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
					JobDispatcher.getInstance().dispatchJob(webCrawlingJob);
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
