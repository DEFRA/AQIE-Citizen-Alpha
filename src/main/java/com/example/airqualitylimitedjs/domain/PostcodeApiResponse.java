package com.example.airqualitylimitedjs.domain;

import lombok.Data;

@Data
public class PostcodeApiResponse {
    private Integer status;
    private PostcodeDetails result;
}
