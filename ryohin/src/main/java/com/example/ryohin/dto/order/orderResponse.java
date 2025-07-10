package com.example.ryohin.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class orderResponse {
    private Integer order_id;
    private LocalDateTime order_date;
}