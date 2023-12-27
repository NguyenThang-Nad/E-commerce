package com.javadev.shopaap.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javadev.shopaap.entity.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse extends BaseResponse{
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("price")
    private Long price;
    @JsonProperty("number_of_products")
    private int numberOfProducts;
    @JsonProperty("total_money")
    private Long totalMoney;
    private String color;
    public static OrderResponse fromOrderDetail(OrderDetailEntity orderDetail) {
        OrderResponse orderResponse =new OrderResponse();
        orderResponse.setOrderId(orderDetail.getId());
        orderResponse.setId(orderDetail.getOrder().getId());
        orderResponse.setProductId(orderDetail.getProduct().getId());
        orderResponse.setPrice(orderDetail.getPrice());
        orderResponse.setNumberOfProducts(orderDetail.getNumberOfProducts());
        orderResponse.setTotalMoney(orderDetail.getTotalMoney());
        orderResponse.setColor(orderDetail.getColor());
        return orderResponse;
    }

}
