package com.rest.service.simpleRESTService.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserCounter {
    @Id
    private String login;
    private Integer requestCount;
}



