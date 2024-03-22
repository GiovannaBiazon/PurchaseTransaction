package com.brief.transaction.dto;

public class FiscalApiMetaDTO {
    private Integer count;
    private FiscalApiMetaLabelsDTO labels;

    FiscalApiMetaDTO() {}

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public FiscalApiMetaLabelsDTO getLabels() {
        return labels;
    }
    public void setLabels(FiscalApiMetaLabelsDTO labels) {
        this.labels = labels;
    }
}
