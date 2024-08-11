package com.sparat.msa_exam.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Value("${server.port}")
    private int serverPort;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.createProduct(requestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", String.valueOf(serverPort));
        return ResponseEntity.ok().headers(headers).body(productResponseDto);
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> readProduct() {
        List<ProductResponseDto> listProduct = productService.readProduct();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", String.valueOf(serverPort));
        return ResponseEntity.ok().headers(headers).body(listProduct);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<ProductResponseDto> readProductById(@PathVariable("product_id") long product_id) {
        return ResponseEntity.ok(productService.readProductById(product_id));
    }

}

