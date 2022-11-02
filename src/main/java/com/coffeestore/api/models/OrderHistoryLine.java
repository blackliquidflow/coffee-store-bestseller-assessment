package com.coffeestore.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "orders_history")
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long orderLineId;
    private LocalDateTime orderDateTime;
    private Long beverageId;
    private String ingredientType;
    private Long ingredientId;
    private String ingredientName;
    private Double price;
}
