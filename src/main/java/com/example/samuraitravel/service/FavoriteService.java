package com.example.samuraitravel.service;

import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	
//	指定したIDを持つお気に入りを取得、存在しなければエラー
	public Favorite findFavoriteById(Integer id) {
	    return favoriteRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("お気に入りが見つかりません (id: " + id + ")"));
	}
//	指定した民宿とユーザーにマッチするお気に入りを取得
	public Optional<Favorite> findFavoriteByHouseAndUser(House house , User user){
		return favoriteRepository.findByHouseAndUser(house, user);
	}
	
//	指定したユーザーの全お気に入りを取得し作成日時順に並び替え、ページング状態で取得
	public Page<Favorite> findFavoriteByUserOrderByCreatedAtDesc(User user , Pageable pageable){
		if (user == null) {
	        return Page.empty(); // nullを返さない
	    }
		return favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
	}
	
//	指定した民宿とユーザーにマッチするお気に入りをテーブルに追加する
	
	@Transactional
	public void createFavorite(House house , User user) {
		
		Favorite favorite = new Favorite();
		
		favorite.setHouse(house);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
		
	}
	
//	指定したお気に入りを削除する
	
	@Transactional
	public void deleteFavorite(Favorite favorite) {
		favoriteRepository.delete(favorite);
	}
	
//	指定したユーザーがすでに該当民宿をお気に入り登録済みかチェックする
	public boolean isFavorite(House house , User user) {
		return favoriteRepository.findByHouseAndUser(house, user).isPresent();
	}
}
