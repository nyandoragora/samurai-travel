package com.example.samuraitravel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	
//	指定したIDを持つレビューを表示する
	public Optional<Review> findReviewById(Integer id) {
		return reviewRepository.findById(id);
	}
	
//	指定した民宿のレビューを作成日時が新しい順に6件取得する
	public List<Review> findTop6ReviewsByHouseOrderByCreatedAtDesc(House house){
		return reviewRepository.findTop6ByHouseOrderByCreatedAtDesc(house);
	}
	
//	指定した民宿とユーザーのレビューを取得する
	public Optional<Review>findByHouseAndUser(House house, User user) {
		return reviewRepository.findByHouseAndUser(house, user);
	}
	
//	指定した民宿のレビュー件数を取得する
	public Long countReviewsByHouse(House house) {
		return reviewRepository.countByHouse(house);
	}
	
//	指定した民宿のすべてのレビューを作成日時が新しい順に並び替え、ページングされた状態で取得する。
	public Page<Review> findReviewsByHouseOrderByCreatedAtDesc(House house , Pageable pageable){
		return reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);
	}
	
//	レビュー投稿ページ用のフォームクラスからのデータをもとに、新しいレビューを登録する。
	@Transactional
	public void createReview(ReviewRegisterForm reviewRegisterForm , House house , User user) {
		
		Review review = new Review();
		
		review.setScore(reviewRegisterForm.getScore());
		review.setContent(reviewRegisterForm.getContent());
		review.setHouse(house);
		review.setUser(user);
		
		reviewRepository.save(review);
	}
	
//	レビュー編集用のフォームクラスからのデータを下に、既存のレビューを更新する。
	@Transactional
	public void updateReview(ReviewEditForm reviewEditForm , Review review) {
		
		review.setScore(reviewEditForm.getScore());
		review.setContent(reviewEditForm.getContent());
		
		reviewRepository.save(review);
		
	}
	
//	レビューを削除する
	@Transactional
	public void deleteReview(Review review) {
		reviewRepository.delete(review);
	}
	
//	指定したユーザーが指定した民宿のレビューをすでに投稿済みかチェック
	public boolean hasUserAlreadyReviewed(House house , User user) {
		return reviewRepository.findByHouseAndUser(house, user).isPresent();
	}
	
}
