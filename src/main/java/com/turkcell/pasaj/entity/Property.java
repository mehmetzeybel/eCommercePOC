package com.turkcell.pasaj.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="TMP_PASAJ_PROPERTY")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder=true)
public class Property {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "TMP_PASAJ_PROPERTY_SEQUENCE")
    @SequenceGenerator(name = "TMP_PASAJ_PROPERTY_SEQUENCE", sequenceName = "TMP_PASAJ_PROPERTY_SEQ")
    private Long Id;
    private String key;
    private String value;
    private boolean isValueHash;
    private String odmValue;
    private boolean isOdmValueHash;
    private String gpValue;
    private boolean isGpValueHash;
    private String decription;

}
