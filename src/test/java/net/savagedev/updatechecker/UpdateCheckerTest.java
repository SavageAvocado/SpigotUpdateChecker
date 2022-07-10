package net.savagedev.updatechecker;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateCheckerTest {
    private static final int EXAMPLE_RESOURCE_ID = 36641;

    private final ResourceUpdateChecker modernUpdateChecker = new ResourceUpdateChecker.Builder()
            .setResourceId(EXAMPLE_RESOURCE_ID)
            .create();

    private final ResourceUpdateChecker legacyUpdateChecker = new ResourceUpdateChecker.Builder()
            .setResourceId(EXAMPLE_RESOURCE_ID)
            .setLegacy(true)
            .create();

    @Test
    public void testUpdateCheckerBuilderFailure() {
        assertThrows(IllegalStateException.class, () ->
                new ResourceUpdateChecker.Builder().create()
        );
    }

    @Test
    public void testUpdateCheckerBuilderSuccess() {
        assertDoesNotThrow(() ->
                new ResourceUpdateChecker.Builder().setResourceId(EXAMPLE_RESOURCE_ID).create()
        );
    }

    @Test
    public void testUpdateChecker() {
        try {
            assertEquals("2.1.0", this.modernUpdateChecker.checkForUpdateAsync().get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testResourceUrlBuilderSuccess() {
        assertEquals("https://www.spigotmc.org/resources/playerlistgui.36641", this.modernUpdateChecker.getResourceUrl());
    }

    @Test
    public void testResourceUrlBuilderFailure() {
        assertThrows(UnsupportedOperationException.class, this.legacyUpdateChecker::getResourceUrl);
    }
}
