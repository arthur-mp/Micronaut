package example.micronaut;

import example.micronaut.service.RegisterUseCase;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;

public class Application{

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

}

//@Singleton
//public class Application implements ApplicationEventListener<ServerStartupEvent> {
//
//    private final RegisterUseCase registerUseCase;
//
//    public Application(RegisterUseCase registerUseCase) {
//        this.registerUseCase = registerUseCase;
//    }
//
//    public static void main(String[] args) {
//        Micronaut.run(Application.class, args);
//    }
//
//    @Override
//    public void onApplicationEvent(ServerStartupEvent event) {
//        try {
//            registerUseCase.register("harry@micronaut.example");
//            Thread.sleep(2000);
//            registerUseCase.register("ron@micronaut.example");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}