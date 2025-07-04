package com.ajbxyyx.entity.po;

import lombok.Data;

@Data
public class Contact {

    private Long targetUid;//聯係人uid
    private Integer mobile;//聯係人電話
    private Integer mobileCountry;//聯係人電話國家
    private String firstName;//聯係人FirstName
    private String lastName;//聯係人LastName
    private Long uid;//本人uid


}
