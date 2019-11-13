package com.springapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"created_at", "updated_at"},
        allowGetters = true
)
public abstract class DateAudit implements Serializable {

    @Getter
    @Setter
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonProperty("created_at")
    private Instant createdAt;

    @Getter
    @Setter
    @LastModifiedDate
    @Column(nullable = false)
    @JsonProperty("updated_at")
    private Instant updatedAt;
}
