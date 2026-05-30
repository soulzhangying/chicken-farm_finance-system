package com.farm.finance.repository;

import com.farm.finance.entity.ChickenBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChickenBatchRepository extends JpaRepository<ChickenBatch, Long> {
    
    List<ChickenBatch> findByIsActiveTrue();
    
    List<ChickenBatch> findByIsActiveTrueOrderByCreatedTimeDesc();
    
    List<ChickenBatch> findByHouseId(Long houseId);
    
    List<ChickenBatch> findByHouseIdAndIsActiveTrue(Long houseId);
    
    Optional<ChickenBatch> findByBatchNo(String batchNo);
    
    Optional<ChickenBatch> findByBatchName(String batchName);
    
    Optional<ChickenBatch> findByIdAndIsActiveTrue(Long id);
    
    boolean existsByBatchNo(String batchNo);
    
    boolean existsByBatchName(String batchName);
    
    @Query("SELECT b FROM ChickenBatch b WHERE b.isActive = true AND b.status = :status")
    List<ChickenBatch> findByStatus(@Param("status") String status);
    
    @Query("SELECT b FROM ChickenBatch b WHERE b.isActive = true AND " +
           "(LOWER(b.batchNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.batchName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<ChickenBatch> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT SUM(b.entryQuantity) FROM ChickenBatch b WHERE b.isActive = true")
    Integer sumEntryQuantity();
    
    @Query("SELECT SUM(b.currentQuantity) FROM ChickenBatch b WHERE b.status = 'ACTIVE' AND b.isActive = true")
    Integer sumCurrentQuantity();
    
    @Query("SELECT b FROM ChickenBatch b WHERE b.isActive = true AND " +
           "(:houseId IS NULL OR b.houseId = :houseId) AND " +
           "(:status IS NULL OR b.status = :status)")
    Page<ChickenBatch> search(@Param("houseId") Long houseId, 
                              @Param("status") String status, 
                              Pageable pageable);
}
