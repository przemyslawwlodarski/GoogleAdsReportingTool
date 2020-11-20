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



package com.pfx.demo.keywords;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v5.common.Metrics;
import com.google.ads.googleads.v5.errors.GoogleAdsError;
import com.google.ads.googleads.v5.errors.GoogleAdsException;
import com.google.ads.googleads.v5.resources.AdGroup;
import com.google.ads.googleads.v5.resources.AdGroupCriterion;
import com.google.ads.googleads.v5.resources.Campaign;
import com.google.ads.googleads.v5.services.GoogleAdsRow;
import com.google.ads.googleads.v5.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import com.pfx.demo.domain.Keyword;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gets keyword performance statistics for the 50 keywords with the most impressions over the last 7
 * days.
 */

public class GetKeywords {

  public List<Keyword> getKeywords(long customerId) {
    List<Keyword> keywordsList = new ArrayList<>();
    GoogleAdsClient googleAdsClient = null;
    try {
      googleAdsClient = GoogleAdsClient.newBuilder().fromPropertiesFile().build();
    } catch (FileNotFoundException fnfe) {
      System.err.printf(
              "Failed to load GoogleAdsClient configuration from file. Exception: %s%n", fnfe);
      System.exit(1);
    } catch (IOException ioe) {
      System.err.printf("Failed to create GoogleAdsClient. Exception: %s%n", ioe);
      System.exit(1);
    }

    try (GoogleAdsServiceClient googleAdsServiceClient =
                googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
      String searchQuery =
              "SELECT campaign.id, "
                      + "campaign.name, "
                      + "ad_group.id, "
                      + "ad_group.name, "
                      + "ad_group_criterion.criterion_id, "
                      + "ad_group_criterion.keyword.text, "
                      + "ad_group_criterion.keyword.match_type, "
                      + "metrics.impressions, "
                      + "metrics.clicks, "
                      + "metrics.cost_micros, "
                      + "metrics.bounce_rate "
                      + "FROM keyword_view "
                      + "WHERE segments.date DURING LAST_7_DAYS "
                      + "AND campaign.advertising_channel_type = 'SEARCH' "
                      + "AND ad_group.status = 'ENABLED' "
                      + "ORDER BY metrics.impressions DESC ";
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
            AdGroup adGroup = googleAdsRow.getAdGroup();
            AdGroupCriterion adGroupCriterion = googleAdsRow.getAdGroupCriterion();
            Metrics metrics = googleAdsRow.getMetrics();

            Keyword keyword = new Keyword(adGroupCriterion.getKeyword().getText().replaceFirst("[+]", "'+"),
                    adGroupCriterion.getKeyword().getMatchType().toString(),
                    adGroup.getName(),
                    campaign.getName(),
                    metrics.getCostMicros(),
                    metrics.getBounceRate(),
                    metrics.getClicks());
            keywordsList.add(keyword);
          }
        }
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
    return keywordsList;
  }
}
