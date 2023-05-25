package code.challenge.crypto.wallet.app;

/**
 * Represents an crypto coin and can compare price to determine the performance.
 */
public class Coin {

  private String symbol;
  private Double quantity;
  private Double price;
  private Asset asset;
  private Double currentPrice = 0.0;
  private Double performance;
  private Double performancePercent;

  /**
   * Instantiate a new Coin object.
   */
  public Coin(String symbol, Double quantity, Double price) {
    this.symbol = symbol;
    this.quantity = quantity;
    this.price = price;
    this.calculatePerformance();
    this.calculatePerformancePercent();
  }

  public String getSymbol() {
    return symbol;
  }

  public Double getPerformance() {
    return performance;
  }

  public Double getPerformancePercent() {
    return performancePercent;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public Double getPrice() {
    return price;
  }

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  /**
   * set a coin price.
   */
  public void setPrice(Double price) {
    this.price = price;
    this.calculatePerformance();
    this.calculatePerformancePercent();
  }

  /**
   * set a coin current price.
   */
  public void setCurrentPrice(Double currentPrice) {
    this.currentPrice = currentPrice;
    this.calculatePerformance();
    this.calculatePerformancePercent();
  }

  public Double getCurrentPrice() {
    return currentPrice;
  }

  private void calculatePerformance() {
    this.performance = (
        (this.currentPrice * this.quantity)
        - (this.price * this.quantity)
      );
  }

  private void calculatePerformancePercent() {
    this.performancePercent = (
        ((this.currentPrice - this.price) / (this.price)) + 1
      );
  }
}
