package com.example.solid.isp.after;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order extends Entity {

    private LocalDateTime orderPlacedOn;

    private double totalValue;
}
