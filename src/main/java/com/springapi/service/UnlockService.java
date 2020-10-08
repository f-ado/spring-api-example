package com.springapi.service;

import com.springapi.domain.User;
import com.springapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnlockService {
    @Value("${unlockUsersPeriod}")
    private long unlockPeriod = 30;
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 5000)
    public void unlockLockedAccounts() {
        List<User> users = userRepository
                .findAllByIsAccountNonLockedAndLastModifiedDateIsBefore(
                        false,
                        Timestamp.valueOf(LocalDateTime.now().minusSeconds(unlockPeriod))
                );
        if (users.size() > 0) {
            log.info("Unlocking accounts");
            users.forEach(user -> user.setAccountNonLocked(true));
            userRepository.saveAll(users);
        }
    }
}
