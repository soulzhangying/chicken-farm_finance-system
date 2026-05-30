package com.farm.finance.repository;

import com.farm.finance.entity.DeathRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeathRecordRepository extends JpaRepository<DeathRecord, Long> {
    
    // ========== 基本查询 ==========
    
    List<DeathRecord> findByHouseId(Long houseId);
    
    List<DeathRecord> findByOperatorId(Long operatorId);
    
    List<DeathRecord> findByDeathReason(String deathReason);
    
    List<DeathRecord> findByIsActive(Boolean isActive);
    
    List<DeathRecord> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<DeathRecord> findByDeathDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<DeathRecord> findByHouseId(Long houseId, Pageable pageable);
    
    Page<DeathRecord> findByOperatorId(Long operatorId, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT d FROM DeathRecord d WHERE " +
           "(:houseId IS NULL OR d.houseId = :houseId) AND " +
           "(:operatorId IS NULL OR d.operatorId = :operatorId) AND " +
           "(:deathReason IS NULL OR d.deathReason = :deathReason) AND " +
           "(:isActive IS NULL OR d.isActive = :isActive)")
    Page<DeathRecord> search(@Param("houseId") Long houseId,
                              @Param("operatorId") Long operatorId,
                              @Param("deathReason") String deathReason,
                              @Param("isActive") Boolean isActive,
                              Pageable pageable);
    
    @Query("SELECT d FROM DeathRecord d WHERE d.createdTime BETWEEN :startTime AND :endTime")
    List<DeathRecord> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    @Query("SELECT SUM(d.deathCount) FROM DeathRecord d WHERE d.houseId = :houseId")
    Integer sumDeathCountByHouseId(@Param("houseId") Long houseId);
    
    @Query("SELECT SUM(d.deathCount) FROM DeathRecord d WHERE d.deathDate BETWEEN :startDate AND :endDate")
    Integer sumDeathCountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(d.deathCount) FROM DeathRecord d WHERE d.isActive = true")
    Integer sumTotalDeathCount();
    
    @Query("SELECT d.deathReason, SUM(d.deathCount) FROM DeathRecord d WHERE d.isActive = true GROUP BY d.deathReason")
    List<Object[]> sumDeathCountGroupByReason();
}
