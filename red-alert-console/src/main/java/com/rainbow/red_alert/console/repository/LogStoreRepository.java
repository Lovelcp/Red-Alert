package com.rainbow.red_alert.console.repository;

import com.rainbow.red_alert.console.domain.LogStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogStoreRepository extends JpaRepository<LogStore, Long> {
}
