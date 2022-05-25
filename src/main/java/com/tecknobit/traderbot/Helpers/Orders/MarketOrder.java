package com.tecknobit.traderbot.Helpers.Orders;

import com.tecknobit.traderbot.Records.Cryptocurrency;

/**
 * The {@code MarketOrder} interface defines base methods to create a market type order<br>
 * @author Tecknobit N7ghtm4r3
 * **/

public interface MarketOrder {

    /**
     * This method is used to get quantity for market order type <br>
     * @param cryptocurrency: cryptocurrency as {@link Cryptocurrency} used in the order
     * **/
    double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception;

}
