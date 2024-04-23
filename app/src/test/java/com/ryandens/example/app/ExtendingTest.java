package com.ryandens.example.app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExtendingTest extends MixinTest {

  @BeforeEach
  void beforeEach() {
    init();
  }

  @Test
  void name() {
    assertThat(new GreetingMessage("Hello, World!").getMessage()).isNotBlank();
  }
}
