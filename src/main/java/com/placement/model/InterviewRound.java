package com.placement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRound {
    private Long id;
    private Long applicationId;
    private String roundName;
    private LocalDateTime scheduledAt;
    private String result;
    private String remarks;
}
