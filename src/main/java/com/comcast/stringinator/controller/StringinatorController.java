package com.comcast.stringinator.controller;

import com.comcast.stringinator.exceptionHandling.EmptyInputCustomException;
import com.comcast.stringinator.model.StatsResult;
import com.comcast.stringinator.model.StringinatorInput;
import com.comcast.stringinator.model.StringinatorResult;
import com.comcast.stringinator.service.StringinatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

@RestController
public class StringinatorController {

    private static final Logger log = LoggerFactory.getLogger(StringinatorController.class);

    @Autowired
    private StringinatorService stringinatorService;

//    @Autowired
//    private RateLimiterRegistry rateLimiterRegistry;

    @GetMapping("/")
	public String index() {
        log.info("Received request for index endpoint");
        return "<pre>\n" +
		"Welcome to the Stringinator 3000 for all of your string manipulation needs.\n" +
		"GET / - You're already here! \n" +
		"POST /stringinate - Get all of the info you've ever wanted about a string. Takes JSON of the following form: {\"input\":\"your-string-goes-here\"}\n" +
		"GET /stats - Get statistics about all strings the server has seen, including the longest and most popular strings.\n" +
		"</pre>";
	}

    @GetMapping(path = "/stringinate", produces = "application/json")
    public StringinatorResult stringinateGet(@RequestParam(name = "input", required = true) String input) throws EmptyInputCustomException {
//        if (rateLimiterRegistry == null) {
//            System.out.println("Rate Limiter is not initialized");
//        }
        log.info("Received GET /stringinate request with input='{}'", input);
        if(input==null || input.isEmpty()){
            log.error("Input is empty...");
            throw new EmptyInputCustomException();
        }
        StringinatorResult result = stringinatorService.stringinate(new StringinatorInput(input));
        log.debug("GET /stringinate result for input='{}': {}", input, result);
        return result;
    }

	@PostMapping(path = "/stringinate", consumes = "application/json", produces = "application/json")
    public StringinatorResult stringinate(@RequestBody @Valid StringinatorInput input) {
        log.info("Received POST /stringinate request with input='{}'", input.getInput());
        StringinatorResult result = stringinatorService.stringinate(input);
        log.debug("POST /stringinate result for input='{}': {}", input.getInput(), result);
        return result;
    }

    @GetMapping(path = "/stats")
    public StatsResult stats() {
        log.info("Received GET /stats request");
        StatsResult result = stringinatorService.stats();
        log.debug("GET /stats result: {}", result);
        return result;
    }
}
