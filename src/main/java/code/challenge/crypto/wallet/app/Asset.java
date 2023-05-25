package code.challenge.crypto.wallet.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an crypto asset.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset {
  private String id = null;
  private String rank = null;
  private String symbol = null;
  private String name = null;
  private String priceUsd = null;

  /**
   * instantiate a new Asset Object.
   */
  public Asset(
      @JsonProperty("id") String id,
      @JsonProperty("rank") String rank,
      @JsonProperty("symbol") String symbol,
      @JsonProperty("name") String name,
      @JsonProperty("priceUsd") String priceUsd
  ) {
    this.id = id;
    this.rank = rank;
    this.symbol = symbol;
    this.name = name;
    this.priceUsd = priceUsd;
  }

  public String getId() {
    return id;
  }

  public String getRank() {
    return rank;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "Asset [id=" + id + ", rank=" + rank
      + ", symbol=" + symbol + ", name=" + name + ", priceUsd=" + priceUsd + "]"
      ;
  }
}
