package com.sparat.msa_exam.product;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String name;

    private Integer supply_price;

    public Product (ProductRequestDto requestDto) {
        this.name = requestDto.getName();
        this.supply_price = requestDto.getSupply_price();
    }
}
