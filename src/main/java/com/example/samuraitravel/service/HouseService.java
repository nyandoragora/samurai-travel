package com.example.samuraitravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseRegisterForm;
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
	
//	民宿のレコード数を取得
	public long countHouses() {
		return houseRepository.count();
	}
	
//	idが最も大きい民宿を取得する
	public House findFirstHouseByOrderByIdDesc() {
		return houseRepository.findFirstByOrderByIdDesc();
	}
	
	
	
	@Transactional
	public void createHouse(HouseRegisterForm houseRegisterForm) {
		
		House house = new House();
		MultipartFile imageFile = houseRegisterForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFIleName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage" + hashedImageName);
			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}
		
        house.setName(houseRegisterForm.getName());
        house.setDescription(houseRegisterForm.getDescription());
        house.setPrice(houseRegisterForm.getPrice());
        house.setCapacity(houseRegisterForm.getCapacity());
        house.setPostalCode(houseRegisterForm.getPostalCode());
        house.setAddress(houseRegisterForm.getAddress());
        house.setPhoneNumber(houseRegisterForm.getPhoneNumber());

        houseRepository.save(house);
		
	}
	
//	UUIDを用いて生成したファイル名を返す
	
	public String generateNewFIleName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		
		for (int i = 0; i < fileNames.length-1; i++) {
			fileNames[i] = UUID.randomUUID().toString();	
		}
		
		String hashedFileName = String.join(".",fileNames);
		
		return hashedFileName;
	}
	
//	画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile , Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
