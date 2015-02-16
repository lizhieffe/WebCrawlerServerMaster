package com.zl.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ServerNode.ServerNodeHelper;
import ServerNode.SlaveNode;

import com.zl.daemons.JobDispatchDaemon;
import com.zl.interfaces.ISlaveManager;

@Component
public class SlaveManager implements ISlaveManager {
	
	@Autowired
	public JobDispatchDaemon jobDispatchDaemon;
	
	private List<SlaveNode> slaves;
	private Map<String, SlaveNode> relationIpSlave;
		
	public SlaveManager() {
		this.slaves = new ArrayList<SlaveNode>();
		this.relationIpSlave = new HashMap<String, SlaveNode>();
	}
	
	@Override
	public boolean addSlave(String ip, int port) {
		
		synchronized(this) {
			if (relationIpSlave.containsKey(ServerNodeHelper.constructHost(ip, port)))
				return false;
		}
		
		SlaveNode slave = new SlaveNode();
		slave.setIp(ip);
		slave.setPort(port);
		synchronized(this) {
			slaves.add(slave);
			relationIpSlave.put(slave.getDomain(), slave);
		}
		jobDispatchDaemon.onSlaveAdded();
		return true;
	}
	
	@Override
	synchronized public boolean removeSlave(SlaveNode slave) {
		if (!slaves.remove(slave))
			return false;
		relationIpSlave.remove(slave.getIp());
		return true;
	}

	@Override
	synchronized public boolean removeSlave(String ip, int port) {
		if (!relationIpSlave.containsKey(ServerNodeHelper.constructHost(ip, port)))
			return false;
		SlaveNode slave = relationIpSlave.remove(ServerNodeHelper.constructHost(ip, port));
		return removeSlave(slave);
	}

	@Override
	synchronized public boolean containsSlave(String ip, int port) {
		return relationIpSlave.containsKey(ServerNodeHelper.constructHost(ip, port));
	}
	
	@Override
	synchronized public SlaveNode getSlave(String ip, int port) {
		if (relationIpSlave.containsKey(ServerNodeHelper.constructHost(ip, port)))
			return relationIpSlave.get(ServerNodeHelper.constructHost(ip, port));
		return null;
	}
	
	@Override
	synchronized public SlaveNode getSlave(int index) {
		if (index > getNum())
			return null;
		return slaves.get(index);
	}
	
	@Override
	synchronized public List<SlaveNode> getSlaves() {
		return new ArrayList<SlaveNode>(slaves);
	}
	
	@Override
	synchronized public int getNum() {
		return slaves.size();
	}
}
