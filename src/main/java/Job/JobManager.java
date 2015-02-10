package Job;

import interfaces.IJobManager;
import abstracts.AJob;

public class JobManager implements IJobManager {
	
	private static JobManager instance;
	
	private JobManager() {
	}
	
	synchronized public static JobManager getInstance() {
		if (instance == null)
			instance = new JobManager();
		return instance;
	}
	
	@Override
	public boolean addJob(AJob job) {
		if (job == null)
			return false;
		if (job instanceof WebCrawlingJob)
			return WebCrawlingJobManager.getInstance().addJob(job);
		return false;
	}

	@Override
	public boolean moveJobToWaitingStatus(AJob job) {
		if (job instanceof WebCrawlingJob)
			return WebCrawlingJobManager.getInstance().moveJobToWaitingStatus(job);
		return false;
	}

	@Override
	public boolean moveJobToRunningStatus(AJob job) {
		if (job instanceof WebCrawlingJob)
			return WebCrawlingJobManager.getInstance().moveJobToRunningStatus(job);
		return false;
	}
	
	public AJob popWaitingWebCrawlingJob() {
		return WebCrawlingJobManager.getInstance().popWaitingJob();
	}
	
	public AJob getWaitingWebCrawlingJob(int index) {
		return WebCrawlingJobManager.getInstance().getWaitingJob(index);
	}
	
	public int getWaitingWebCrawlingJobNum() {
		return WebCrawlingJobManager.getInstance().getWaitingJobs().size();
	}
	
	public AJob getRunningWebCrawlingJob(int index) {
		return WebCrawlingJobManager.getInstance().getRunningJob(index);
	}
	
	public int getRunningWebCrawlingJobNum() {
		return WebCrawlingJobManager.getInstance().getRunningJobs().size();
	}
}
