package com.tecknobit.traderbot.Helpers.Orders;

import com.tecknobit.traderbot.Records.Cryptocurrency;

public interface MarketOrder {

    double getMarketOrderQuantity(Cryptocurrency cryptocurrency) throws Exception;

}
