package com.zl.interfaces;

import com.zl.abstracts.AJob;
import com.zl.server.nodes.SlaveNode;

public interface IDispatchJobService {
	public void dispatchJob(SlaveNode slave, AJob job);
}
