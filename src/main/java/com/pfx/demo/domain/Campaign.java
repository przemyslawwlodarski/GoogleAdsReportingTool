package com.pfx.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {
    private Long id;
    private String campaignName;
    private String campaignStatus;
    private String campaignAdvertisingChannelType;
    private String campaignAdvertisingChannelSubType;


}
