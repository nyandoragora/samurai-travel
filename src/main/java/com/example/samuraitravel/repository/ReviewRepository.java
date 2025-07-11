package com.example.samuraitravel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;

public interface ReviewRepository extends JpaRepository<Review , Integer>{

	public List<Review> findTop6ByHouseOrderByCreatedAtDesc(House house);
	public Optional<Review> findByHouseAndUser(House house , User user);
	public Long countByHouse(House house);
	public Page<Review> findByHouseOrderByCreatedAtDesc(House house , Pageable pageable);
	
}
