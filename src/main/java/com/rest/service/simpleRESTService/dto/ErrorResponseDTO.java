package com.rest.service.simpleRESTService.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class ErrorResponseDTO {
    private String message;
    private String documentationUrl;

    @JsonCreator
    public ErrorResponseDTO(@JsonProperty("message") String message, @JsonProperty("documentation_url") String documentationUrl) {
        this.message = message;
        this.documentationUrl = documentationUrl;
    }
}
