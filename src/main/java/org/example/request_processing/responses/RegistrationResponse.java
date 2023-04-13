package org.example.request_processing.responses;

import java.util.List;


public record RegistrationResponse(List<String> violations) {
}
