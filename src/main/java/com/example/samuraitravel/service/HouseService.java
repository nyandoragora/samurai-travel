package com.example.samuraitravel.service;

import java.util.Optional;

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
	
//	すべての民宿をページング状態で取得
	public Page<House> findAllHouses(Pageable pageable){
		return houseRepository.findAll(pageable);
	}
	
//	検索ボックスに入力したキーワードと部分一致する民宿をページング状態で取得
	public Page<House> findHousesByNameLike(String keyword , Pageable pageable){
		return houseRepository.findByNameLike("%" + keyword + "%", pageable);
	}
	
	
//	指定したIDを持つ民宿を取得
	public Optional<House> findHouseById(Integer id){
		return houseRepository.findById(id);
	}
	
}
