package dev.pawan.sharemate.model;

import java.util.Map;

import dev.pawan.sharemate.util.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PREFERENCE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USER_ID", nullable = false)
    private Integer userId;

    @Column(name = "USER_PREFERENCE", columnDefinition = "json", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, String> userPreference;
}
