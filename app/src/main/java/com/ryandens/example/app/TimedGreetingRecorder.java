package com.ryandens.example.app;

import com.ryandens.delegation.AutoDelegate;

@AutoDelegate(GreetingRecorder.class)
public final class TimedGreetingRecorder extends AutoDelegate_TimedGreetingRecorder
    implements GreetingRecorder {

  public TimedGreetingRecorder(final GreetingRecorder recorder) {
    super(recorder);
  }

  @Override
  public boolean record() {
    final long start = System.nanoTime();
    final var result = super.record();
    final long end = System.nanoTime();
    System.out.println("TimedGreetingRecorder: " + (end - start));
    return result;
  }
}
