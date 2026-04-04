package com.ryandens.example.app;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;

@ApplicationScoped
@Typed(LoggingGreetingRecorder.class)
class LoggingGreetingRecorder implements GreetingRecorder {

  @Override
  public boolean record() {
    System.out.println("Greeting recorded!");
    return true;
  }
}
