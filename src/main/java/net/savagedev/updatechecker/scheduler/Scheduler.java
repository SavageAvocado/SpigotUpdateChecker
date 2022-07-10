package net.savagedev.updatechecker.scheduler;

import java.util.concurrent.TimeUnit;

public interface Scheduler {
    void scheduleAsync(Runnable runnable, int period, TimeUnit timeUnit);

    void executeAsync(Runnable runnable);
}
