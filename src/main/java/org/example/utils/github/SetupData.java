package org.example.utils.github;

import org.kohsuke.github.GitHub;

record SetupData(byte[] jwt, GitHub client) {

}
