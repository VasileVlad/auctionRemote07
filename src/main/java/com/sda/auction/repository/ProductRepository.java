package com.sda.auction.repository;

import com.sda.auction.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where :now between p.startBiddingTime and p.endBiddingTime")
    List<Product> findAllActive(@Param("now") LocalDateTime now);

    @Query("select distinct b.product from Bid b where :authenticatedUserEmail = b.user.email")
    List<Product> findAllByBidder(@Param("authenticatedUserEmail") String authenticatedUserEmail);

    @Query("select p from Product p where :now > p.endBiddingTime and p.winner is null")
    List<Product> findAllExpiredAndUnassigned(@Param("now") LocalDateTime now);
}
