package org.example.controllers.responses;

import org.example.github.dto.GHContentDTO;

import java.util.List;


public record CommitResponse(List<GHContentDTO> files) {

}
