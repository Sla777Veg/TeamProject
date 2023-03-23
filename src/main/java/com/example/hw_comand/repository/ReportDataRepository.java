package com.example.hw_comand.repository;


import com.example.hw_comand.model.ReportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface ReportDataRepository.
 * @author Obukhov Damir
 * @version 1.0.0
 */
@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, Long> {

    Collection<ReportData> findListByChatId(Long id);

    Optional<ReportData> findByChatId(Long id);

    Optional<ReportData> findFirstByChatIdOrderByLastMessageDesc(Long id);

}