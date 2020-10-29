package com.pfx.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeywordDto {
    private String keywordText;
    private String keywordMatchType;
    private String keywordAdGroup;
    private String keywordCampaign;
    //private String keywordStatus;
    private double keywordCost;
    private double keywordBounceRate;
    private long keywordClicks;

}
