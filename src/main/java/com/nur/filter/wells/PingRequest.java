package com.nur.filter.wells;

import lombok.Data;

@Data
public class PingRequest {
    private String clientId;
    private String token;
    private String url;
}
