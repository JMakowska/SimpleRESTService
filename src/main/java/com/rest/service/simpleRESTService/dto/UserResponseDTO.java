package com.rest.service.simpleRESTService.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDTO {
    private Integer id;
    private String login;
    private String name;
    private String type;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private Integer followers;
    private Integer publicRepos;

    @JsonCreator
    public UserResponseDTO(@JsonProperty("id") Integer id,
                           @JsonProperty("login") String login,
                           @JsonProperty("name") String name,
                           @JsonSetter("type") String type,
                           @JsonProperty("avatar_url") String avatarUrl,
                           @JsonProperty("created_at") LocalDateTime createdAt,
                           @JsonProperty("followers") Integer followers,
                           @JsonProperty("public_repos") Integer publicRepos) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.type = type;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.followers = followers;
        this.publicRepos = publicRepos;
    }
}
