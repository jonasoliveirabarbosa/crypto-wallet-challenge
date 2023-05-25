package code.challenge.crypto.wallet.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main app class.
 */
public class App {
  public static final String HISTORY_PARAMS = "interval=d1&start=1617753600000&end=1617753601000";

  /**
   * main method can receive an path to an csv file on the arg
   * then execute all the logic to return the required info.
   */
  public static void main(String[] args) {
    String path = "input.csv";

    if (args.length > 0) {
      path = args[0];
    }
    Wallet wallet = new Wallet(readFile(path));
    Collection<Callable<Void>> tasks = new ArrayList<>();

    ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(3);

    for (final Coin coin : wallet.getCoins()) {
      Callable<Void> task = () -> {
        coin.setAsset(getAssetFromApi(coin));
        return null;
      };

      tasks.add(task);
    }

    try {
      executor.invokeAll(tasks);
      executor.shutdown();
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
      tasks.clear();
    } catch (InterruptedException ex) {
      System.out.println(ex);
    }

    for (final Coin coin : wallet.getCoins()) {
      Callable<Void> task = () -> {
        coin.setCurrentPrice(getCurrentPriceFromApi(coin));
        return null;
      };

      tasks.add(task);
    }

    executor = (ExecutorService) Executors.newFixedThreadPool(3);
    try {
      executor.invokeAll(tasks);
      executor.shutdown();
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException ex) {
      System.out.println(ex);
    }

    wallet.updateAndPrintWalletResume();
  }

  /**
   * Process an csv file and then returns an Coin collection.
   */
  public static List<Coin> readFile(String path) {
    String filePath = path;

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      reader.readLine();
      List<Coin> coins = new ArrayList<Coin>();
      while ((line = reader.readLine()) != null) {
        String[] columns = line.split(",");
        coins.add(new Coin(
            columns[0],
            Double.parseDouble(columns[1]),
            Double.parseDouble(columns[2])
          )
        );
      }
      return coins;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Make a call to the coincap api to get the coin
   * asset data.
   */
  public static Asset getAssetFromApi(Coin coin) {
    try {
      URL url = new URL(
          String.format("https://api.coincap.io/v2/assets/?search=%s", coin.getSymbol())
      );

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream())
      );

      String line;
      StringBuilder response = new StringBuilder();

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      reader.close();
      connection.disconnect();

      final ObjectMapper objectMapper = new ObjectMapper();
      JsonNode responseJsonNode = new ObjectMapper().readTree(response.toString());
      JsonNode dataJsonNode = responseJsonNode.get("data");

      if (dataJsonNode.isArray()) {
        for (JsonNode assetJsonNode : dataJsonNode) {
          if (coin.getSymbol().equals(assetJsonNode.get("symbol").asText())) {
            ObjectReader readerObj = objectMapper.readerFor(Asset.class);
            return readerObj.readValue(assetJsonNode);
          }
        }
      }

      throw new Exception("Symbol not found");

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Make a call to the coincap api to get the
   * coin current value.
   */
  public static double getCurrentPriceFromApi(Coin coin) {
    try {
      URL url = new URL(
          String.format("https://api.coincap.io/v2/assets/%s/history/?%s", coin.getAsset().getId(), HISTORY_PARAMS)
      );

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream())
      );

      String line;
      StringBuilder response = new StringBuilder();

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      reader.close();
      connection.disconnect();

      JsonNode responseJsonNode = new ObjectMapper().readTree(response.toString());
      JsonNode dataJsonNode = responseJsonNode.get("data");

      if (dataJsonNode.isArray()) {
        for (JsonNode historyJsonNode : dataJsonNode) {
          return historyJsonNode.get("priceUsd").asDouble();
        }
      }

      throw new Exception("id not found");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
