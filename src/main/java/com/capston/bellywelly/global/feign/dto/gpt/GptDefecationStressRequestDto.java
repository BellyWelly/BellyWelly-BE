package com.capston.bellywelly.global.feign.dto.gpt;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GptDefecationStressRequestDto {

	private List<Integer> defecation;
	private List<Integer> stress;
}
