package com.turkcell.pasaj.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="TMP_LOG_PASAJ_PROPERTY")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LogProperty {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "TMP_LOG_PASAJ_PROPERTY_SEQUENCE")
    @SequenceGenerator(name = "TMP_LOG_PASAJ_PROPERTY_SEQUENCE", sequenceName = "TMP_LOG_PASAJ_PROPERTY_SEQ")
    private Long Id;
    private String username;
    private String operationType;
    private Date operationDate;
    @Lob
    private String operationRequest;
    private String operationResult;
}
