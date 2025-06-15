package com.example.samuraitravel.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReservationInputForm {

	@NotNull(message = "チェックイン日を入力してください。")
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate checkinDate;
	
	@NotNull(message = "チェックアウト日を入力してください。")
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate checkoutDate;
	
	@NotNull(message = "宿泊人数を入力してください。")
	private Integer numberOfPeople;
	
}
