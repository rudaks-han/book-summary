package com.example.designpattern.adapter;

/**
 * Customer 인터페이스를 필요로 하는 클라이언트 코드
 */
public class BusinessCardDesigner {

    public String designCard(Customer customer) {
        String card = "";
        card += customer.getName();
        card += "\n" + customer.getDesignation();
        card += "\n" + customer.getAddress();
        return card;
    }
}
