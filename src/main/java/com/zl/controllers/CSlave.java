package com.zl.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zl.resources.RSimpleResponse;
import com.zl.resources.RSlave;
import com.zl.resources.SimpleResponseFactory;
import com.zl.utils.ResourceUtil;
import com.zl.utils.SimpleLogger;
import com.zl.server.nodes.ServerNodeHelper;
import com.zl.server.nodes.SlaveNode;

import com.zl.managers.SlaveManager;

@RestController
public class CSlave {

	@Autowired
	public SlaveManager slaveManager;
	
	@RequestMapping(value = "/addslave", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse addSlave(@RequestBody RSlave slave) {
		
		SimpleLogger.info(this.getClass(), "[Server receives][/addslave][" + slave.toString() + "]");

		String ip = slave.getIp();
		int port = slave.getPort();
		
		RSimpleResponse response = null;
		if (ip == null || !ServerNodeHelper.isValidIp(ip) || !ServerNodeHelper.isValidPort(String.valueOf(port))) {
			response = SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		else {
			boolean addSlaveSucceed = slaveManager.containsSlave(ip, port) || slaveManager.addSlave(ip, port);
			if (!addSlaveSucceed) {
				return SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot add slave");
			}
			else
				response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		}
		
		SimpleLogger.info(this.getClass(), "[Server responses][/addslave][" + response.toString() + "]");
		return response;	
	}
	
	
	
	@RequestMapping(value = "/removeslave", method = RequestMethod.POST, consumes="application/json",produces="application/json")
	public RSimpleResponse removeSlave(@RequestBody RSlave slave) {
		
		SimpleLogger.info(this.getClass(), "[Server receives][/removeslave][" + slave.toString() + "]");

		String ip = slave.getIp();
		int port = slave.getPort();
		
		RSimpleResponse response = null;
		if (ip == null || !ServerNodeHelper.isValidIp(ip) || !ServerNodeHelper.isValidPort(String.valueOf(port))) {
			response = SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Invalid parameter");
		}
		else {
			boolean addSlaveSucceed = slaveManager.removeSlave(ip, port);
			if (!addSlaveSucceed) {
				response = SimpleResponseFactory.generateFailSerciveResponseTemplate(1, "", "Cannot remove slave");
			}
			else
				response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		}
			
		SimpleLogger.info(this.getClass(), "[Server responses][/removeslave][" + response.toString() + "]");
		return response;
	}
	
	@RequestMapping(value = "/getslaves", method = RequestMethod.GET, produces="application/json")
	public RSimpleResponse getSlaves() {
		
		SimpleLogger.info(this.getClass(), "[Server receives][/removeslave]");

		SimpleLogger.info(this.getClass(), "[Request] URI=/getslaves");
		List<SlaveNode> slaves = slaveManager.getSlaves();
		RSimpleResponse response = SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
		response.setResponse(ResourceUtil.converToRSlaves(slaves));

		SimpleLogger.info(this.getClass(), "[Server responses][/getslaves][" + response.toString() + "]");
		return response;
	}
}