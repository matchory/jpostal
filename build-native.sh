#!/usr/bin/env bash

set -e

echo "Building for MacOS aarch64…"
./gradlew clean build -PbuildNative=true
cp build/libs/jpostal/shared/libjpostal.dylib libs/jni/macos/aarch64/

# Ensure output directories exist
mkdir -p libs/jni/linux/{x86_64, aarch64}

echo "Building for Linux x86_64…"
docker build --platform linux/amd64 -f Dockerfile.jni -t jpostal-jni:x86_64 .
docker run --rm --platform linux/amd64 -v "$(pwd)/libs/jni/linux/x86_64":/output jpostal-jni:x86_64 \
    bash -c "cp build/libs/jpostal/shared/libjpostal.so /output/"

echo "Building for Linux aarch64…"
docker build --platform linux/arm64 -f Dockerfile.jni -t jpostal-jni:aarch64 .
docker run --rm --platform linux/arm64 -v "$(pwd)/libs/jni/linux/aarch64":/output jpostal-jni:aarch64 \
    bash -c "cp build/libs/jpostal/shared/libjpostal.so /output/"

echo "JNI bindings built successfully!"
echo "MacOS aarch64:"
ls -la libs/jni/macos/aarch64/
file libs/jni/macos/aarch64/libjpostal.dylib

echo "Linux x86_64:"
ls -la libs/jni/linux/x86_64/
file libs/jni/linux/x86_64/libjpostal.so

echo "Linux aarch64:"
ls -la libs/jni/linux/aarch64/
file libs/jni/linux/aarch64/libjpostal.so

echo "Cleaning up Docker images…"
docker rmi jpostal-jni:x86_64 jpostal-jni:aarch64 || true
