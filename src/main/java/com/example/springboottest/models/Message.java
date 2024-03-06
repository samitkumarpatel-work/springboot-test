package com.example.springboottest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Message(
        String message,
        @JsonProperty("isActive") boolean isPublic
        ) {}
