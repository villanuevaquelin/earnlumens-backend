package com.api.store.domain.dto.response;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WaitlistStatsResponse {

    private Map<String, Long> stats;

    public WaitlistStatsResponse(Map<String, Long> stats) {
        this.stats = stats;
    }

    @JsonProperty("stats")
    public Map<String, Long> getStats() {
        return stats;
    }

    public void setStats(Map<String, Long> stats) {
        this.stats = stats;
    }
}