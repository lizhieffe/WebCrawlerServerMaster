package com.zl.interfaces;

import ServerNode.SlaveNode;
import abstracts.AJob;

public interface IDispatchJobService {
	public void dispatchJob(SlaveNode slave, AJob job);
}
