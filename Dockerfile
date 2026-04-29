# Use an official Ubuntu base image
FROM ubuntu:22.04

# Set up environment variables
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

# Install necessary packages (Java 17, wget, unzip)
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk wget unzip && \
    apt-get clean

# Download and install Android Command Line Tools
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    cd ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip -O tools.zip && \
    unzip -q tools.zip && \
    mv cmdline-tools latest && \
    rm tools.zip

# Accept Android licenses and install platform tools
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Grant execution rights to the gradle wrapper
RUN chmod +x gradlew

# Default command to build the project
CMD ["./gradlew", "build"]
