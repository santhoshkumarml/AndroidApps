package com.android.alarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

public class AddressConvertUtil {
	private static final Logger LOGGER = Logger.getLogger(AddressConvertUtil.class.getCanonicalName());

	public static List<String> convertLocationToAddress(Context context, Location location) {
        List<String> addresses = new ArrayList<String>();
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addr=geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    3);

            if(addr != null)
            {
                LOGGER.info("Decoding Location");
                for(Address address:addr)
                {
                    String placeName = address.getLocality();
                    String featureName = address.getFeatureName();
                    String country = address.getCountryName();
                    String road = address.getThoroughfare();
                    String locInfo = String.format("\n[%s] [%s] [%s] [%s]",
                            placeName,featureName,road,country);
                    addresses.add(locInfo);
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return addresses;
    }
}
