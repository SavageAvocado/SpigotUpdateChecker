package net.savagedev.updatechecker.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultScheduler implements Scheduler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void scheduleAsync(Runnable runnable, int period, TimeUnit timeUnit) {
        this.executor.scheduleAtFixedRate(runnable, 0L, period, timeUnit);
    }

    @Override
    public void executeAsync(Runnable runnable) {
        this.executor.execute(runnable);
    }
}
