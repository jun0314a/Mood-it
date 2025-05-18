package com.example.group.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateRequest {
    private String title;
    private String description;
    private String tags;
    private String emotion;
}