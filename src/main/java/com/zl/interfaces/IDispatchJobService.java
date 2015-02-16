package com.zl.interfaces;

import com.zl.server.nodes.SlaveNode;
import com.zl.abstracts.AJob;

public interface IDispatchJobService {
	public void dispatchJob(SlaveNode slave, AJob job);
}
