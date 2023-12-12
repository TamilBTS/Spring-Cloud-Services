package com.spring.order.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderRequest {
    @JsonProperty("items")
    private List<OrderLineItemsDto> orderLineItemsDto;
}
