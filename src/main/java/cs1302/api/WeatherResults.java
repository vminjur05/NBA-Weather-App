package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * represents the result in a response from the weather API.
 */
public class WeatherResults {
    @SerializedName("app_temp")
    double apptemp;
    double uv;
    double precip;
    double snow;
    @SerializedName("wind_spd")
    double windspeed;
    @SerializedName("wind_dir")
    double winddir;
}
