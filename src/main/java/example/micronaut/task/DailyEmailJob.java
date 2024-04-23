package example.micronaut.task;

import example.micronaut.service.EmailUseCase;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

@Singleton
public class DailyEmailJob {
    protected final EmailUseCase emailUseCase;

    public DailyEmailJob(EmailUseCase emailUseCase) {
        this.emailUseCase = emailUseCase;
    }

//    @Scheduled(cron = "*/5 * * * * *")
//    void execute() {
//        emailUseCase.send("john.doe@micronaut.example", "Test Message");
//    }
}
