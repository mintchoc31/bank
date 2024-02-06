
package com.tenco.bank.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;
import lombok.Getter;

@Data
public class KakaoProfile {

    private Long id;
    private String connectedAt;
    private Properties properties;
    private KakaoAccount kakaoAccount;
    

}
