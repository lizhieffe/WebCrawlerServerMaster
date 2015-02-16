package com.zl.interfaces;

import java.util.List;

import com.zl.server.nodes.SlaveNode;

public interface ISlaveManager {
	public boolean addSlave(String ip, int port);
	public boolean removeSlave(SlaveNode slave);
	public boolean removeSlave(String ip, int port);
	public boolean containsSlave(String ip, int port);
	public SlaveNode getSlave(String ip, int port);
	public SlaveNode getSlave(int index);
	public List<SlaveNode> getSlaves();
	public int getNum();
}
