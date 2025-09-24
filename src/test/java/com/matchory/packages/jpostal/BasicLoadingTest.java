package com.matchory.packages.jpostal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic test to verify library loading works on macOS
 */
public class BasicLoadingTest {

    @Test
    public void testLibraryLoadingOnly() {
        System.out.println("=== Basic Library Loading Test ===");
        System.out.println();

        // Test 1: Platform Detection
        System.out.println("1. Platform Detection:");
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        System.out.println("   OS: " + os);
        System.out.println("   Architecture: " + arch);

        String expectedFolder = os.contains("mac") ? "macos" : "linux";
        String expectedArch = arch.contains("aarch64") || arch.contains("arm") ? "aarch64" : "x86_64";
        System.out.println("   Expected folder: " + expectedFolder);
        System.out.println("   Expected arch: " + expectedArch);
        System.out.println();

        // Test 2: Library Resources
        System.out.println("2. Library Resources Check:");
        String libpostalName = expectedFolder.equals("macos") ? "libpostal.1.dylib" : "libpostal.so";
        String libpostalPath = "/native/" + expectedFolder + "/" + expectedArch + "/" + libpostalName;
        String jniName = System.mapLibraryName("jpostal");
        String jniPath = "/native/" + expectedFolder + "/" + expectedArch + "/" + jniName;

        System.out.println("   Looking for libpostal at: " + libpostalPath);
        boolean libpostalExists = BasicLoadingTest.class.getResourceAsStream(libpostalPath) != null;
        System.out.println("   ✓ libpostal found: " + libpostalExists);
        assertTrue("libpostal library should exist in JAR", libpostalExists);

        System.out.println("   Looking for JNI bindings at: " + jniPath);
        boolean jniExists = BasicLoadingTest.class.getResourceAsStream(jniPath) != null;
        System.out.println("   ✓ JNI bindings found: " + jniExists);
        assertTrue("JNI bindings should exist in JAR", jniExists);
        System.out.println();

        // Test 3: JNI Loading
        System.out.println("3. JNI Library Loading:");
        try {
            JNILoader.load();
            System.out.println("   ✅ JNILoader.load() succeeded!");
            System.out.println("   ✅ Native libraries loaded successfully!");
        } catch (Exception e) {
            System.out.println("   ❌ JNILoader.load() failed: " + e.getMessage());
            fail("JNI loading should succeed: " + e.getMessage());
        }
        System.out.println();

        // Test 4: Multiple loads (should be safe)
        System.out.println("4. Multiple Load Test:");
        try {
            JNILoader.load(); // Second call
            JNILoader.load(); // Third call
            System.out.println("   ✅ Multiple JNILoader.load() calls succeeded!");
        } catch (Exception e) {
            System.out.println("   ❌ Multiple loads failed: " + e.getMessage());
            fail("Multiple JNI loads should be safe: " + e.getMessage());
        }
        System.out.println();

        System.out.println("🎉 Library loading verification passed!");
        System.out.println("   The universal JAR contains all required native libraries");
        System.out.println("   and they can be loaded successfully on this platform.");
    }
}
