package com.example.samuraitravel.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;
import com.example.samuraitravel.service.HouseService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;
	private final HouseService houseService;
	
//	指定されたユーザーのお気に入りをページング状態で取得、お気に入り一覧ページに受け渡す
	
	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsimpl,
						@PageableDefault(page = 0 , size = 10 , sort = "id" , direction = Direction.ASC)Pageable pageable,
						Model model) {
		
		User user = userDetailsimpl.getUser();
		
		Page<Favorite> favoritePages = favoriteService.findFavoriteByUserOrderByCreatedAtDesc(user, pageable);
		
		model.addAttribute("favoritePages" , favoritePages);
		model.addAttribute("user" , user);
		
		return "favorites/index";
		
	}
	
	
	
//	新しいお気に入りを追加する。その後該当IDの民宿詳細ページにリダイレクト。
	
	@PostMapping("/houses/{id}/createFavorite")
	public String create(@PathVariable(name = "id")Integer id,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 RedirectAttributes redirectAttributes) {
		
		User user = userDetailsImpl.getUser();
		Optional<House> optionalHouse = houseService.findHouseById(id);
		if(optionalHouse.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage" , "民宿が存在しません。");
			return "redirect:/houses";
		}
		
		House house = optionalHouse.get();
		
		favoriteService.createFavorite(house, user);
		redirectAttributes.addFlashAttribute("successMessage" , "お気に入りに追加しました。");
		
		return "redirect:/houses/" + id;
		
	}
	
	
	
//	指定されたお気に入りを削除する。その後は該当IDの民宿詳細ページにリダイレクト。
	
	@PostMapping("/houses/{id}/deleteFavorite")
	public String delete(@PathVariable(name = "id") Integer id,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 RedirectAttributes redirectAttributes) {
		
		Optional<House> optionalHouse = houseService.findHouseById(id);
		
		if(optionalHouse.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage" , "民宿が存在しません。");
			
			return "redirect:/houses";
		}
		
		House house = optionalHouse.get();
		User user = userDetailsImpl.getUser();
		
		Optional<Favorite> optionalFavorite = favoriteService.findFavoriteByHouseAndUser(house, user);
		
		if(optionalFavorite.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage" , "お気に入り民宿が存在しません");
			return "redirect:/houses";
		}
		
		Favorite favorite = optionalFavorite.get();
		
		favoriteService.deleteFavorite(favorite);
		redirectAttributes.addFlashAttribute("successMessage" , "お気に入りを解除しました。");
		
		return "redirect:/houses/" + id;
		
		
	}
}
