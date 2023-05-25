package code.challenge.crypto.wallet.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
  /**
   * test get asset method.
   */
  @Test
  public void testGetAssetFromApi() {
    Coin coin = new Coin(null, 0.0, 0.0);

    assertNull(
        "The function should not return anything if the symbol not exist",
        App.getAssetFromApi(coin)
    );

    coin = new Coin("DOESNOTEXISTS", 0.0, 0.0);
    assertNull(
        "The function should not return anything if the symbol not exist",
        App.getAssetFromApi(coin)
    );

    coin = new Coin("BTC", 0.0, 0.0);

    assertEquals(
        "The function should return a asset if a valid coin symbol is provided",
        App.getAssetFromApi(coin).getClass(),
        Asset.class
    );
  }

  @Test
  public void getCurrentPriceFromApi() {
    Coin coin = new Coin(null, 0.0, 0.0);

    assertEquals(
        "The function should return 0 symbol not exist",
        0.0,
        App.getCurrentPriceFromApi(coin),
        0.001
    );

    coin = new Coin("DOESNOTEXISTS", 0.0, 0.0);
    assertEquals(
        "The function should return 0 symbol not exist",
        0.0,
        App.getCurrentPriceFromApi(coin),
        0.001
    );

    coin = new Coin("BTC", 0.0, 0.0);

    Asset assetTest = new Asset("bitcoin", "1", "BTC", "bitcoin", "2.0");
    coin.setAsset(assetTest);

    assertNotEquals(
        "The function should return a value if a valid coin symbol is provided",
        0.0,
        App.getCurrentPriceFromApi(coin)
    );
  }
}
