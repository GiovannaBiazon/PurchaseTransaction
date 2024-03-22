package com.brief.transaction.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiscalApiResponseDTO {

    private ArrayList<FiscalApiDataDTO> data;
    private FiscalApiMetaDTO meta;
    private FiscalApiDataDTO dataTypes;
    private FiscalApiDataDTO dataFormats;
    @JsonAlias("total-count")
    private Integer totalCount;
    @JsonAlias("total-pages")
    private Integer totalPages;
    private FiscalApiLinksDTO links;
}
