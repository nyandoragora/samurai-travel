package com.example.samuraitravel.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.HouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseService {

	private final HouseRepository houseRepository;
	
	public Page<House> findAllHouses(Pageable pageable){
		return houseRepository.findAll(pageable);
	}
	
	public Page<House> findHousesByNameLike(String keyword , Pageable pageable){
		return houseRepository.findByNameLike("%" + keyword + "%", pageable);
	}
	
}
