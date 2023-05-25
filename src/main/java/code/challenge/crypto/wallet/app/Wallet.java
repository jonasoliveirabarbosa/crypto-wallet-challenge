package code.challenge.crypto.wallet.app;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Represents an wallet containing an coin collection
 * and executing the required calculations.
 */
public class Wallet {
  private Collection<Coin> coins = null;
  private Double total;
  private Coin bestAsset;
  private Coin worstAsset;

  public Wallet(Collection<Coin> coins) {
    this.coins = coins;
    this.executeCalculations();
  }

  public void setCoins(Collection<Coin> coins) {
    this.coins = coins;
    this.executeCalculations();
  }

  /**
   * execute the total wallet value calculation
   * and update the best and worst coins accordingly.
   */
  public void executeCalculations() {
    this.calculateTotal();
    this.calculateBestPerformance();
    this.calculateWorstPerformance();
  }

  public Collection<Coin> getCoins() {
    return coins;
  }

  public Double getTotal() {
    return total;
  }

  public Coin getBestAsset() {
    return bestAsset;
  }

  public Coin getWorstAsset() {
    return worstAsset;
  }

  private void calculateTotal() {
    this.total = coins.stream()
      .map((Coin coin) -> coin.getCurrentPrice() * coin.getQuantity())
      .reduce(0.0, Double::sum)
    ;
  }

  private void calculateBestPerformance() {
    this.bestAsset = coins
      .stream()
      .max(Comparator.comparing(Coin::getPerformance))
      .orElseThrow(NoSuchElementException::new);
  }

  private void calculateWorstPerformance() {
    this.worstAsset = coins
      .stream()
      .min(Comparator.comparing(Coin::getPerformance))
      .orElseThrow(NoSuchElementException::new);
  }

  /**
   * Execute calculations and print wallet resume.
   */
  public void updateAndPrintWalletResume() {
    this.executeCalculations();
    DecimalFormat df = new DecimalFormat("#.##");
    df.setRoundingMode(RoundingMode.HALF_UP);

    System.out.println(
        String.format(
          "total=%s,best_asset=%s,best_performance=%s,worst_asset=%s,worst_performance=%s",
          df.format(this.getTotal()),
          this.getBestAsset().getSymbol(),
          df.format(this.getBestAsset().getPerformancePercent()),
          this.getWorstAsset().getSymbol(),
          df.format(this.getWorstAsset().getPerformancePercent())
        )
    );
  }
}
