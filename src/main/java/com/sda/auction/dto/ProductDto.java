package com.sda.auction.dto;

import lombok.Data;

@Data
public class ProductDto {

    private String id;
    private String name;
    private String description;
    private String category;
    private String startingPrice;
    private String currentPrice;
    private String minimumBidStep;
    private String userMaximumBid;
    private String startBiddingTime;
    private String endBiddingTime;
    private String base64Image;
    private boolean winnerAssigned;
}
