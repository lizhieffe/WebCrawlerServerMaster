package com.zl.interfaces;

public interface IJobDispatchDaemon extends ISlaveMonitor, IJobToDispatchMonitor {
	public void start();
//	public void onJobToDispatchAdded();
//	public void onSlaveAdded();
//	public void onSlaveRemoved();
}
