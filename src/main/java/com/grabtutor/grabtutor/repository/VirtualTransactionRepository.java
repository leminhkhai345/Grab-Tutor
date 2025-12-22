package com.grabtutor.grabtutor.repository;

import com.grabtutor.grabtutor.entity.VirtualTransaction;
import com.grabtutor.grabtutor.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VirtualTransactionRepository extends JpaRepository<VirtualTransaction, String> {
    Page<VirtualTransaction> findAllByAccountBalanceId(String userId, Pageable pageable);
    Page<VirtualTransaction> findAll(Pageable pageable);
    @Query(value = """
      SELECT MONTH(created_at) AS m, COALESCE(SUM(amount),0) AS total
      FROM virtual_transaction
      WHERE created_at >= :start AND created_at < :end
        AND type = :type
        AND status = 'SUCCESS'
      GROUP BY MONTH(created_at)
    """, nativeQuery = true)
    List<Object[]> sumByMonth(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("type") String type);
}
