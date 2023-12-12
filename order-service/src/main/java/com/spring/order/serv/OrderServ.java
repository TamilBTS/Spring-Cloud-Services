package com.spring.order.serv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.order.entity.Order;
import com.spring.order.entity.OrderLineItems;
import com.spring.order.event.OrderBasedEvent;
import com.spring.order.repo.Orderrepo;
import com.spring.order.vo.InventoryResponse;
import com.spring.order.vo.OrderRequest;
import com.spring.order.vo.OrderResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderServ {

    @Autowired
    private Orderrepo orderrepo;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    io.micrometer.tracing.Tracer tracer;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public String saveOrder(OrderRequest orderRequest) {
        log.info("Save order Method Called");
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDto().stream()
                .map(str -> OrderLineItems.builder()
                        .price(str.getPrice())
                        .quantity(str.getQuantity())
                        .skuCode(str.getSkuCode())
                        .build()).collect(Collectors.toList());
        Order order = Order.builder()
                .orderNo(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .build();

        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //Create a custom span for tracing
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){
            InventoryResponse[] result = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory", uriBuilder ->
                            uriBuilder.queryParam("sku-code", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            log.info("API has been called and recieved a response");
            Boolean allProductsIsInStock= Arrays.stream(result).allMatch(InventoryResponse::getIsInStock);
            log.info("Result was " + allProductsIsInStock);

            if(allProductsIsInStock){
                order = orderrepo.save(order);
                kafkaTemplate.send("notificationTopicSuccess", order.getOrderNo());
            }
            else
                throw new IllegalArgumentException("Product not in stock. Please try again later");

        } finally {
            inventoryServiceLookup.end();
        }


        return order.getOrderNo();
    }

//    public List<OrderResponse> getALlOrders(){
//        List<Order> orders = orderrepo.findAll();
//        orders.stream().map(str -> OrderResponse.builder()
//                .orderId(str.getId())
//                .orderLineItems(mapOrderLineI))
//    }
}
