package com.sda.auction.service;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.mapper.ProductMapper;
import com.sda.auction.model.Product;
import com.sda.auction.model.User;
import com.sda.auction.repository.ProductRepository;
import com.sda.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userRepository = userRepository;
    }

    public void addProduct(ProductDto productDto, String loggedUserEmail, MultipartFile multipartFile) {
        Product product = productMapper.map(productDto, multipartFile);
        assignSeller(loggedUserEmail, product);
        productRepository.save(product);
    }

    public List<ProductDto> getProductDtoList() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = productMapper.map(productList);
        return productDtoList;
    }

    public Optional<ProductDto> getProductDtoBy(String productId) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.parseInt(productId));
        if (!optionalProduct.isPresent()) {
            return Optional.empty();
        }
        ProductDto productDto = productMapper.map(optionalProduct.get());
        return Optional.of(productDto);
    }

    private void assignSeller(String loggedUserEmail, Product product) {
        Optional<User> optionalUser = userRepository.findByEmail(loggedUserEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            product.setSeller(user);
        }
    }
}
