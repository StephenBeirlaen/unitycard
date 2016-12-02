package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Stephen on 1/12/2016.
 */

public class GeoCodeResponse {
    @SerializedName("results")
    private List<GeoCodeResult> results;

    public List<GeoCodeResult> getResults() {
        return results;
    }

    public class GeoCodeResult {
        @SerializedName("address_components")
        private List<AddressComponent> addressComponents;

        public class AddressComponent {
            @SerializedName("long_name")
            private String longName;

            @SerializedName("short_name")
            private String shortName;

            @SerializedName("types")
            private List<String> types;
        }

        @SerializedName("formatted_address")
        private String formattedAddress;

        @SerializedName("geometry")
        private Geometry geometry;

        public Geometry getGeometry() {
            return geometry;
        }

        public class Geometry {
            @SerializedName("location")
            private Location location;

            public Location getLocation() {
                return location;
            }

            public class Location {
                @SerializedName("lat")
                private double lat;

                public double getLat() {
                    return lat;
                }

                @SerializedName("lng")
                private double lng;

                public double getLng() {
                    return lng;
                }
            }

            @SerializedName("location_type")
            private String locationType;

            @SerializedName("viewport")
            private Viewport viewport;

            public class Viewport {
                @SerializedName("northeast")
                private Location northEast;

                @SerializedName("southwest")
                private Location southWest;
            }
        }

        @SerializedName("place_id")
        private String place_id;

        @SerializedName("types")
        private List<String> types;
    }
}
