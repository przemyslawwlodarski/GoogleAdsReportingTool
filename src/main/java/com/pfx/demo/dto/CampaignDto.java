package com.pfx.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDto {
    private Long id;
    private String campaignName;
    private String campaignStatus;
    private String campaignAdvertisingChannelType;
    private String campaignAdvertisingChannelSubType;
}
