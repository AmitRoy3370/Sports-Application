package com.example.demo700.Repositories.Athelete;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.Athlete.Athelete;

@Repository
public interface AtheleteRepository extends MongoRepository<Athelete, String> {

    // 🔥 PAGINATED METHODS - Use these for large datasets
    Page<Athelete> findAll(Pageable pageable);
    
    Page<Athelete> findByAgeLessThan(int age, Pageable pageable);
    
    Page<Athelete> findByHeightGreaterThan(double height, Pageable pageable);
    
    Page<Athelete> findByWeightLessThan(double weight, Pageable pageable);
    
    Page<Athelete> findByPresentTeamIgnoreCase(String presentTeam, Pageable pageable);
    
    Page<Athelete> findByPositionContainingIgnoreCase(String position, Pageable pageable);
    
    Page<Athelete> findByEventAttendenceContainingIgnoreCase(String eventName, Pageable pageable);
    
    Page<Athelete> findByGameLogsContainingIgnoreCase(String gameLog, Pageable pageable);
    
    Page<Athelete> findByAgeLessThanAndHeightGreaterThan(int age, double height, Pageable pageable);
    
    // 🔥 NON-PAGINATED METHODS - Keep for backward compatibility but use with caution
    // These should only be used for small result sets or in admin contexts
    List<Athelete> findByAgeLessThan(int age);
    
    List<Athelete> findByHeightGreaterThan(double height);
    
    List<Athelete> findByWeightLessThan(double weight);
    
    List<Athelete> findByPresentTeamIgnoreCase(String presentTeam);
    
    List<Athelete> findByPositionContainingIgnoreCase(String position);
    
    List<Athelete> findByEventAttendenceContainingIgnoreCase(String eventName);
    
    List<Athelete> findByGameLogsContainingIgnoreCase(String gameLog);
    
    List<Athelete> findByAgeLessThanAndHeightGreaterThan(int age, double height);
    
    List<Athelete> findBypresentTeam(String teamName);
    
    Page<Athelete> findByUserIdIn(List<String> userIds, Pageable pageable);
    
    List<Athelete> findByUserIdIn(List<String> userIds);
    
    // 🔥 CUSTOM QUERIES WITH PAGINATION
    @Query("{ 'presentTeam': { $regex: ?0, $options: 'i' } }")
    List<Athelete> searchByTeamNamePartial(String partialName);
    
    @Query("{ 'presentTeam': { $regex: ?0, $options: 'i' } }")
    Page<Athelete> searchByTeamNamePartial(String partialName, Pageable pageable);
    
    @Query("{ 'eventAttendence': { $in: ?0 } }")
    List<Athelete> findByMultipleEvents(List<String> eventNames);
    
    @Query("{ 'eventAttendence': { $in: ?0 } }")
    Page<Athelete> findByMultipleEvents(List<String> eventNames, Pageable pageable);
    
    @Query("{ 'weight': { $gte: ?0, $lte: ?1 } }")
    List<Athelete> findByWeightRange(double min, double max);
    
    @Query("{ 'weight': { $gte: ?0, $lte: ?1 } }")
    Page<Athelete> findByWeightRange(double min, double max, Pageable pageable);
    
    // 🔥 SINGLE RESULT METHODS
    Optional<Athelete> findByUserId(String userId);
    
    // 🔥 BATCH FETCH METHODS - For efficient bulk operations
    List<Athelete> findAllById(Iterable<String> ids);
    
    // 🔥 COUNT METHODS
    long countByPresentTeamIgnoreCase(String presentTeam);
    long countByAgeLessThan(int age);
    long countByHeightGreaterThan(double height);
    long countByWeightLessThan(double weight);
    
    // 🔥 DELETE METHODS
    long deleteByUserId(String userId);
    
    // 🔥 ADVANCED QUERIES WITH PROJECTION (for better performance)
    @Query(value = "{}", fields = "{ 'id': 1, 'userId': 1, 'age': 1, 'position': 1 }")
    List<Athelete> findAllWithMinimalFields();
    
    @Query(value = "{ 'presentTeam': ?0 }", fields = "{ 'id': 1, 'userId': 1, 'age': 1 }")
    List<Athelete> findByPresentTeamWithMinimalFields(String teamName);
}