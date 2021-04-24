package net.wheman.melicoupon.strategy;

import java.util.HashMap;
/**
 * This interface proposes an abstraction layer for Strategies.
 * It works together with the {@code Strategyfactory} for a 
 */
public abstract interface StrategyHandler {
    public HashMap<String, Double> ApplyStrategy(HashMap<String, Double> items, Double target);
}
