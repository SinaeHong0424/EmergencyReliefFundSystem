package com.ny.safeny.repository;

import com.ny.safeny.model.Claim;
import com.ny.safeny.model.Claim.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    
    List<Claim> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Claim> findByStatus(ClaimStatus status);

    long countByStatus(ClaimStatus status);
}