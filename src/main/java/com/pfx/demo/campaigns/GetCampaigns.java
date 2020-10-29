package com.pfx.demo.campaigns;

import com.beust.jcommander.Parameter;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v5.errors.GoogleAdsError;
import com.google.ads.googleads.v5.errors.GoogleAdsException;
import com.google.ads.googleads.v5.services.GoogleAdsRow;
import com.google.ads.googleads.v5.services.GoogleAdsServiceClient;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamRequest;
import com.google.ads.googleads.v5.services.SearchGoogleAdsStreamResponse;
import com.google.api.gax.rpc.ServerStream;
import com.pfx.demo.client.AdsClientCreator;
import com.pfx.demo.utils.ArgumentNames;
import com.pfx.demo.utils.CodeSampleParams;

import java.io.IOException;

//import com.google.ads.googleads.examples.utils.ArgumentNames;
//import com.google.ads.googleads.examples.utils.CodeSampleParams;

/** Gets all campaigns. To add campaigns, run AddCampaigns.java. */
public class GetCampaigns {

    private static class GetCampaignsWithStatsParams extends CodeSampleParams {

        @Parameter(names = ArgumentNames.CUSTOMER_ID, required = true)
        private Long customerId;
    }

    public static void main(String[] args) throws IOException {
        GetCampaignsWithStatsParams params = new GetCampaignsWithStatsParams();
        if (!params.parseArguments(args)) {

            // Either pass the required parameters for this example on the command line, or insert them
            // into the code here. See the parameter class definition above for descriptions.
            params.customerId = Long.parseLong("3534529120");
        }

        GoogleAdsClient googleAdsClient = AdsClientCreator.googleAdsClientCreation();

        try {
            new GetCampaigns().runExample(googleAdsClient, params.customerId);
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
            String query = "SELECT campaign.id, campaign.name FROM campaign ORDER BY campaign.id";
            // Constructs the SearchGoogleAdsStreamRequest.
            SearchGoogleAdsStreamRequest request =
                    SearchGoogleAdsStreamRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(query)
                            .build();

            // Creates and issues a search Google Ads stream request that will retrieve all campaigns.
            ServerStream<SearchGoogleAdsStreamResponse> stream =
                    googleAdsServiceClient.searchStreamCallable().call(request);

            // Iterates through and prints all of the results in the stream response.
            for (SearchGoogleAdsStreamResponse response : stream) {
                for (GoogleAdsRow googleAdsRow : response.getResultsList()) {
                    System.out.printf(
                            "Campaign with ID %d and name '%s' was found.%n",
                            googleAdsRow.getCampaign().getId(), googleAdsRow.getCampaign().getName());
                }
            }
        }
    }
}