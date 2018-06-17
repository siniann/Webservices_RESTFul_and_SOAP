package de.uniba.dsg.jaxws;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import javax.xml.ws.Endpoint;
import com.wrapper.spotify.exceptions.WebApiException;
import de.uniba.dsg.Configuration;

public class JaxWsServer {
    private static Properties properties = Configuration.loadProperties();
    public static void main(String[] args)throws IOException, WebApiException {
        String serverUri = properties.getProperty("soapServerUri");
        Endpoint endpoint = Endpoint.create(new MusicApiImpl());
        endpoint.publish(serverUri);
        System.out.println("Server ready to serve your JAX-WS requests...");
    }
    //method to check if the URI is available
    public static boolean isPortInUse(URI url) {
        boolean result;

        try {
            Socket s = new Socket(url.getHost(), url.getPort());
            s.close();
            result = true;
        }
        catch(Exception e) {
            result = false;
        }
        return(result);
    }
}