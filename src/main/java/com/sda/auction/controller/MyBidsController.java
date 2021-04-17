package com.sda.auction.controller;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.service.ProductService;
import com.sda.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MyBidsController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public MyBidsController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/myBids")
    public String getMyBids(Model model, Authentication authentication){
        List<ProductDto> productDtoList = productService.getProductDtoListByBidder(authentication.getName());
        model.addAttribute("productDtoList", productDtoList);
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);
        return "myBids";
    }
}
