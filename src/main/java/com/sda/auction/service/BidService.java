package com.sda.auction.service;

import com.sda.auction.dto.BidDto;
import com.sda.auction.mapper.BidMapper;
import com.sda.auction.model.Bid;
import com.sda.auction.model.Product;
import com.sda.auction.model.User;
import com.sda.auction.repository.BidRepository;
import com.sda.auction.repository.ProductRepository;
import com.sda.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BidService {

    private BidRepository bidRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private BidMapper bidMapper;

    @Autowired
    public BidService(BidRepository bidRepository, ProductRepository productRepository,
                      UserRepository userRepository, BidMapper bidMapper) {
        this.bidRepository = bidRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.bidMapper = bidMapper;
    }

    public void placeBid(BidDto bidDto, String productId, String userEmail) {
        Product product = getProduct(productId);
        User user = getUser(userEmail);
        Bid bid = bidMapper.map(bidDto, product, user);
        bidRepository.save(bid);
    }

    private User getUser(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User Email Invalid!");
        }
        return optionalUser.get();
    }

    private Product getProduct(String productId) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
        if (!optionalProduct.isPresent()) {
            throw new IllegalArgumentException("Invalid product id!");
        }
        return optionalProduct.get();
    }
}
