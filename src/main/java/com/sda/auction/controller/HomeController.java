package com.sda.auction.controller;

import com.sda.auction.dto.BidDto;
import com.sda.auction.dto.ProductDto;
import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.service.BidService;
import com.sda.auction.service.ProductService;
import com.sda.auction.service.UserService;
import com.sda.auction.validator.BidValidator;
import com.sda.auction.validator.GenericValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class HomeController {

    private final ProductService productService;
    private GenericValidator genericValidator;
    private BidValidator bidValidator;
    private BidService bidService;
    private final UserService userService;

    @Autowired
    public HomeController(ProductService productService, GenericValidator genericValidator,
                          BidValidator bidValidator, BidService bidService, UserService userService) {
        this.productService = productService;
        this.genericValidator = genericValidator;
        this.bidValidator = bidValidator;
        this.bidService = bidService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String getHomepage(Model model, Authentication authentication) {
        log.info("getHomePage called");
        List<ProductDto> productDtoList = productService.getActiveProductDtoList(authentication.getName());
        model.addAttribute("productDtoList", productDtoList);
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);
        return "home";
    }

    @GetMapping("/viewProduct/{productId}")
    public String getViewProduct(Model model, @PathVariable(value = "productId") String productId, Authentication authentication) {
        if (genericValidator.isNotPositiveInteger(productId)) {
            return "redirect:/home";
        }
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId, authentication.getName());
        if (!optionalProductDto.isPresent()) {
            return "redirect:/home";
        }
        ProductDto productDto = optionalProductDto.get();
        model.addAttribute("product", productDto);
        model.addAttribute("bidDto", new BidDto());
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);
        return "viewProduct";
    }

    @PostMapping("/viewProduct/{productId}")
    public String postBid(Model model, @PathVariable(value = "productId") String productId,
                          BidDto bidDto, BindingResult bindingResult, Authentication authentication) {
        String loggedUserEmail = authentication.getName();
        bidValidator.validate(productId, bidDto, bindingResult);
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId, authentication.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("bidDto", bidDto);
            model.addAttribute("product", optionalProductDto.get());
            return "viewProduct";
        }
        bidService.placeBid(bidDto, productId, loggedUserEmail);
        return "redirect:/home";
    }
}
