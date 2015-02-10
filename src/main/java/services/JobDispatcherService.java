package services;

import interfaces.IJobToDispatchMonitor;
import interfaces.IService;
import interfaces.ISlaveMonitor;
import interfaces.IThreadPoolService;
import utils.SimpleLogger;
import Job.JobManager;
import JobDispatcher.JobDispatcher;
import Slave.SlaveManager;
import abstracts.AJob;

public class JobDispatcherService implements IService, IJobToDispatchMonitor, ISlaveMonitor {
	
	private boolean started;
	
	private static JobDispatcherService instance;
	
	public static JobDispatcherService getInstance() {
		if (instance == null)
			instance = new JobDispatcherService();
		return instance;
	}
	
	private JobDispatcherService() {
	}

	@Override
	public void start(IThreadPoolService threadPoolService) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				start();
			}
		};
		threadPoolService.submit(task);
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
			AJob webCrawlingJob = null;
			while (started) {
				while ((webCrawlingJob = JobManager.getInstance().popWaitingWebCrawlingJob()) == null || SlaveManager.getInstance().getNum() == 0) {
					wait();
				}
				JobManager.getInstance().moveJobToRunningStatus(webCrawlingJob);
				JobDispatcher.getInstance().dispatchJob(webCrawlingJob);
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
