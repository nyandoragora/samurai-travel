package com.example.samuraitravel.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite , Integer>{
	
//	特定のユーザーに対するお気に入りを取得し、ページング可能な形式で出力
	public Page<Favorite> findByUserOrderByCreatedAtDesc(User user , Pageable pageable);
	
//	特定の民宿とユーザーに紐づくお気に入りを取得し、該当Entityを出力
	public Optional<Favorite> findByHouseAndUser(House house , User user);

}
