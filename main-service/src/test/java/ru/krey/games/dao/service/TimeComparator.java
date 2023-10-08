package ru.krey.games.dao.service;

import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeComparator {
    public void isEquals(LocalDateTime date1, LocalDateTime date2) {
        Assertions.assertThat(date1).isNotNull();
        Assertions.assertThat(date2).isNotNull();
        Assertions.assertThat(date1.getYear()).isEqualTo(date2.getYear());
        Assertions.assertThat(date1.getMonth()).isEqualTo(date2.getMonth());
        Assertions.assertThat(date1.getDayOfMonth()).isEqualTo(date2.getDayOfMonth());
        Assertions.assertThat(date1.getHour()).isEqualTo(date2.getHour());
        Assertions.assertThat(date1.getMinute()).isEqualTo(date2.getMinute());
        Assertions.assertThat(date1.getSecond()).isEqualTo(date2.getSecond());
    }
}
