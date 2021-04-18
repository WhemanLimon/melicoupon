package net.wheman.melicoupon.strategy;

import java.util.HashMap;

public class StrategyFactory {
    
    public static HashMap<String, Double> getStrategResult(HashMap<String, Double> items, Double target){
        StrategyHandler strategyHandler = null;
        if(target >= 0 && target <= 1000 && items.size() < 50 ){
            strategyHandler = new KnapSack();
        }else{
            strategyHandler = new TopBottom();
        }
        return strategyHandler.ApplyStrategy(items, target);
    }  
}
