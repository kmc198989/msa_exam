package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.dto.OrderReqPostDto;
import com.sparta.msa_exam.order.dto.OrderReqPutDto;
import com.sparta.msa_exam.order.dto.OrderResDto;
import com.sparta.msa_exam.order.dto.OrderResGetDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderedProduct;
import com.sparta.msa_exam.order.repository.OrderRepository;
import feign.FeignException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public String getProductInfo(Long product_Id) {
        try {
            return productClient.getProduct(product_Id);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("해당 상품은 존재하지 않습니다. id=" + product_Id);
        } catch (FeignException e) {
            throw new IllegalStateException("상품 서비스를 호출하는 도중 오류가 발생했습니다.");
        }
    }


    public OrderResDto createOrder(OrderReqPostDto requestDto) {
        // 새로운 Order 객체 생성
        Order order = new Order();
        order.setName(requestDto.getName());

        // OrderedProduct 리스트 생성
        List<OrderedProduct> orderedProducts = new ArrayList<>();
        for (Long product_id : requestDto.getProduct_ids()) {
            if (getProductInfo(product_id) == null) break;
            OrderedProduct newOrderedProduct = new OrderedProduct();
            newOrderedProduct.setOrder(order);
            newOrderedProduct.setProduct_Id(product_id); // Long 타입의 product_id 설정
            orderedProducts.add(newOrderedProduct);
        }

        // Order 객체에 OrderedProduct 리스트 설정
        order.setProduct_Ids(orderedProducts);

        // Order를 저장하면 연관된 OrderedProduct들도 자동으로 저장됨
        orderRepository.save(order);

        // 생성된 주문을 바탕으로 OrderResDto 반환
        return new OrderResDto(order);
    }

    @CachePut(cacheNames = "updateOrder", key = "#order_Id")
    @CacheEvict(cacheNames = "orderByOrderId", key = "#order_Id")
    public OrderResDto updateOrder(OrderReqPutDto requestDto, Long order_Id) {
        // 1. orderId에 해당하는 주문을 조회합니다.
        Order order = orderRepository.findById(order_Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 없습니다. id=" + order_Id));
        Long product_id = requestDto.getProduct_id();
        String productInfo = getProductInfo(product_id);
        if (productInfo == null) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다. product_id=" + product_id);
        }
        // 2. 새로운 OrderedProduct 객체를 생성하고, 요청으로 받은 product_id를 설정합니다.
        OrderedProduct newOrderedProduct = new OrderedProduct();
        newOrderedProduct.setOrder(order);
        newOrderedProduct.setProduct_Id(product_id);

        // 3. 주문에 새로운 상품을 추가합니다.
        order.getProduct_Ids().add(newOrderedProduct);

        // 4. 주문을 저장합니다.
        orderRepository.save(order);

        return new OrderResDto(order);
    }

    @Cacheable(cacheNames = "orderByOrderId")
    public OrderResGetDto readOrderByOrderId(Long order_Id) {
        Order order = orderRepository.findById(order_Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 없습니다. id=" + order_Id));
        return new OrderResGetDto(order);
    }
}