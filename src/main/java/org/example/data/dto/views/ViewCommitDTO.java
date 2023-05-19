package org.example.data.dto.views;

import java.util.Date;
import java.util.List;

public record ViewCommitDTO(Date date, String message, String sha, String url, String commiter) {

}
