package com.sparta.msa_exam.order;

import com.sparta.msa_exam.order.dto.OrderReqPostDto;
import com.sparta.msa_exam.order.dto.OrderReqPutDto;
import com.sparta.msa_exam.order.dto.OrderResDto;
import com.sparta.msa_exam.order.dto.OrderResGetDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Value("${server.port}")
    private int serverPort;

    @PostMapping
    public ResponseEntity<OrderResDto> createOrder(@RequestBody OrderReqPostDto requestDto) {
        OrderResDto createdOrder= orderService.createOrder(requestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server_Port", String.valueOf(serverPort));
        return ResponseEntity.ok().headers(headers).body(createdOrder);
    }

    @PutMapping("/{order_Id}")
    public ResponseEntity<OrderResDto> updateOrder(@RequestBody OrderReqPutDto requestDto, @PathVariable Long order_Id) {
        OrderResDto updatedOrder = orderService.updateOrder(requestDto, order_Id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server_Port", String.valueOf(serverPort));
        return ResponseEntity.ok().headers(headers).body(updatedOrder);
    }


    @GetMapping("/{order_Id}")
    public ResponseEntity<OrderResGetDto> readOrderByOrderId(@PathVariable Long order_Id) {
        OrderResGetDto readOrder = orderService.readOrderByOrderId(order_Id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server_Port", String.valueOf(serverPort));
        return ResponseEntity.ok().headers(headers).body(readOrder);
    }
}
