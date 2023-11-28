package com.platform.iperform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Config {
    private final UUID id;
    private boolean checkPoint;
    private boolean checkIn;
    private String guidCheckIn;
    private String guidCheckPoint;
    private String guidEks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(id, config.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", checkPoint=" + checkPoint +
                ", checkIn=" + checkIn +
                ", guidCheckIn='" + guidCheckIn + '\'' +
                ", guidCheckPoint='" + guidCheckPoint + '\'' +
                ", guidEks='" + guidEks + '\'' +
                '}';
    }
}
