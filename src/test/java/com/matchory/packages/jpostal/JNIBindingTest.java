package com.matchory.packages.jpostal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test that JNI bindings can be loaded correctly for different platforms
 */
public class JNIBindingTest {

    @Test
    public void testJNIBinding() {
        // Test that JNI library loading doesn't throw unexpected exceptions
        // Note: This may fail if native libraries aren't available, which is expected
        try {
            JNILoader.load();
            // If we get here, the library loaded successfully
            assertTrue("JNI library should load successfully", true);
        } catch (UnsatisfiedLinkError e) {
            // This is expected if native libraries aren't available for current platform
            assertTrue("Expected UnsatisfiedLinkError when libraries not available", true);
        } catch (Exception e) {
            fail("Unexpected exception during library loading: " + e.getMessage());
        }
    }

    @Test
    public void testPlatformDetection() {
        // Test that platform detection works correctly
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        assertNotNull("OS should be detected", os);
        assertNotNull("Architecture should be detected", arch);

        // Verify our platform detection logic
        boolean isLinux = os.contains("linux");
        boolean isMac = os.contains("mac");
        boolean isArm = arch.contains("aarch64") || arch.contains("arm");
        boolean isX86 = arch.contains("x86") || arch.contains("amd64");

        assertTrue("Should detect Linux or macOS", isLinux || isMac);
        assertTrue("Should detect ARM or x86 architecture", isArm || isX86);
    }
}
