package com.zl.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zl.resources.RSimpleResponse;
import com.zl.resources.SimpleResponseFactory;

@RestController
public class CConfig {
	@RequestMapping(value = "/getip", method = RequestMethod.GET)
	public RSimpleResponse getIp() {
		return SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
	}
}
