package com.ryandens.example.app;

import static org.assertj.core.api.Assertions.assertThat;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

/**
 * test classes cannot be final because {@link HelidonTest} uses weld to create dynamic proxies of
 * test classes to do dependency injection
 */
@HelidonTest
class MainTest {

  @Inject WebTarget target;

  @Test
  void testHelloWorld() {

    GreetingMessage message = target.path("/greet").request().get(GreetingMessage.class);
    assertThat(message.getMessage()).isEqualTo("Hello World!");

    message = target.path("/greet/Joe").request().get(GreetingMessage.class);
    assertThat(message.getMessage()).isEqualTo("Hello Joe!");

    try (Response r =
        target
            .path("/greet/greeting")
            .request()
            .put(Entity.entity("{\"message\" : \"Hola\"}", MediaType.APPLICATION_JSON))) {
      assertThat(r.getStatus()).isEqualTo(204);
    }

    message = target.path("/greet/Jose").request().get(GreetingMessage.class);
    assertThat(message.getMessage()).isEqualTo("Hola Jose!");
  }

  @Test
  void testMetrics() {
    try (Response r = target.path("/metrics").request().get()) {
      assertThat(r.getStatus()).isEqualTo(200);
    }
  }

  @Test
  void testHealth() {
    try (Response r = target.path("/health").request().get()) {
      assertThat(r.getStatus()).isEqualTo(200);
    }
  }
}
