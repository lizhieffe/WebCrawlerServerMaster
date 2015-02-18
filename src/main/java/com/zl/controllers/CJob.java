package com.zl.controllers;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sun.management.ThreadMXBean;
import com.zl.abstracts.AJob;
import com.zl.jobs.JobHelper;
import com.zl.jobs.WebCrawlingJobFactory;
import com.zl.managers.JobManager;
import com.zl.resources.RSimpleResponse;
import com.zl.resources.RWebCrawlingJob;
import com.zl.resources.SimpleResponseFactory;
import com.zl.utils.SimpleLogger;

@RestController
public class CJob {

	@Autowired
	public JobManager jobManager;
	
	@RequestMapping(value = "/addmasterjob", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse addSlaveJob(@RequestBody RWebCrawlingJob reqJob) {
		
		Runnable task = new Runnable() {

			@Override
			public void run() {
				ThreadMXBean bean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
				long[] threadIds = bean.findDeadlockedThreads(); // Returns null if no threads are deadlocked.

				if (threadIds != null) {
				    ThreadInfo[] infos = bean.getThreadInfo(threadIds);

				    for (ThreadInfo info : infos) {
//				        StackTraceElement[] stack = info.getStackTrace();
				        SimpleLogger.error(info.toString());
				    }
				    throw new RuntimeException("Deadlock found");
				}
			}
			
		};
		new Thread(task).start();
		
		
		
		
		
		SimpleLogger.info(this.getClass(), "[Server receives][/addmasterjob][" + reqJob.toString() + "]");

		String url = reqJob.getUrl();
		int depth = reqJob.getDepth();
		String type = reqJob.getType();
		
		RSimpleResponse response = null;
		
		if (type == null || url == null || !JobHelper.isValidTypeNameForWebCrawlingJob(type) 
				|| !JobHelper.isValidUrl(url) || !JobHelper.isValidDepth(String.valueOf(depth))) {
			response = SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		else {
			AJob job = WebCrawlingJobFactory.create(url, depth);
			boolean addJobSucceed = jobManager.addJob(job);
			if (!addJobSucceed) {
				return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot add job");
			}
			else
				response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		}
		
		SimpleLogger.info(this.getClass(), "[Server responses][/addmasterjob][" + response.toString() + "]");
		return response;
	}
}