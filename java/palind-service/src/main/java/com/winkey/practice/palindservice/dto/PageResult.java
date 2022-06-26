package com.winkey.practice.palindservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Venkatesh Rajendran
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResult {
    private List<String> dataList;
    private long count;
    private String error;
    private ErrorID errorID;

    public PageResult(List<String> dataList, long count) {
        this.dataList = dataList;
        this.count = count;
    }
}
