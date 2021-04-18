package net.wheman.melicoupon.strategy;

import java.util.HashMap;

public abstract interface StrategyHandler<T> {
    public HashMap<String, Double> ApplyStrategy(HashMap<String, Double> items, Double target);
}
