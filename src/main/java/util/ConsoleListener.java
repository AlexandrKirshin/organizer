package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import util.handler.RequestHandler;

public class ConsoleListener {

  private final RequestHandler requestHandler;

  public ConsoleListener(RequestHandler requestHandler) {

    this.requestHandler = requestHandler;
  }

  public void run() {

    while (true) {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      try {
        String request = bufferedReader.readLine();
        requestHandler.handleRequest(request);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
