package org.example.request_processing.requests;

import org.example.data.dto.UploadFileDTO;

public record CreateCommitRequest(FileRequest fileInfo, UploadFileDTO file) {
}
