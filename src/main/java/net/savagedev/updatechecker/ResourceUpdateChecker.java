package net.savagedev.updatechecker;

import net.savagedev.updatechecker.listeners.FailureListener;
import net.savagedev.updatechecker.listeners.SuccessListener;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ResourceUpdateChecker {
    private static final String SPIGOT_API_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    private final int resourceId;

    private SuccessListener successListener = version -> {
        throw new IllegalStateException("Add a SuccessListener before checking for resource updates.");
    };
    private FailureListener failureListener = Throwable::printStackTrace;

    public ResourceUpdateChecker(final int resourceId) {
        this.resourceId = resourceId;
    }

    public void checkUpdatesAsync() {
        EXECUTOR.execute(this::checkUpdatesSync);
    }

    public void checkUpdatesSync() {
        try {
            final HttpsURLConnection connection = (HttpsURLConnection) new URL(SPIGOT_API_URL + this.resourceId).openConnection();
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                this.successListener.onSuccess(reader.readLine());
            }
        } catch (IOException e) {
            this.failureListener.onFailure(e);
        }
    }

    public ResourceUpdateChecker addSuccessListener(SuccessListener successListener) {
        if (successListener == null) {
            throw new IllegalArgumentException("SuccessListener cannot be null!");
        } else {
            this.successListener = successListener;
        }
        return this;
    }

    public ResourceUpdateChecker addFailureListener(FailureListener failureListener) {
        if (failureListener == null) {
            throw new IllegalArgumentException("FailureListener cannot be null!");
        } else {
            this.failureListener = failureListener;
        }
        return this;
    }
}
