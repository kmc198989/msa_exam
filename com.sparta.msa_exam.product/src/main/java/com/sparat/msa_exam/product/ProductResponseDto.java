package com.sparat.msa_exam.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long product_id;
    private String name;
    private Integer supply_price;

    public ProductResponseDto(Product product) {
        this.product_id = product.getProduct_id();
        this.name = product.getName();
        this.supply_price = product.getSupply_price();
    }
}
