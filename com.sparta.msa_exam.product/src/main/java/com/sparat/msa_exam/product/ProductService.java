package com.sparat.msa_exam.product;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRepository.save(new Product(requestDto));
        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> readProduct() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::new)  // 각 Product를 ProductResponseDto로 변환
                .collect(Collectors.toList());
    }

    public ProductResponseDto readProductById(long product_Id) {
        Product product = productRepository.findById(product_Id).orElseThrow(() ->
                new IllegalArgumentException("해당 상품은 존재하지 않습니다. productId = " + product_Id)
        );
        return new ProductResponseDto(product);
    }
}
