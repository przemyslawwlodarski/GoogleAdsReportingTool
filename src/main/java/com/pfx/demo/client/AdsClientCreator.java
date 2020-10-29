package com.pfx.demo.client;

import com.google.ads.googleads.lib.GoogleAdsClient;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdsClientCreator {
    public static GoogleAdsClient googleAdsClientCreation() {
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
        return googleAdsClient;
    }
}
