package com.javadev.shopaap.dto.responses;

import lombok.*;

import java.util.List;
    @AllArgsConstructor
    @Data
    @Builder
    @NoArgsConstructor
    public class ProductListResponse {
        private List<ProductResponse> products;
        private int totalPages;
    }

