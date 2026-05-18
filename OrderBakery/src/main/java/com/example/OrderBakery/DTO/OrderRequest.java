package com.example.OrderBakery.DTO;


import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequest {
    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, message = "Имя должно содержать минимум 2 символа")
    private String customerName;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(
            regexp = "^(\\+7\\d{10}|8\\d{10})$",
            message = "Телефон должен быть в формате +7XXXXXXXXXX или 8XXXXXXXXXX"
    )
    private String customerPhone;

    @NotBlank(message = "Способ доставки обязателен")
    private String deliveryMethod;

    private String deliveryAddress;

    @NotNull(message = "Дата доставки обязательна")
    @FutureOrPresent(message = "Дата доставки не может быть в прошлом")
    private LocalDate deliveryDate;

    private String orderComment;

    @NotEmpty(message = "Корзина не должна быть пустой")
    @Valid
    private List<OrderItemRequest> items;
}
