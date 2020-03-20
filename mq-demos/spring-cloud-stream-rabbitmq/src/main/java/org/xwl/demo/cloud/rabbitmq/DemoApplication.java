package org.xwl.demo.cloud.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;

@SpringBootApplication
@EnableBinding({Sink.class, DemoApplication.MySink.class})
public class DemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);		
	}
	
	@StreamListener(Sink.INPUT)
	public void handle1(Message<String> msg) {
		System.out.println("Received message from " + Sink.INPUT + " ::: " + msg.getPayload());
	}

	public interface MySink{
		public final String INPUT = "remote_input";

		@Input
		SubscribableChannel remote_input();
	}

	@StreamListener(MySink.INPUT)
	public void handle2(Message<String> msg) {
		System.out.println("Received message from " + MySink.INPUT + " ::: " + msg.getPayload());
	}

}
