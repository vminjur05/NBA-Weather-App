package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * A result from the response from the BallDontLieAPI.
 */
public class NbaDataResults {
    @SerializedName("first_name")
    String firstname;
    @SerializedName("last_name")
    String lastname;
    NbaTeam team;
}
