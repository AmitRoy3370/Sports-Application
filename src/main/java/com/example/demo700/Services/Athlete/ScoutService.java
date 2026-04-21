package com.example.demo700.Services.Athlete;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.demo700.DTOFiles.ScoutResponse;
import com.example.demo700.DTOFiles.ScoutsListResponseDTO;
import com.example.demo700.ENUMS.AthleteClassificationTypes;
import com.example.demo700.Models.Athlete.Scouts;

public interface ScoutService {
	
	Scouts addScout(Scouts scout, String userId);
	List<ScoutResponse> seeAllScouts();
	
	ScoutResponse findByAtheleteId(String atheleteId);
	ScoutResponse findByScoutsId(String scoutId);
	Scouts updateScouts(String scoutId, String userId, Scouts updatedScouts);
	boolean deleteScouts(String scoutId, String userId);
	List<ScoutResponse> findByEventsContainingIgnoreCase(String scoutId);
	List<ScoutResponse> findByMatchesContainingIgnoreCase(String scoutId);
	List<ScoutResponse> findByAthleteClassification(AthleteClassificationTypes athleteClassificationTypes);

    // NEW PAGINATED METHODS
	ScoutsListResponseDTO findAllScoutsPaginated(Pageable pageable, String baseUrl);
	ScoutsListResponseDTO searchByAtheleteIdPaginated(String atheleteId, Pageable pageable, String baseUrl);
	ScoutsListResponseDTO searchByScoutsIdPaginated(String scoutId, Pageable pageable, String baseUrl);
	ScoutsListResponseDTO searchByEventsContainingPaginated(String eventId, Pageable pageable, String baseUrl);
	ScoutsListResponseDTO searchByMatchesContainingPaginated(String matchId, Pageable pageable, String baseUrl);
	ScoutsListResponseDTO searchByClassificationPaginated(AthleteClassificationTypes classification, Pageable pageable, String baseUrl);
	ScoutsListResponseDTO searchByNamePaginated(String name, Pageable pageable, String baseUrl);

	
}
