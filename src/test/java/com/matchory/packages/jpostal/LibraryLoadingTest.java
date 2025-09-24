package com.matchory.packages.jpostal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive test to verify library loading and basic functionality
 */
public class LibraryLoadingTest {

    @Test
    public void testPlatformDetection() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        System.out.println("Detected OS: " + os);
        System.out.println("Detected Architecture: " + arch);

        // Verify platform detection logic matches JNILoader
        String expectedFolder = os.contains("mac") ? "macos" : "linux";
        String expectedArch;
        if (arch.contains("aarch64") || arch.contains("arm")) {
            expectedArch = "aarch64";
        } else if (arch.contains("64")) {
            expectedArch = "x86_64";
        } else {
            expectedArch = "unknown";
        }

        System.out.println("Expected folder: " + expectedFolder);
        System.out.println("Expected arch: " + expectedArch);

        assertNotNull("OS should be detected", os);
        assertNotNull("Architecture should be detected", arch);
        assertTrue("Should detect supported OS", expectedFolder.equals("macos") || expectedFolder.equals("linux"));
        assertTrue("Should detect supported architecture", expectedArch.equals("aarch64") || expectedArch.equals("x86_64"));
    }

    @Test
    public void testLibraryResourcesExist() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        String folder = os.contains("mac") ? "macos" : "linux";

        if (arch.contains("aarch64") || arch.contains("arm")) {
            arch = "aarch64";
        } else if (arch.contains("64")) {
            arch = "x86_64";
        }

        // Check if libpostal library exists in JAR
        String libpostalName = folder.equals("macos") ? "libpostal.1.dylib" : "libpostal.so";
        String libpostalPath = "/native/" + folder + "/" + arch + "/" + libpostalName;

        System.out.println("Looking for libpostal at: " + libpostalPath);
        assertNotNull("libpostal library should exist in JAR",
                     LibraryLoadingTest.class.getResourceAsStream(libpostalPath));

        // Check if JNI bindings exist in JAR
        String jniName = System.mapLibraryName("jpostal");
        String jniPath = "/native/" + folder + "/" + arch + "/" + jniName;

        System.out.println("Looking for JNI bindings at: " + jniPath);
        assertNotNull("JNI bindings should exist in JAR",
                     LibraryLoadingTest.class.getResourceAsStream(jniPath));
    }

    @Test
    public void testJNILoaderLoad() {
        System.out.println("Testing JNILoader.load()...");

        try {
            JNILoader.load();
            System.out.println("✅ JNILoader.load() succeeded!");
            assertTrue("JNI loading should succeed", true);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("❌ UnsatisfiedLinkError: " + e.getMessage());
            fail("JNI loading failed with UnsatisfiedLinkError: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            fail("JNI loading failed with unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleLoads() {
        System.out.println("Testing multiple JNILoader.load() calls...");

        try {
            // First load
            JNILoader.load();
            System.out.println("✅ First load succeeded");

            // Second load (should be no-op)
            JNILoader.load();
            System.out.println("✅ Second load succeeded (should be no-op)");

            // Third load
            JNILoader.load();
            System.out.println("✅ Third load succeeded (should be no-op)");

            assertTrue("Multiple loads should work", true);
        } catch (Exception e) {
            fail("Multiple loads failed: " + e.getMessage());
        }
    }
}
