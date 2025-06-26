package com.example.samuraitravel.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
	
	@Min(value = 1 , message = "評価は1～5のいずれかを選択してください。")
	@Max(value = 5 , message = "評価は1～5のいずれかを選択してください。")
	@NotNull(message = "評価は1～5のいずれかを選択してください。")
	private Integer score;
	
	@NotBlank(message = "コメントを入力してください")
	@Size( max = 300, message = "コメントは300文字以内で入力してください。")
	private String content;
	

}
