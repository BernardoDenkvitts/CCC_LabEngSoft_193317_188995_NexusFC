package com.nexusfc.api.Market.Exception;

import java.text.DecimalFormat;

public class InsufficientBalance extends RuntimeException {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public InsufficientBalance(Float currBalance, Float requiredAmount) {
        super(String.format("Not enough coins, current balance %s, necessary balance %s", df.format(currBalance), df.format(requiredAmount)));
    }
}
