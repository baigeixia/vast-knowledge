package com.vk.analyze.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ChartDateTime {
    private LocalDate dateTime;
    private Long rowData;
}
