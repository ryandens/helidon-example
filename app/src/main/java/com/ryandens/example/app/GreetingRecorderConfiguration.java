package com.ryandens.example.app;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class GreetingRecorderConfiguration {

  @Produces
  @ApplicationScoped
  GreetingRecorder greetingRecorder(final LoggingGreetingRecorder loggingGreetingRecorder) {
    return new TimedGreetingRecorder(loggingGreetingRecorder);
  }
}
