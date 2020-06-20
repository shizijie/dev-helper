package com.shizijie.dev.helper.web.test;

import lombok.Data;

/**
 * @author shizijie
 * @version 2020-05-27 下午3:09
 */
@Data
public class Car {
    private double price;
    private String colour;
    public Car(double price, String colour){
        this.price = price;
        this.colour = colour;
    }
}
