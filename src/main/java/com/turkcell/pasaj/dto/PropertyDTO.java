package com.turkcell.pasaj.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PropertyDTO {

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "Key info")
    private String key;
    @ApiModelProperty(value = "Value info")
    private String value;
    @ApiModelProperty(value = "Encryption/Decryption info for value")
    private boolean isValueHash;
    @ApiModelProperty(value = "Odm Value info")
    private String odmValue;
    @ApiModelProperty(value = "Encryption/Decryption info for Odm value")
    private boolean isOdmValueHash;

    @ApiModelProperty(value = "GP Value info")
    private String gpValue;

    @ApiModelProperty(value = "Encryption/Decryption info for GP Value")
    private boolean isGpValueHash;

    @ApiModelProperty(value = "Description info")
    private String decription;

}
