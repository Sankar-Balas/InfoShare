package com.example.sankba.infogroup;

/**
 * Created by SANKBA on 11/29/2017.
 */

public class igGeoWrapper
{
        private String deviceId = "TestShankar123";
        private double geoLatPos;
        private double geoLongPos;

        public igGeoWrapper() {
            //this constructor is required
        }

        public igGeoWrapper(String deviceId, double geoLatPos, double geoLongPos) {
            this.deviceId = deviceId;
            this.geoLatPos = geoLatPos;
            this.geoLongPos = geoLongPos;
        }

        public String getdeviceId() {
            return deviceId;
        }

        public double getgeoLatPos() {
            return geoLatPos;
        }

        public double getgeoLongPos() {
            return geoLongPos;
        }

}
