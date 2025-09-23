package com.matchory.packages.jpostal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class JNILoader {
    private static boolean loaded = false;

    public static synchronized void load() throws IOException {
        if (loaded) {
            return;
        }

        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        String folder = os.contains("mac") ? "macos" : "linux";

        if (arch.contains("aarch64") || arch.contains("arm")) {
            arch = "aarch64";
        } else if (arch.contains("64")) {
            arch = "x86_64";
        }

        // Try platform-specific paths first (universal JAR), then fall back to simplified paths (platform-specific JAR)
        loadLibpostal(folder, arch);
        loadJNIBindings(folder, arch);

        loaded = true;
    }

    private static void loadLibpostal(String folder, String arch) throws IOException {
        String libpostalName;
        if (folder.equals("macos")) {
            libpostalName = "libpostal.1.dylib";
        } else {
            libpostalName = "libpostal.so";
        }

        // Try platform-specific path first (universal JAR)
        String platformPath = "/native/" + folder + "/" + arch + "/" + libpostalName;
        InputStream in = JNILoader.class.getResourceAsStream(platformPath);

        // Fall back to simplified path (platform-specific JAR)
        if (in == null) {
            String simplePath = "/native/" + libpostalName;
            in = JNILoader.class.getResourceAsStream(simplePath);
        }

        if (in == null) {
            throw new UnsatisfiedLinkError("libpostal library not found. Tried: " + platformPath + " and /native/" + libpostalName);
        }

        File temp = File.createTempFile("libpostal", libpostalName.substring(libpostalName.lastIndexOf('.')));
        temp.deleteOnExit();
        Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);

        System.load(temp.getAbsolutePath());
    }

    private static void loadJNIBindings(String folder, String arch) throws IOException {
        String libName = System.mapLibraryName("jpostal");

        // Try platform-specific path first (universal JAR)
        String platformPath = "/native/" + folder + "/" + arch + "/" + libName;
        InputStream in = JNILoader.class.getResourceAsStream(platformPath);

        // Fall back to simplified path (platform-specific JAR)
        if (in == null) {
            String simplePath = "/native/" + libName;
            in = JNILoader.class.getResourceAsStream(simplePath);
        }

        if (in == null) {
            throw new UnsatisfiedLinkError("JNI bindings not found. Tried: " + platformPath + " and /native/" + libName);
        }

        File temp = File.createTempFile("libjpostal", libName.substring(libName.lastIndexOf('.')));
        temp.deleteOnExit();
        Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);

        System.load(temp.getAbsolutePath());
    }
}
