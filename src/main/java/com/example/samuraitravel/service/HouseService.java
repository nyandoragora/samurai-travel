package com.example.samuraitravel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.HouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseService {

	private final HouseRepository houseRepository;
	
	public List<House> findAllHouses(){
		return houseRepository.findAll();
	}
	
}
