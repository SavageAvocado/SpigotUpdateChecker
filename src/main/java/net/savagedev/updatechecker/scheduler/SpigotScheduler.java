package net.savagedev.updatechecker.scheduler;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class SpigotScheduler implements Scheduler {
    private final JavaPlugin plugin;

    public SpigotScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void scheduleAsync(Runnable runnable, int period, TimeUnit timeUnit) {
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, 0L, timeUnit.toSeconds(period) * 20);
    }

    @Override
    public void executeAsync(Runnable runnable) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }
}
