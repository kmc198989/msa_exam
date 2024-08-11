package com.sparta.msa_exam.order.dto;


import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderedProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
public class OrderResDto implements Serializable {
    private Long order_Id;  // 필드 이름은 카멜케이스로 일관성 있게 사용
    private String name;
    private List<Long> product_Ids;  // 제품 ID 리스트를 포함

    // 생성자에서 Order 엔티티를 받아서 필요한 필드를 초기화
    public OrderResDto(Order order) {
        this.order_Id = order.getOrder_Id();
        this.name = order.getName();
        this.product_Ids = order.getProduct_Ids().stream()
                .map(OrderedProduct::getProduct_Id)  // 각 OrderedProduct의 productId를 리스트에 추가
                .collect(Collectors.toList());
    }
}
