package com.spring.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_order_line_items")
@Entity
@Builder
public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String skuCode;

    private BigDecimal price;

    private Integer quantity;
}
