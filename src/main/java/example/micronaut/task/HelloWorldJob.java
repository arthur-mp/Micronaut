package example.micronaut.task;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Singleton
//public class HelloWorldJob {
//
//    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);
//
//    @Scheduled(fixedDelay = "10s")
//    void executeEveryTen(){
//        LOG.info("Simple Job every 10 second: {}", new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()));
//    }
//
//    @Scheduled(fixedDelay = "45s", initialDelay = "5s")
//    void executeEveryFourtyFive() {
//        LOG.info("Simple Job every 45 seconds: {}", new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()));
//    }
//}
