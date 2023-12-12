package com.spring.order.controller;

import com.spring.order.serv.OrderServ;
import com.spring.order.vo.OrderRequest;
import com.spring.order.vo.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/api/order")
@RestController
@Slf4j
public class OrderController {

    @Autowired
    OrderServ orderServ;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> saveOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(() -> orderServ.saveOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        runtimeException.printStackTrace();
        return CompletableFuture.supplyAsync(() -> "Server is down. Please try again later");
    }

    public String fallbackMethodForTimeout(OrderRequest orderRequest, RuntimeException runtimeException){
        return "";
    }

//    public List<OrderResponse> getAllorders(){
//
//    }


}
