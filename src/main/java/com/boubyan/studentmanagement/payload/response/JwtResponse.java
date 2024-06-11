package com.boubyan.studentmanagement.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {
    @JsonProperty("user-id")
    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
