package example.micronaut.service;

import example.micronaut.task.EmailTask;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Singleton
public class RegisterUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUseCase.class);

    protected final TaskScheduler taskScheduler;
    protected final EmailUseCase emailUseCase;

    public RegisterUseCase(EmailUseCase emailUseCase,
                           @Named(TaskExecutors.SCHEDULED) TaskScheduler taskScheduler) {
        this.emailUseCase = emailUseCase;
        this.taskScheduler = taskScheduler;
    }

    public void register(String email) {
        LOG.info("saving {} at {}", email, new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()));
        scheduleFollowupEmail(email, "Welcome to the Micronaut framework");
    }

    private void scheduleFollowupEmail(String email, String message) {
        EmailTask task = new EmailTask(emailUseCase, email, message);
        taskScheduler.schedule(Duration.ofSeconds(10), task);
    }
}
