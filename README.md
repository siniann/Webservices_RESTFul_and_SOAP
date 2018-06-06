Import the Gradle project via `build.gradle` inside your preferred IDE.  
IntelliJ: https://www.jetbrains.com/help/idea/2017.1/importing-a-gradle-project-or-a-gradle-module.html  
Eclipse: http://www.vogella.com/tutorials/EclipseGradle/article.html

The `de.uniba.dsg.SpotifyApi` class is the main entry point for using the Spotify Api wrapper functionality provided by `se.michaelthelin.spotify:spotify-web-api-java`. 
To be able to authenticate your requests, set your Spotify API credentials in `src/main/resources/config.properties`.

All service interfaces are located inside `de.uniba.dsg.interfaces`.
Main entry point for the JAX-RS web service is the `de.uniba.dsg.jaxrs.MusicApi` class.
For JAX-WS take a look at the `de.uniba.dsg.jaxws.MusicApi` interface and the `de.uniba.dsg.jaxws.MusicApiImpl` class.
Resource implementations are located inside `de.uniba.dsg.jaxws.resources` and `de.uniba.dsg.jaxrs.resources`.
The shared model and entity classes for both web services are located inside `de.uniba.dsg.models`. 

Start the JAXRS server via `de.uniba.dsg.jaxrs.JaxRsServer.java`.  
Start the JAXWS server via `de.uniba.dsg.jaxws.JaxWsServer.java`.
  

