package com.pfx.demo.reports;

import com.pfx.demo.domain.Keyword;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ReportPrinter {

    public void printReport(List<Keyword> keywordList) throws FileNotFoundException {
        String date = LocalDate.now().toString();
        File file = new File("Słowa kluczowe z wysokim współczynnikiem odrzuceń oraz wysokim kosztem" + date + ".csv");
        PrintWriter pw = new PrintWriter(file);
        StringBuilder sb = new StringBuilder();
        sb.append("KeyWord" + ";" + "MatchType" + ";" + "AdGroup" + ";" + "Campaign" + ";" + "Cost" + ";" + "Bounce Rate" + ";" + "Clicks" + "\n");
        for (Keyword keyword : keywordList) {
            sb.append(keyword.getKeywordText() + ";"
                    + keyword.getKeywordMatchType() + ";"
                    + keyword.getKeywordAdGroup() + ";"
                    + keyword.getKeywordCampaign() + ";"
                    + (String.valueOf(keyword.getKeywordCost() / 1000000)).replace(".", ",") + ";"
                    + (String.valueOf(keyword.getKeywordBounceRate())).replace(".", ",") + ";"
                    + keyword.getKeywordClicks()
                    + "\n");
        }
        pw.write(sb.toString());
        pw.close();
    }
}
