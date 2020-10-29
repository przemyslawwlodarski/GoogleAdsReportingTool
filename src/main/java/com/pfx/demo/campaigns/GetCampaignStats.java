package com.pfx.demo.campaigns;

// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v5.common.Metrics;
import com.google.ads.googleads.v5.errors.GoogleAdsError;
import com.google.ads.googleads.v5.errors.GoogleAdsException;
import com.google.ads.googleads.v5.resources.Campaign;
import com.google.ads.googleads.v5.services.GoogleAdsRow;
import com.google.ads.googleads.v5.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import com.pfx.demo.client.AdsClientCreator;
import com.pfx.demo.utils.CodeSampleParams;

import java.io.IOException;
import java.util.Scanner;

//import com.google.ads.googleads.examples.utils.ArgumentNames;
//import com.google.ads.googleads.examples.utils.CodeSampleParams;

/**
 * Gets keyword performance statistics for the 50 keywords with the most impressions over the last 7
 * days.
 */

public class GetCampaignStats {

    private static class GetCampaignStatsParams extends CodeSampleParams {
        Scanner input = new Scanner(System.in);
        String accountId = input.nextLine();
        private final Long customerId = Long.parseLong(accountId);
    }

    public static void main(String[] args) throws IOException {
        GetCampaignStatsParams params = new GetCampaignStatsParams();
        if (!params.parseArguments(args)) {

            // Either pass the required parameters for this example on the command line, or insert them
            // into the code here. See the parameter class definition above for descriptions.
            //params.customerId = Long.parseLong("3534529120");
            //params.customerId = Long.parseLong(accountId);
        }

        GoogleAdsClient googleAdsClient = AdsClientCreator.googleAdsClientCreation();

        try {
            new GetCampaignStats().runExample(googleAdsClient, params.customerId);
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

    /**
     * Runs the example.
     *
     * @param googleAdsClient the Google Ads API client.
     * @param customerId the client customer ID.
     * @throws GoogleAdsException if an API request failed with one or more service errors.
     */
    private void runExample(GoogleAdsClient googleAdsClient, long customerId) {
        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            String searchQuery =
                    "SELECT campaign.id, "
                            + "campaign.name, "
                            + "metrics.cost_micros "/*
                            + "ad_group.name, "
                            + "ad_group_criterion.criterion_id, "
                            + "ad_group_criterion.keyword.text, "
                            + "ad_group_criterion.keyword.match_type, "
                            + "metrics.impressions, "
                            + "metrics.clicks, "
                            + "metrics.cost_micros "*/
                            + "FROM campaign "
                            + "WHERE segments.date DURING LAST_7_DAYS "
                            + "AND campaign.advertising_channel_type = 'SEARCH' "
                            + "AND campaign.status = 'ENABLED' "
                            //+ "AND campaign.serving_status = 'SERVING' "
                            + "AND metrics.cost_micros > 0 "
                            //+ "AND ad_group_criterion.status IN ('ENABLED', 'PAUSED') "
                            // Limits to the 50 keywords with the most impressions in the date range.
                            //+ "ORDER BY metrics.impressions DESC "
                            + "LIMIT 50";
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
                    Campaign campaign = googleAdsRow.getCampaign();
                    //AdGroup adGroup = googleAdsRow.getAdGroup();
                    ///AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
                    Metrics metrics = googleAdsRow.getMetrics();

                    System.out.printf(
                            "Campaign name: "+ campaign.getName()
                                    + "costxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx: "+metrics.getCostMicros());
                                    /*+ "and ID %d "
                                    + "in ad group '%s' "
                                    + "with ID %d "
                                    + "in campaign '%s' "
                                    + "with ID %d "
                                    + "had %d impression(s), "
                                    + "%d click(s), "
                                    + "and %d cost (in micros) "
                                    + "during the last 7 days.%n",*/
                            //campaign.getName(),
                            //metrics.getCostMicros())
                            /*adGroupCriterion.getCriterionId(),
                            adGroup.getName(),
                            adGroup.getId(),
                            campaign.getName(),
                            campaign.getId(),
                            metrics.getImpressions(),
                            metrics.getClicks(),
                            metrics.getCostMicros())*/;
                }
            }
        }
    }
}

