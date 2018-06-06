package de.uniba.dsg;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Logger;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

public class SpotifyApi {
    private static final Logger LOGGER = Logger.getLogger(SpotifyApi.class.getName());

    private static Api api;
    private static Properties properties = new Properties();
    private static volatile LocalDateTime expirationTime;

    private static final Object lock = new Object();

    static {
        try (InputStream stream = SpotifyApi.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(stream);
            api = Api.builder()
                    .clientId(properties.getProperty("spotifyClientId"))
                    .clientSecret(properties.getProperty("spotifyClientSecret"))
                    .build();
        } catch (IOException e) {
            LOGGER.severe("Cannot load configuration file.");
        }
    }

    private SpotifyApi() {}

    public static Api getInstance() {
        synchronized (lock) {
            try {
                signIn();
            } catch (WebApiException | IOException e) {
                LOGGER.severe("Failed to authenticate with Spotify API.");
            }
            return api;
        }
    }

    private static void signIn() throws WebApiException, IOException {
        if (expirationTime == null || expirationTime.isBefore(LocalDateTime.now())) {
            ClientCredentialsGrantRequest credentialsRequest = api.clientCredentialsGrant().build();
            ClientCredentials credentials = credentialsRequest.get();
            // expiration is in seconds
            expirationTime = LocalDateTime.now().plusSeconds(credentials.getExpiresIn() - 5);
            api.setAccessToken(credentials.getAccessToken());
        }
    }
}
