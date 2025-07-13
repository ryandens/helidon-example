package com.ryandens.example.app;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class LoggingGreetingRecorder implements GreetingRecorder {

  @Override
  public boolean record() {
    System.out.println("Greeting recorded!");
    return true;
  }
}
