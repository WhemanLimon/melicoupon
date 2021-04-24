package net.wheman.melicoupon.strategy;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.wheman.melicoupon.AppConfiguration;

/**
 * This class implements a combination of Factory with Strategy patterns which helps decoupling calculation logic, used to select items for a coupon, 
 * from the Controller who received the input data and requires the result.
 * <p>
 * The Factory method {@code getStrategyResult} allows creating instances of different algorithm classes called "Strategies".
 * Depending on the boundaries set in the application.properties file for each "strategy" the factory will instantiate and execute the logic.
 */
@Service
public class StrategyFactory {

    private final AppConfiguration appConfiguration;

    @Autowired
    public StrategyFactory(AppConfiguration appConfiguration){
        this.appConfiguration = appConfiguration;
    }

    /**
     * It receives the input data used for the calculation and returns the selected items picked by the strategy chosen.
     * <p>
     * The strategy chosen is determined by the boundaries set in the configuration file.
     * @param items Contains the raw items to be analyzed.
     * @param target Sets the maximum value that the coupon can reach.
     * @return
     */
    public HashMap<String, Double> getStrategyResult(HashMap<String, Double> items, Double target){
        StrategyHandler strategyHandler = null;
        if(target >= appConfiguration.getKnapSackTargetFrom() && target <= appConfiguration.getKnapSackTargetTo() && items.size() < appConfiguration.getKnapSackItemsMax()){
            strategyHandler = new KnapSack();
        }else{
            strategyHandler = new TopBottom();
        }
        return strategyHandler.ApplyStrategy(items, target);
    }  
}
