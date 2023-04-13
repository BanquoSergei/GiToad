package org.example.request_processing.responses;

import org.example.data.dto.views.FileViewDTO;

import java.util.List;


public record CommitResponse(List<FileViewDTO> files) {

}
