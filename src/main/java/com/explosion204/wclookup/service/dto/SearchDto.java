package com.explosion204.wclookup.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchDto {
    private Double latitude;
    private Double longitude;
    private Integer radius;

    public boolean hasNoNullAttributes() {
        return latitude != null && longitude != null && radius != null;
    }
}
