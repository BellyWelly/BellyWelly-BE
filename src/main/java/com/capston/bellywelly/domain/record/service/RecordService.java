package com.capston.bellywelly.domain.record.service;

import static com.capston.bellywelly.global.SecurityUtil.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capston.bellywelly.domain.member.entity.Member;
import com.capston.bellywelly.domain.record.dto.DietRecordRequestDto;
import com.capston.bellywelly.domain.record.dto.DietRecordResponseDto;
import com.capston.bellywelly.domain.record.dto.StressRequestDto;
import com.capston.bellywelly.domain.record.entity.Diet;
import com.capston.bellywelly.domain.record.entity.DietMeal;
import com.capston.bellywelly.domain.record.entity.Mealtime;
import com.capston.bellywelly.domain.record.entity.Stress;
import com.capston.bellywelly.domain.record.repository.DietMealRepository;
import com.capston.bellywelly.domain.record.repository.DietRepository;
import com.capston.bellywelly.domain.record.repository.StressRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

	private final DietRepository dietRepository;
	private final DietMealRepository dietMealRepository;
	private final MealService mealService;
	private final StressRepository stressRepository;

	public DietRecordResponseDto createDietRecord(DietRecordRequestDto requestDto) {
		Member member = getCurrentUser();

		Diet diet = Diet.builder()
			.member(member)
			.image(requestDto.getImage())
			.mealtime(Mealtime.from(requestDto.getMealtime()))
			.build();
		dietRepository.save(diet);

		mealService.findMealList(requestDto.getMeal())
			.forEach(meal -> dietMealRepository.save(DietMeal.builder().diet(diet).meal(meal).build()));

		return DietRecordResponseDto.builder()
			.diet(diet)
			.meal(requestDto.getMeal())
			.fodmapList(mealService.findLowOrHighFodmap(diet))
			.nutrient(mealService.sumNutrientComponent(diet))
			.build();
	}

	public void createStressRecord(StressRequestDto requestDto) {
		Member member = getCurrentUser();
		stressRepository.save(
			Stress.builder()
				.member(member)
				.degree(requestDto.getStress())
				.build()
		);
	}
}
