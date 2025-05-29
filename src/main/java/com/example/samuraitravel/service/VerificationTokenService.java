package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.entity.VerificationToken;
import com.example.samuraitravel.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
	private final VerificationTokenRepository verificationtokenRepository;
	
	@Transactional
	public void create(User user , String token) {
		
		VerificationToken verificationToken = new VerificationToken();
		
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		
		verificationtokenRepository.save(verificationToken);
		
	}
//	トークンの文字列で検索した結果を返すメソッド
	public VerificationToken getVerificationToken(String token) {
		return verificationtokenRepository.findByToken(token);
	}

}
