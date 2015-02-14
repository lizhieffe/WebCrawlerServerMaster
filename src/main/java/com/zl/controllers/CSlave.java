package com.zl.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import resources.RSimpleResponse;
import resources.RSlave;
import resources.SimpleResponseFactory;
import utils.ResourceUtil;
import utils.SimpleLogger;
import ServerNode.ServerNodeHelper;
import ServerNode.SlaveNode;

import com.zl.slave.SlaveManager;

@RestController
public class CSlave {

	@RequestMapping(value = "/addslave", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse addSlave(@RequestBody RSlave slave) {
		
		SimpleLogger.info(this.getClass(), "[Request] URI=/addslave, IP=" + slave.getIp() + ", Port=" + slave.getPort());

		String ip = slave.getIp();
		int port = slave.getPort();
		
		if (ip == null || !ServerNodeHelper.isValidIp(ip) || !ServerNodeHelper.isValidPort(String.valueOf(port))) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		
		boolean addSlaveSucceed = SlaveManager.getInstance().addSlave(ip, port);
		if (!addSlaveSucceed) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot add slave");
		}
			
		return SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
	}
	
	
	
	@RequestMapping(value = "/removeslave", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse removeSlave(@RequestBody RSlave slave) {
		
		SimpleLogger.info(this.getClass(), "[Request] URI=/removeslave, IP=" + slave.getIp() + ", Port=" + slave.getPort());

		String ip = slave.getIp();
		int port = slave.getPort();
		
		if (ip == null || !ServerNodeHelper.isValidIp(ip) || !ServerNodeHelper.isValidPort(String.valueOf(port))) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		
		boolean addSlaveSucceed = SlaveManager.getInstance().removeSlave(ip, port);
		if (!addSlaveSucceed) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot remove slave");
		}
			
		return SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
	}
	
	@RequestMapping(value = "/getslaves", method = RequestMethod.GET, produces="application/json")
	public RSimpleResponse getSlaves() {
		
		SimpleLogger.info(this.getClass(), "[Request] URI=/getslaves");
		List<SlaveNode> slaves = SlaveManager.getInstance().getSlaves();
		RSimpleResponse response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		response.setResponse(ResourceUtil.converToRSlaves(slaves));
		return response;
	}
}