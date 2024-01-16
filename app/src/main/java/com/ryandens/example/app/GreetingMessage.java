package com.ryandens.example.app;

/** POJO defining the greeting message content. */
public class GreetingMessage {
  private String message;

  /** Create a new GreetingMessage instance. */
  @SuppressWarnings("unused")
  public GreetingMessage() {}

  /**
   * Create a new GreetingMessage instance.
   *
   * @param message message
   */
  public GreetingMessage(String message) {
    this.message = message;
  }

  /**
   * Gets the message value.
   *
   * @return message value
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message value.
   *
   * @param message message value to set
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
