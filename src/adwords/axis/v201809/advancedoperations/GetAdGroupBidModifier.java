// Copyright 2017 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package adwords.axis.v201809.advancedoperations;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201809.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupBidModifier;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupBidModifierPage;
import com.google.api.ads.adwords.axis.v201809.cm.AdGroupBidModifierServiceInterface;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.AdGroupBidModifierField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import java.rmi.RemoteException;

/**
 * This example illustrates how to retrieve the first 10 ad group level bid modifiers.
 *
 * <p>Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class GetAdGroupBidModifier {

  private static final int PAGE_SIZE = 10;

  public static void main(String[] args) {
    AdWordsSession session;
    try {
      // Generate a refreshable OAuth2 credential.
      Credential oAuth2Credential =
          new OfflineCredentials.Builder()
              .forApi(Api.ADWORDS)
              .fromFile()
              .build()
              .generateCredential();

      // Construct an AdWordsSession.
      session =
          new AdWordsSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();
    } catch (ConfigurationLoadException cle) {
      System.err.printf(
          "Failed to load configuration from the %s file. Exception: %s%n",
          DEFAULT_CONFIGURATION_FILENAME, cle);
      return;
    } catch (ValidationException ve) {
      System.err.printf(
          "Invalid configuration in the %s file. Exception: %s%n",
          DEFAULT_CONFIGURATION_FILENAME, ve);
      return;
    } catch (OAuthException oe) {
      System.err.printf(
          "Failed to create OAuth credentials. Check OAuth settings in the %s file. "
              + "Exception: %s%n",
          DEFAULT_CONFIGURATION_FILENAME, oe);
      return;
    }

    AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

    try {
      runExample(adWordsServices, session);
    } catch (ApiException apiException) {
      // ApiException is the base class for most exceptions thrown by an API request. Instances
      // of this exception have a message and a collection of ApiErrors that indicate the
      // type and underlying cause of the exception. Every exception object in the adwords.axis
      // packages will return a meaningful value from toString
      //
      // ApiException extends RemoteException, so this catch block must appear before the
      // catch block for RemoteException.
      System.err.println("Request failed due to ApiException. Underlying ApiErrors:");
      if (apiException.getErrors() != null) {
        int i = 0;
        for (ApiError apiError : apiException.getErrors()) {
          System.err.printf("  Error %d: %s%n", i++, apiError);
        }
      }
    } catch (RemoteException re) {
      System.err.printf(
          "Request failed unexpectedly due to RemoteException: %s%n", re);
    }
  }

  /**
   * Runs the example.
   *
   * @param adWordsServices the services factory.
   * @param session the session.
   * @throws ApiException if the API request failed with one or more service errors.
   * @throws RemoteException if the API request failed due to other errors.
   */
  public static void runExample(AdWordsServicesInterface adWordsServices,
      AdWordsSession session) throws RemoteException {
    // Get the AdGroupBidModifierService.
    AdGroupBidModifierServiceInterface adGroupBidModifierService =
        adWordsServices.get(session, AdGroupBidModifierServiceInterface.class);

    // Create selector.
    Selector selector = new SelectorBuilder()
        .fields(
            AdGroupBidModifierField.CampaignId,
            AdGroupBidModifierField.AdGroupId,
            AdGroupBidModifierField.BidModifier,
            AdGroupBidModifierField.Id)
        .offset(0)
        .limit(PAGE_SIZE)
        .build();

    AdGroupBidModifierPage page = adGroupBidModifierService.get(selector);
    if (page.getEntries() != null) {
      for (AdGroupBidModifier bidModifierResult : page.getEntries()) {
        String bidModifierValue =
            bidModifierResult.getBidModifier() != null
                ? bidModifierResult.getBidModifier().toString()
                : "unset";
        System.out.printf("Campaign ID %d, AdGroup ID %d, Criterion ID %d, "
            + "has ad group level modifier: %s%n",
            bidModifierResult.getCampaignId(),
            bidModifierResult.getAdGroupId(),
            bidModifierResult.getCriterion().getId(),
            bidModifierValue);
      }
    } else {
      System.out.println("No ad group level bid modifiers were found.");
    }
  }
}
