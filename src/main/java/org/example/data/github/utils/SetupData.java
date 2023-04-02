package org.example.data.github.utils;

import org.kohsuke.github.GitHub;

public record SetupData(byte[] jwt, GitHub client) {

}
