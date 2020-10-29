package com.pfx.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Keyword {
    private String keywordText;
    private String keywordMatchType;
    private String keywordAdGroup;
    private String keywordCampaign;
    //private String keywordStatus;
    private double keywordCost;
    private double keywordBounceRate;
    private long keywordClicks;
    //private double keywordIs;
}
