package net.savagedev.updatechecker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.savagedev.updatechecker.scheduler.DefaultScheduler;
import net.savagedev.updatechecker.scheduler.Scheduler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ResourceUpdateChecker {
    private static final String SPIGOT_RESOURCE_URL_BASE = "https://www.spigotmc.org/resources/";

    private final Scheduler scheduler;
    private final UpdateChecker updateChecker;
    private final int resourceId;

    public ResourceUpdateChecker(Builder builder) {
        this.scheduler = builder.scheduler;
        this.updateChecker = builder.updateChecker;
        this.resourceId = builder.resourceId;
    }

    public CompletableFuture<String> checkForUpdateAsync() {
        final CompletableFuture<String> future = new CompletableFuture<>();
        this.scheduler.executeAsync(() -> {
            try {
                future.complete(this.updateChecker.getMostRecentVersion(this.resourceId));
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    // Probably going to remove this, it's kinda pointless...
    public String getResourceUrl() {
        if (this.updateChecker.isLegacy()) {
            throw new UnsupportedOperationException("Resource URLs cannot be built by legacy implementations.");
        }
        try {
            return SPIGOT_RESOURCE_URL_BASE + ((ModernUpdateChecker) this.updateChecker).getResourceName(this.resourceId).toLowerCase(Locale.ROOT) + "." + this.resourceId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {

        private UpdateChecker updateChecker = new ModernUpdateChecker();

        private Scheduler scheduler = new DefaultScheduler();

        private int resourceId = -1;

        public Builder setResourceId(int resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder setLegacy(boolean legacy) {
            if (legacy) {
                this.updateChecker = new LegacyUpdateChecker();
            } else {
                this.updateChecker = new ModernUpdateChecker();
            }
            return this;
        }

        public Builder setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public ResourceUpdateChecker create() {
            if (this.resourceId == -1) {
                throw new IllegalStateException("A resource ID must be provided!");
            }
            return new ResourceUpdateChecker(this);
        }
    }

    private interface UpdateChecker {
        /**
         * Check what the most recently available version of a Spigot resource is.
         *
         * @param resourceId - The ID of the resource you want to check.
         * @return The most recent version of the resource available.
         */
        String getMostRecentVersion(int resourceId) throws IOException;

        /**
         * @return Whether the current implementation is using the legacy Spigot API.
         */
        boolean isLegacy();
    }

    private static class LegacyUpdateChecker implements UpdateChecker {
        private static final String LEGACY_SPIGOT_API_URL = "https://api.spigotmc.org/legacy/update.php?resource=";

        private final HttpClient client = HttpClient.newHttpClient();

        @Override
        public String getMostRecentVersion(int resourceId) throws IOException {
            try {
                return this.client.send(HttpRequest.newBuilder(URI.create(LEGACY_SPIGOT_API_URL + resourceId)).GET().build(), HttpResponse.BodyHandlers.ofString()).body();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean isLegacy() {
            return true;
        }
    }

    private static class ModernUpdateChecker implements UpdateChecker {
        private static final String MODERN_SPIGOT_API_URL = "https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=";

        private final HttpClient client = HttpClient.newHttpClient();

        private String resourceName;

        @Override
        public String getMostRecentVersion(int resourceId) throws IOException {
            final JsonObject object = this.getResource(resourceId);
            if (this.resourceName == null) {
                this.resourceName = object.get("title").getAsString();
            }
            return object.get("current_version").getAsString();
        }

        private String getResourceName(int resourceId) throws IOException {
            if (this.resourceName == null) {
                this.resourceName = this.getResource(resourceId).get("title").getAsString();
            }
            return this.resourceName;
        }

        private JsonObject getResource(int resourceId) throws IOException {
            try {
                return JsonParser.parseString(
                        this.client.send(
                                HttpRequest.newBuilder(URI.create(MODERN_SPIGOT_API_URL + resourceId)).GET().build(),
                                HttpResponse.BodyHandlers.ofString()
                        ).body()
                ).getAsJsonObject();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean isLegacy() {
            return false;
        }
    }
}
