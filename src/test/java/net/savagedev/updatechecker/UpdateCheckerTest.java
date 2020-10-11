package net.savagedev.updatechecker;

public class UpdateCheckerTest {
    private static final int TEST_RESOURCE_ID = 36641; // Resource ID of my PlayerListGUI plugin. (https://www.spigotmc.org/resources/playerlistgui.36641/)

    public static void main(String[] args) {
        new ResourceUpdateChecker(TEST_RESOURCE_ID)
                .addSuccessListener(System.out::println)
                .addFailureListener(exception -> {
                    System.out.println("Failed to check for updates!");
                    exception.printStackTrace();
                })
                .checkUpdatesAsync();
    }
}
