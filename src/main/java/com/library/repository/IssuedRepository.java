package com.library.repository;

import com.library.entity.Issued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuedRepository extends JpaRepository<Issued, Long> {
    Optional<Issued> findByBookIdAndStatus(Long bookId, String status);
    List<Issued> findByUserId(Long userId);
    List<Issued> findByStatus(String status);
}
