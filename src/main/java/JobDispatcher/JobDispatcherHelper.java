package JobDispatcher;

import interfaces.ISlaveManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import Job.JobHelper;
import Job.WebCrawlingJob;
import ServerNode.SlaveNode;
import abstracts.AJob;

public class JobDispatcherHelper {
	
	private static String URI =  "/addslavejob";

	public static int hashCode(String s) {
		if (s == null)
			return 0;
		int hash = 7;
		char[] chars = s.toCharArray();
		for (char c : chars)
			hash = (hash * 31 + c) % 31;
		return hash;
	}
	
	/**
	 * find the slave to process the job
	 * 
     * @param strs: A list of strings
     * @return: A list of strings
     */
	static SlaveNode findSlave(AJob job, ISlaveManager slaveManager) {
		if (job == null || !(job instanceof WebCrawlingJob))
			return null;
		String domain = JobHelper.getDomainFromUrl(((WebCrawlingJob)job).getUrl().toString());
		int hash = JobDispatcherHelper.hashCode(domain);
		return slaveManager.getSlave(hash % slaveManager.getNum());
	}
	
	static String constructRequestUrl(SlaveNode slave) {
		return slave.getDomain() + URI;
	}
	
	static MultiValueMap<String, Object> constructRequestJson(AJob job) {		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("type", "webcrawling");
		map.add("url", ((WebCrawlingJob)job).getUrl().toString());
		map.add("depth", Integer.valueOf(((WebCrawlingJob)job).getDepth()));
		return map;
	}
	
	static HttpEntity<MultiValueMap<String, Object>> constructRequestHttpEntity(AJob job) {
		return new HttpEntity<MultiValueMap<String, Object>>(constructRequestJson(job), new HttpHeaders());
	}
}
