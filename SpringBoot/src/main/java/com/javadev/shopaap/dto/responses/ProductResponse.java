package com.javadev.shopaap.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javadev.shopaap.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse fromProduct(ProductEntity product) {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setThumbnail(product.getThumbnail());
        productResponse.setDescription(product.getDescription());
        if (product.getCategoryId() != null) {
            productResponse.setCategoryId(product.getCategoryId().getId());
        }

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }
}
