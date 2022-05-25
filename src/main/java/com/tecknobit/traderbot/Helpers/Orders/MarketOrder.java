package com.tecknobit.traderbot.Helpers.Orders;

import com.tecknobit.traderbot.Records.Cryptocurrency;

/**
 * The {@code MarketOrder} interface defines base methods to create a market type order<br>
 * @author Tecknobit N7ghtm4r3
 * **/

public interface MarketOrder {

    /**
     * This method is used to print details of an object <br>
     * @param cryptocurrency: cryptocurrency as {@link Cryptocurrency} used in the order
     * **/
    double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception;

}
