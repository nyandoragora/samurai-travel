package com.example.samuraitravel.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.HouseService;
import com.example.samuraitravel.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewService reviewService;
	private final HouseService houseService;
	
	@GetMapping("/{id}")
	public String index(@PathVariable(name = "id") Integer houseId ,
						@PageableDefault (page = 0 , size = 10 , sort = "createdAt" , direction = Direction.ASC) Pageable pageable ,
						@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						RedirectAttributes redirectAttributes ,
						Model model) {
		
		Optional<House> optionalHouse = houseService.findHouseById(houseId);
		
		if(optionalHouse.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage" , "民宿が存在しません。");
			
			return "redirect:/houses";
		}
		
		House house = optionalHouse.get();
		User user = null;
	    if(userDetailsImpl != null) {
	        user = userDetailsImpl.getUser();
	    }
		
		Page<Review> reviewPage = reviewService.findReviewsByHouseOrderByCreatedAtDesc(house, pageable);
		
		model.addAttribute("reviewPage" , reviewPage);
		model.addAttribute("house" , house);
		model.addAttribute("user" , user);

		return "reviews/index";
		
	}
	
	@GetMapping("/register/{id}")
	public String register(@PathVariable(name = "id") Integer houseId , RedirectAttributes redirectAttributes , Model model) {
		
		Optional<House> optionalHouse = houseService.findHouseById(houseId);
		
		if(optionalHouse.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage" , "民宿が存在しません。");
			
			return "redirect:/houses";
		}
		
		House house = optionalHouse.get();
		
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		model.addAttribute("house" , house);
		
		return "reviews/register";
	}
	
	@PostMapping("/create/{id}")
	public String create(@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,
						 BindingResult bindingResult,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 @PathVariable(name = "id") Integer houseId,
						 RedirectAttributes redirectAttributes,
						 Model model) {
		
		User user = userDetailsImpl.getUser();
		Optional<House> optionalHouse = houseService.findHouseById(houseId);
		
		if(optionalHouse.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage" , "民宿が存在しません。");
			
			return "redirect:/houses";
		}
		
		House house = optionalHouse.get();
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("house" , house);
			model.addAttribute("reviewRegisterForm" , reviewRegisterForm);
			model.addAttribute("errorMessage" , "入力内容に不備があります。");
			
			return "reviews/register";
			
		}
		
		reviewService.createReview(reviewRegisterForm, house, user);
		redirectAttributes.addFlashAttribute("successMessage" , "レビューを投稿しました。");
		
		
		return "redirect:/houses/" + house.getId();
		
	}
	
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(name = "id") Integer reviewId,
					   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
					   RedirectAttributes redirectAttributes , Model model) {
		
		Optional<Review> optionalReview = reviewService.findReviewById(reviewId);
		
		if(optionalReview.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage" , "レビューが存在しません。");
			
			return "redirect:/houses";
		}
				
		Review review = optionalReview.get();
		

	    User loginUser = userDetailsImpl.getUser();
	    if (!review.getUser().getId().equals(loginUser.getId())) {
	        redirectAttributes.addFlashAttribute("errorMessage", "このレビューは編集できません。");
	        return "redirect:/houses";
	    }
		
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getScore(), review.getContent());
		
		model.addAttribute("review" , review);
		model.addAttribute("reviewEditForm" , reviewEditForm);
		
		return "reviews/edit";
		
	}
	
	@PostMapping("{id}/update")
	public String update(@PathVariable Integer id,
						 @ModelAttribute @Validated  ReviewEditForm reviewEditForm,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 Model model) {

		Optional<Review> optionalReview = reviewService.findReviewById(id);
		
		if (optionalReview.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "レビューが存在しません。");
			return "redirect:/houses";
		}

		Review review = optionalReview.get();

		if (bindingResult.hasErrors()) {
			
			model.addAttribute("review", review);
			model.addAttribute("reviewEditForm" , reviewEditForm);
			model.addAttribute("errorMessage" , "入力内容に不備があります。");
			
			return "reviews/edit";
		}

		
		review.setScore(reviewEditForm.getScore());
		review.setContent(reviewEditForm.getContent());
		reviewService.updateReview(reviewEditForm, review);

		redirectAttributes.addFlashAttribute("successMessage", "レビューを更新しました。");
		
		return "redirect:/houses/" + review.getHouse().getId();
}
	
	@PostMapping("{id}/delete")
	public String delete(@PathVariable(name="id") Integer reviewId, RedirectAttributes redirectAttributes) {
		
		Optional<Review> optionalReview = reviewService.findReviewById(reviewId);
		
		if (optionalReview.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "レビューが存在しません。");
			return "redirect:/houses";
		}
		
		Review review = optionalReview.get();
		reviewService.deleteReview(review);
		
		redirectAttributes.addFlashAttribute("successMessage" , "レビューを削除しました。");
		
		return "redirect:/houses/" + review.getHouse().getId();
		
	}
	
	
	
}
