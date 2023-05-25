package code.challenge.crypto.wallet.app;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * add tests for the wallet class.
 */
public class WalletTest {
  private Coin bitTest = null;
  private Coin theterTest = null;
  private Coin ethTest = null;

  /**
   * Initialize some coins to test the wallet.
   */
  @Before
  public void initEach() {
    List<Coin> list = new ArrayList<>();

    Assert.assertThrows(NoSuchElementException.class, () -> {
      new Wallet(list);
    });

    this.bitTest = new Coin("BTC", 1.0, 1.0);
    this.theterTest = new Coin("USDT", 1.0, 1.5);
    this.ethTest = new Coin("ETH", 1.0, 2.0);
  }

  @Test
  public void testGetTotal() {
    List<Coin> coinCollection = Arrays.asList(this.bitTest, this.theterTest, this.ethTest);
    Wallet wallet = new Wallet(coinCollection);

    assertEquals(
        "Total should be zero before set the current price",
        0.0d,
        wallet.getTotal(),
        0.001
    );

    this.bitTest.setCurrentPrice(1.5);
    this.theterTest.setCurrentPrice(2.5);
    this.ethTest.setCurrentPrice(2.0);

    assertEquals(
        "Total should not update before calculation",
        0.0d,
        wallet.getTotal(),
        0.001
    );

    wallet.executeCalculations();

    assertEquals(
        "Total should be the sum of the current price",
        6.0d,
        wallet.getTotal(),
        0.001
    );
  }

  @Test
  public void testGetBestAsset() {
    this.bitTest.setCurrentPrice(1.5);
    this.theterTest.setCurrentPrice(2.5);
    this.ethTest.setCurrentPrice(2.0);

    List<Coin> coinCollection = Arrays.asList(this.bitTest, this.theterTest, this.ethTest);
    Wallet wallet = new Wallet(coinCollection);

    assertEquals("should return the highest increase",
        wallet.getBestAsset(),
        this.theterTest
    );

    this.ethTest.setCurrentPrice(4.0);
    assertEquals("should not update best asset before calculation",
        wallet.getBestAsset(),
        this.theterTest
    );

    wallet.executeCalculations();

    assertEquals("should update best asset after calculation",
        wallet.getBestAsset(),
        this.ethTest
    );
  }

  @Test
  public void testGetWorstAsset() {
    this.bitTest.setCurrentPrice(1.5);
    this.theterTest.setCurrentPrice(2.5);
    this.ethTest.setCurrentPrice(2.0);

    List<Coin> coinCollection = Arrays.asList(this.bitTest, this.theterTest, this.ethTest);
    Wallet wallet = new Wallet(coinCollection);

    assertEquals("should return the lowest increase",
        wallet.getWorstAsset(),
        this.ethTest
    );

    this.theterTest.setCurrentPrice(1.0);
    assertEquals("should not update best asset before calculation",
        wallet.getWorstAsset(),
        this.ethTest
    );

    wallet.executeCalculations();

    assertEquals("should update best asset after calculation",
        wallet.getWorstAsset(),
        this.theterTest
    );
  }
}
