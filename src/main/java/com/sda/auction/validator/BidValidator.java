package com.sda.auction.validator;

import com.sda.auction.dto.BidDto;
import com.sda.auction.model.Bid;
import com.sda.auction.model.Product;
import com.sda.auction.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Comparator;
import java.util.Optional;

@Service
public class BidValidator {

    private ProductRepository productRepository;

    @Autowired
    public BidValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // TODO---> validations
    public void validate(String productId, BidDto bidDto, BindingResult bindingResult) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
        if (!optionalProduct.isPresent()) {
            bindingResult.addError(new FieldError("bidDto", "value", "Invalid product id"));
            return;
        }
        validateBidValue(bidDto, bindingResult, optionalProduct.get());
    }

    private void validateBidValue(BidDto bidDto, BindingResult bindingResult, Product product) {
        Optional<Bid> optionalMaxBid = getMaxBid(product);
        int productCurrentPrice = product.getStartingPrice();
        int bidDtoValue = Integer.parseInt(bidDto.getValue());
        boolean isError = false;
        String errorMessage = null;
        if (optionalMaxBid.isPresent()) {
            productCurrentPrice = optionalMaxBid.get().getValue();
            if (bidDtoValue <= productCurrentPrice) {
                isError = true;
                errorMessage = "Value is smaller than the last bid";
            }
        } else {
            if (bidDtoValue < productCurrentPrice) {
                isError = true;
                errorMessage = "Value is smaller than the starting price";
            }
        }
        if (isError) {
            bindingResult.addError(new FieldError("bidDto", "value", errorMessage));
        }
    }

    private Optional<Bid> getMaxBid(Product product) {
        Optional<Bid> optionalMaxBid = product.getBidList()
                .stream()
                .max(Comparator.comparing(Bid::getValue));
        return optionalMaxBid;
    }
}
