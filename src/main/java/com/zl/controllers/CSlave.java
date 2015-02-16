package com.zl.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.zl.managers.SlaveManager;

@RestController
public class CSlave {

	@Autowired
	public SlaveManager slaveManager;
	
	@RequestMapping(value = "/addslave", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse addSlave(@RequestBody RSlave slave) {
		
		SimpleLogger.info(this.getClass(), "[/addslave] Receive: [" + slave.toString() + "]");

		String ip = slave.getIp();
		int port = slave.getPort();
		
		if (ip == null || !ServerNodeHelper.isValidIp(ip) || !ServerNodeHelper.isValidPort(String.valueOf(port))) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		
		boolean addSlaveSucceed = slaveManager.containsSlave(ip, port) || slaveManager.addSlave(ip, port);
		if (!addSlaveSucceed) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot add slave");
		}
			
		RSimpleResponse response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		SimpleLogger.info(this.getClass(), "[/addslave] Send: [" + response.toString() + "]");

		return response;	
	}
	
	
	
	@RequestMapping(value = "/removeslave", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse removeSlave(@RequestBody RSlave slave) {
		
		SimpleLogger.info(this.getClass(), "[/removeslave] Receive: [" + slave.toString() + "]");

		String ip = slave.getIp();
		int port = slave.getPort();
		
		if (ip == null || !ServerNodeHelper.isValidIp(ip) || !ServerNodeHelper.isValidPort(String.valueOf(port))) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		
		boolean addSlaveSucceed = slaveManager.removeSlave(ip, port);
		if (!addSlaveSucceed) {
			return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot remove slave");
		}
			
		RSimpleResponse response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		SimpleLogger.info(this.getClass(), "[/removeslave] Send: [" + response.toString() + "]");

		return response;
	}
	
	@RequestMapping(value = "/getslaves", method = RequestMethod.GET, produces="application/json")
	public RSimpleResponse getSlaves() {
		
		SimpleLogger.info(this.getClass(), "[/removeslave] Receive");

		SimpleLogger.info(this.getClass(), "[Request] URI=/getslaves");
		List<SlaveNode> slaves = slaveManager.getSlaves();
		RSimpleResponse response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		response.setResponse(ResourceUtil.converToRSlaves(slaves));

		SimpleLogger.info(this.getClass(), "[/getslaves] Send: [" + response.toString() + "]");

		return response;
	}
}