package com.pfx.demo.reports;

import com.pfx.demo.domain.Keyword;

import java.util.ArrayList;
import java.util.List;

public class ReportMaker {
    public List<Keyword> brAndCostReport(List<Keyword>keywordList, double finalBounceRateLevel, double finalCostLevel){
        List<Keyword> newList = new ArrayList<>();
        for (Keyword keyword: keywordList) {
            if ((keyword.getKeywordCost() >= finalCostLevel/1000000)&&(keyword.getKeywordBounceRate()>=finalBounceRateLevel)) {
                newList.add(keyword);
            }
        }
        return newList;
    }
}
