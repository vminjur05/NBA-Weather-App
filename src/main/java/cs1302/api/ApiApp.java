package cs1302.api;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.ProgressBar;
import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Takes in an NBA player and uses the city they play for to generate the weather in that city.
 */
public class ApiApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private static final String NBAPLAYERAPI = "https://api.balldontlie.io/v1/players";
    private static final String BALLDONTLIEAPIKEY = "5a9c49dc-b88b-4fd1-b59c-4ba247934272";
    private static final String WEATHERAPI = "https://api.weatherbit.io/v2.0/current";
    private static final String WEATHERBITAPIKEY = "210e3c9cb6574ad9bc2cd999fe39b04d";

    private Stage stage;
    private Scene scene;
    private VBox root;

    private HBox searchArea;
    private Text searchText;
    private TextField searchFieldOne;
    private TextField searchFieldTwo;
    private Button searchButton;

    private HBox playerInfo;
    private Text playerInfoField;

    private HBox weatherOutput;
    private VBox tileOne;
    private Text playerData;
    private Text cityTemp;
    private Text cityUV;
    private Text cityPrecipitation;
    private Text citySnow;
    private Text cityWindSpeed;
    private Text cityWindDirection;

    private HBox progressInfo;
    private Text apiSource;

    //misc variables
    private String combinedNBAUrl;
    private String combinedWeatherUrl;


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox(8);

        this.searchArea = new HBox(8);
        this.searchText = new Text("Search:");
        this.searchFieldOne = new TextField("Lebron");
        this.searchFieldTwo = new TextField("James");
        this.searchFieldOne.setPrefWidth(240);
        this.searchFieldTwo.setPrefWidth(240);
        this.searchButton = new Button("Get Weather");

        this.playerInfo = new HBox(8);
        this.playerInfoField = new Text("Please Enter an NBA Player that is Currently Playing.");

        this.weatherOutput = new HBox(8);
        this.tileOne = new VBox(8);
        this.playerData = new Text("Player Data");
        this.cityTemp = new Text("Temperature (F):");
        this.cityUV = new Text("UltraViolet Levels:");
        this.cityPrecipitation = new Text("Precipitation Levels:");
        this.citySnow = new Text("Snow Levels:");
        this.cityWindSpeed = new Text("Wind Speeds:");
        this.cityWindDirection = new Text("Wind Direction:");

        this.progressInfo = new HBox(8);
        this.apiSource = new Text("Data Provided by BallDontLie API and Weatherbit API.");
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(root);
        this.stage.setTitle("ApiApp!");
        this.stage.setScene(scene);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.sizeToScene();
        stage.show();

    } // start

    /** {@inheritDoc} */
    @Override
    public void init() {
        this.root.getChildren().addAll(this.searchArea, this.playerInfo,
                                       this.weatherOutput, this.progressInfo);
        this.searchArea.getChildren().addAll(this.searchText, this.searchFieldOne,
                                             this.searchFieldTwo, this.searchButton);
        this.playerInfo.getChildren().addAll(this.playerInfoField);
        this.weatherOutput.getChildren().addAll(this.tileOne);
        this.tileOne.getChildren().addAll(this.playerData, this.cityTemp, this.cityUV,
                                          this.cityPrecipitation, this.citySnow,
                                          this.cityWindSpeed, this.cityWindDirection);
        this.progressInfo.getChildren().addAll(this.apiSource);
        this.searchButton.setOnAction(event -> {
            this.queryCombination();
        });
        System.out.println("init() called");
    }

    /**
     * Takes the response from the first API and uses it to generate a response with the
     * second one.
     *
     */
    private void queryCombination() {
        Runnable runner = () -> {
            try {
                String nbaURL = this.buildNBAQuery(this.searchFieldOne.getText(),
                                                   this.searchFieldTwo.getText());
                combinedNBAUrl = nbaURL;
                HttpRequest nbaRequest = HttpRequest.newBuilder()
                    .uri(URI.create(nbaURL)).GET()
                    .setHeader("Authorization", BALLDONTLIEAPIKEY)
                    .build();
                HttpResponse<String> nbaResponse = HTTP_CLIENT
                    .send(nbaRequest, BodyHandlers.ofString());
                if (nbaResponse.statusCode() != 200) {
                    throw new IOException(nbaResponse.toString());
                }
                String nbaJsonString = nbaResponse.body();
                NbaDataResponse nbaData =
                    GSON.<NbaDataResponse>fromJson(nbaJsonString, NbaDataResponse.class);
                String allFirstNames = nbaData.data[0].firstname;
                String allLastNames = nbaData.data[0].lastname;
                String teamCity = nbaData.data[0].team.city;
                String teamFullName = nbaData.data[0].team.fullname;
                Platform.runLater(() -> this.playerInfoField.setText(allFirstNames + " "
                                                                     + allLastNames +
                                                                     " plays for the "
                                                                     + teamFullName
                                                                     + " located in "
                                                                     + teamCity));
                Platform.runLater(() -> this.playerData.setText("Heres some weather data from "
                                                                + teamCity));
                String weatherURL = this.buildWeatherQuery(teamCity);
                HttpRequest weatherRequest = HttpRequest.newBuilder()
                    .uri(URI.create(weatherURL)).build();
                HttpResponse<String> weatherResponse = HTTP_CLIENT
                    .send(weatherRequest, BodyHandlers.ofString());
                String weatherJsonString = weatherResponse.body();
                WeatherResponse weatherData =
                    GSON.<WeatherResponse>fromJson(weatherJsonString, WeatherResponse.class);
                double temperature = weatherData.data[0].apptemp;
                double ultraViolet = weatherData.data[0].uv;
                double precipitation = weatherData.data[0].precip;
                double snowLevels = weatherData.data[0].snow;
                double windSpeeds = weatherData.data[0].windspeed;
                double windDirection = weatherData.data[0].winddir;
                Platform.runLater(() -> {
                    this.cityTemp.setText("Temperature (F): " + temperature);
                    this.cityUV.setText("UltraViolet Levels: " + ultraViolet);
                    this.cityPrecipitation.setText("Precipitation Levels: " + precipitation);
                    this.citySnow.setText("Snow Levels: " + snowLevels);
                    this.cityWindSpeed.setText("Wind Speeds: " + windSpeeds);
                    this.cityWindDirection.setText("Wind Direction: " + windDirection);
                });
            } catch (ArrayIndexOutOfBoundsException | IOException | InterruptedException e) {
                System.err.println(e);
                e.printStackTrace();
                Platform.runLater(() -> alertError(e, combinedNBAUrl));
            }
        };
        runThread(runner, "Thread");
    }

    /**
     * Creates a weather query.
     *
     * @return the query
     *
     * @param city takes in the city generated from the nba player
     *
     */
    public String buildWeatherQuery(String city) {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String query = String.format("%s?city=%s&key=%s&include=hourly&units=I",
                                     WEATHERAPI, encodedCity, WEATHERBITAPIKEY);
        return query;
    }

    /**
     * Creates an nba query.
     *
     * @return the query
     *
     * @param firstName from the first text field
     * @param lastName from the second text field
     *
     */
    public String buildNBAQuery(String firstName, String lastName) {
        String encodedFirstName = URLEncoder.encode(firstName, StandardCharsets.UTF_8);
        String encodedLastName = URLEncoder.encode(lastName, StandardCharsets.UTF_8);
        String query = String.format("%s?first_name=%s&last_name=%s",
                                     NBAPLAYERAPI, encodedFirstName, encodedLastName);
        return query;
    }

    /**
     * Throws and alert whenever an exception is thrown.
     *
     * @param cause is the cause
     * @param url is the url that caused the error
     */
    private static void alertError(Throwable cause, String url) {
        TextArea text = new TextArea("URI: " + url + "\n" + "\n"
                                     + "Exception: " + cause.toString());
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    }

    /**
     * Creates a Thread.
     *
     * @param task is the runnable that is put into the thread
     * @param name is the name of the thread
     *
     */
    private static void runThread(Runnable task, String name) {
        Thread t = new Thread(task, name);
        t.start();
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        System.out.println("stop() called");
    }


} // ApiApp
