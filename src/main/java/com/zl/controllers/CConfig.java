package com.zl.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import resources.RSimpleResponse;
import resources.SimpleResponseFactory;

@RestController
public class CConfig {
	@RequestMapping(value = "/getip", method = RequestMethod.GET)
	public RSimpleResponse getIp() {
		return SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
	}
}
