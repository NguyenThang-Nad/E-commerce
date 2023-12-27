package com.javadev.shopaap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    @Size(min =1,message = "Product Id must be > 0")
    @JsonProperty("product_id")
    private Long productId;
    @Size(min =5,max = 200,message = "Image name > 0")
    @JsonProperty("image_url")
    private String imageUrl;
}
