package microservices.book.multiplication.configuration;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfiguration {

  @Bean
  public TopicExchange challengesTopicExchange(
      @Value("${amqp.exchange.attempts}") final String exchangeName) {
    return ExchangeBuilder
        .topicExchange(exchangeName)
        .durable(true)
        .build();
  }

  /**
   * By injecting a bean of type Jackson2JsonMessageConverter, you’re overriding the
   * default Java object serializer by a JSON object serializer. You do this to avoid various
   * pitfalls of Java object serialization.
   * - It’s not a proper standard that you can use between programming
   * languages. If you introduced a consumer that’s not written in Java,
   * you would have to look for a specific library to perform cross language deserialization.
   * - It uses a hard-coded, fully qualified type name in the header of the
   * message. The deserializer expects the Java bean to be located in the
   * same package and to have the same name and fields. This is not
   * flexible at all, since you may want to deserialize only some properties
   * and keep your own version of the event data, following good domain driven design practices.
   *
   * @return Jackson2JsonMessageConverter
   */
  @Bean
  public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
