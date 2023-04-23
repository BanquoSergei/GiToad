package org.example.request_processing.requests;

public record   FileRequest(String repository, String branch, String path, String content, String message) {
}
