package com.luol.httpserver;

/**
 * Created by ruanlisi on 17/6/3.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
@EnableAutoConfiguration
public class SampleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/hangup/{uuid}/{timeout}")
    @ResponseBody
    String hangup(@PathVariable("uuid") String uuid, @PathVariable("timeout") Long timeout) throws InterruptedException {
        LOGGER.info("request uuid:{}, timeout:{}", uuid, timeout);
        TimeUnit.MILLISECONDS.sleep(timeout);
        LOGGER.info("response uuid:{}, timeout:{}", uuid, timeout);
        return "ok";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}