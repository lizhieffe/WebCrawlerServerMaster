package com.zl.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zl.job.JobManager;

import resources.RSimpleResponse;
import resources.RWebCrawlingJob;
import resources.SimpleResponseFactory;
import utils.SimpleLogger;
import Job.JobHelper;
import Job.WebCrawlingJobFactory;
import abstracts.AJob;

@RestController
public class CJob {

	@RequestMapping(value = "/addmasterjob", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse addSlaveJob(@RequestBody RWebCrawlingJob reqJob) {
		
		SimpleLogger.info(this.getClass(), "[Request] URI=/addmasterjob, URL=" + reqJob.getUrl() + ", Depth=" + reqJob.getDepth());

		String url = reqJob.getUrl();
		int depth = reqJob.getDepth();
		String type = reqJob.getType();
		
		if (type == null || url == null || !JobHelper.isValidTypeNameForWebCrawlingJob(type) 
				|| !JobHelper.isValidUrl(url) || !JobHelper.isValidDepth(String.valueOf(depth))) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		
		AJob job = WebCrawlingJobFactory.create(url, depth);
		boolean addJobSucceed = JobManager.getInstance().addJob(job);
		if (!addJobSucceed) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot add job");
		}
		
		return SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
	}
}