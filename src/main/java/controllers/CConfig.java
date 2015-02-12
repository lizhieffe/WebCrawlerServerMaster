package controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import resources.RSimpleResponse;
import resources.SimpleResponseFactory;
import utils.SimpleLogger;

@RestController
public class CConfig {
	@RequestMapping(value = "/getip", method = RequestMethod.GET)
	public RSimpleResponse addSlaveJob() {
		return SimpleResponseFactory.generateSuccessfulSerciveResponseTemplate();
	}
}
