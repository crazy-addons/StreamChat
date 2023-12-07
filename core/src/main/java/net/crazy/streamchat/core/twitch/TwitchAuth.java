package net.crazy.streamchat.core.twitch;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import net.crazy.streamchat.core.StreamChat;
import net.crazy.streamchat.core.events.auth.OAuthFailedEvent;
import net.labymod.api.util.io.web.URLResolver;
import net.labymod.api.util.io.web.WebResponse;
import net.labymod.api.util.io.web.exception.WebRequestException;

public class TwitchAuth {

   private final StreamChat addon;

   public TwitchAuth(StreamChat addon) {
      this.addon = addon;
      this.loadData();
   }

   private String clientID;
   private String scopes = "";
   private ServerSocket server = null;
   private Socket socket = null;

   /**
    * Starts the entire OAuth Process
    */
   public void startOAuth() {
      if (!addon.configuration().apiConfig.token.equals(""))
         return;

      addon.logger().info("Starting StreamChat-OAuth");

      if (!dataLoaded) {
         return;
      }

      String state = this.createStateToken();
      String authUrl = String.format("https://id.twitch.tv/oauth2/authorize"
              + "?response_type=token"
              + "&client_id=%s"
              + "&redirect_uri=%s"
              + "&scope=%s"
              + "&state=%s",
          clientID,
          "http://localhost:3000",
          scopes,
          state);

      try {
         this.server = new ServerSocket(3000);
         addon.labyAPI().minecraft().chatExecutor().openUrl(authUrl);
         this.listenForCode(state);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void listenForCode(String state) {
      Executors.newSingleThreadExecutor().execute(() -> {
         while (this.server.isBound() && !this.server.isClosed()) {
            try (final Socket socket = this.server.accept();
                final Scanner scanner = new Scanner(socket.getInputStream());
                BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream())) {
               this.socket = socket;
               String path = scanner.nextLine().split(" ")[1];

               output.write("HTTP/1.0 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
               output.write("Content-Type: html; charset=UTF-8\r\n".getBytes(StandardCharsets.UTF_8));
               output.write("\r\n".getBytes(StandardCharsets.UTF_8));
               output.write("<!DOCTYPE html>\n".getBytes(StandardCharsets.UTF_8));
               // Parsing the access_token hash into an actual query parameter
               output.write(("<html>\n"
                   + "<body>\n"
                   + "<script>"
                   + "if (window.location.hash) {"
                   + "var fragment = window.location.hash.substring(1);"
                   + "var newUrl = window.location.href.replace('#', '?');"
                   + "window.history.replaceState({}, document.title, newUrl);"
                   + "location.reload();"
                   + "}"
                   + "</script>\nLoading token...</body>\n<html>").getBytes(StandardCharsets.UTF_8));

               if (path.contains("?access_token=") && path.contains("state=")) {
                  String providedState = path.split("state=")[1];
                  providedState = providedState.substring(0, 12);

                  if (!state.equals(providedState)) {
                     this.handleError("Invalid state");
                     return;
                  }

                  String accessToken = path.substring(path.indexOf("=") + 1, path.indexOf("&"));
                  addon.configuration().apiConfig.token = accessToken;
                  addon.saveConfiguration();
                  this.finalizeOAuth(true);
               }

               output.flush();
            } catch (IOException exception) {
               exception.printStackTrace();
               this.handleError("Unknown error. Please send the log to the Developer!");
            }
         }
      });
   }

   private void handleError(String error) {
      addon.labyAPI().eventBus().fire(new OAuthFailedEvent(error));
      this.finalizeOAuth(false);
   }

   private void finalizeOAuth(boolean success) {
      try {
         PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
         printWriter.print("HTTP/1.0 200 OK\r\n");
         printWriter.write("Content-Type: html; charset=UTF-8\r\n");
         printWriter.write("\r\n");
         printWriter.write(success ? "You can close this window now" : "Error, please try again later");
         printWriter.flush();
         printWriter.close();
         socket.close();
         server.close();
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   private boolean dataLoaded = false;

   /**
    * Load current settings for O-Auth process
    */
   private void loadData() {
      if (dataLoaded) {
         return;
      }

      URLResolver.readJson("https://dl.csjako.com/addons/streamchat/config.json",
          true, new WebResponse<>() {
             @Override
             public void success(JsonElement result) {
                JsonObject response = result.getAsJsonObject();
                clientID = response.get("client_id").getAsString();
                JsonArray responseScopes = response.get("scopes").getAsJsonArray();
                ArrayList<String> rawScopes = new ArrayList<>();

                for (int i = 0; i < responseScopes.size(); i++) {
                   rawScopes.add(responseScopes.get(i).getAsString());
                }
                scopes = String.join("+", rawScopes);

                dataLoaded = true;
             }

             @Override
             public void failed(WebRequestException exception) {
                exception.printStackTrace();
                dataLoaded = false;
             }
          });
   }

   /**
    * Creates a random string to protect against CSRF
    *
    * @return random 12 letter string
    */
   private String createStateToken() {
      int leftLimit = 48; // numeral '0'
      int rightLimit = 122; // letter 'z'
      int targetStringLength = 12;
      Random random = new Random();

      return random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
   }
}
