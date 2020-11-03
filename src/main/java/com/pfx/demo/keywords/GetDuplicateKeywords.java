package com.pfx.demo.keywords;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v5.errors.GoogleAdsError;
import com.google.ads.googleads.v5.errors.GoogleAdsException;
import com.google.ads.googleads.v5.services.GoogleAdsRow;
import com.google.ads.googleads.v5.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import com.pfx.demo.client.AdsClientCreator;
import com.pfx.demo.dto.KeywordDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//import com.google.ads.googleads.examples.utils.ArgumentNames;
//import com.google.ads.googleads.examples.utils.CodeSampleParams;

/**
 * Gets keyword performance statistics for the 50 keywords with the most impressions over the last 7
 * days.
 */
@Getter
public class GetDuplicateKeywords {

    private List<KeywordDto> keywordDtoList = new ArrayList<>();

        //3534529120

    /**
     * Runs the example.
     *
     * @param googleAdsClient the Google Ads API client.
     * @param customerId the client customer ID.
     * @throws GoogleAdsException if an API request failed with one or more service errors.
     */
    private void runQuery(GoogleAdsClient googleAdsClient, long customerId) {
        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT campaign.id " +
                            "FROM KEYWORDS_PERFORMANCE_REPORT ";



            // Constructs the SearchGoogleAdsStreamRequest.
            SearchGoogleAdsStreamRequest request =
                    SearchGoogleAdsStreamRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(searchQuery)
                            .build();

            // Creates and issues a search Google Ads stream request that will retrieve all of the
            // requested field values for the keyword.
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            // Iterates through the results in the stream response and prints all of the requested
            // field values for the keyword in each row.
            for (SearchGoogleAdsStreamResponse response : stream) {
                for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                    System.out.println(googleAdsRow);
                    /*Campaign campaign = googleAdsRow.getCampaign();
                    AdGroup adGroup = googleAdsRow.getAdGroup();
                    AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
                    Metrics metrics = googleAdsRow.getMetrics();

                    KeywordDto keywordDto = new KeywordDto(adGroupCriterion.getKeyword().getText().replace("+", "'+"),
                            adGroupCriterion.getKeyword().getMatchType().toString(),
                            adGroup.getName(),
                            campaign.getName(),
                            metrics.getCostMicros(),
                            metrics.getBounceRate(),
                            metrics.getClicks());
                    keywordDtoList.add(keywordDto);*/
                }
            }/*
            String date = LocalDate.now().toString();
            File file = new File("Znajdź aktywne duplikaty słów kluczowych "+ date +".csv");
            PrintWriter pw = new PrintWriter(file);
            StringBuilder sb = new StringBuilder();
            sb.append("KeyWord"+";"+"MatchType"+";"+"AdGroup"+";"+"Campaign"+";"+"Cost"+";"+"Bounce Rate"+";"+"Clicks"+"Historyczny QS"+"\n");
            for (KeywordDto keyword: keywordDtoList) {
                if (true) {
                    sb.append(keyword.getKeywordText() + ";"
                            + keyword.getKeywordMatchType() + ";"
                            + keyword.getKeywordAdGroup() + ";"
                            + keyword.getKeywordCampaign() + ";"
                            + (String.valueOf(keyword.getKeywordCost() / 1000000)).replace(".",",")+ ";"
                            + (String.valueOf(keyword.getKeywordBounceRate())).replace(".",",") + ";"
                            + keyword.getKeywordClicks()+ ";"
                            + "\n");
                }
            }
            pw.write(sb.toString());
            pw.close();*/
        //} catch (FileNotFoundException e) {
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public static void getKeywords(long accountId){
        GoogleAdsClient googleAdsClient = AdsClientCreator.googleAdsClientCreation();
        try {
            new GetDuplicateKeywords().runQuery(googleAdsClient, accountId);
        } catch (GoogleAdsException gae) {
            // GoogleAdsException is the base class for most exceptions thrown by an API request.
            // Instances of this exception have a message and a GoogleAdsFailure that contains a
            // collection of GoogleAdsErrors that indicate the underlying causes of the
            // GoogleAdsException.
            System.err.printf(
                    "Request ID %s failed due to GoogleAdsException. Underlying errors:%n",
                    gae.getRequestId());
            int i = 0;
            for (GoogleAdsError googleAdsError : gae.getGoogleAdsFailure().getErrorsList()) {
                System.err.printf("  Error %d: %s%n", i++, googleAdsError);
            }
            System.exit(1);
        }
    }
}
