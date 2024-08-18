package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an object of the nba data results.
 */
public class NbaTeam {
    String city;
    String name;
    @SerializedName("full_name")
    String fullname;
}
