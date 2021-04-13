package net.wheman.melicoupon.datakeeper;

import java.sql.DriverManager;
import java.util.HashMap;

public class H2Memory {

    public void AddItemCount(String id_item){
        var dbUrl = "jdbc:h2:mem:coupondb";
        try {

            var conn = DriverManager.getConnection(dbUrl);
            var stm = conn.createStatement();
            var query = String.format("MERGE INTO FAVORITE_ITEMS USING DUAL ON ID_ITEM = '$v' WHEN NOT MATCHED THEN INSERT VALUES ($v', 1) WHEN MATCHED THEN UPDATE SET FAV_COUNT = FAV_COUNT + 1;", id_item);
            stm.execute(query);
            
        } catch (Exception e) {
        }
    }

    public HashMap<String, Long> GetTopFive(){
        HashMap<String, Long> topFiveItems = new HashMap<String, Long>();
        var dbUrl = "jdbc:h2:mem:coupondb";
        try {

            var conn = DriverManager.getConnection(dbUrl);
            var stm = conn.createStatement();
            var query = "SELECT ITEM_ID, FAV_COUNT FROM FAVORITE_ITEMS ORDER BY FAV_COUNT DESC LIMIT 5";
            var rs = stm.executeQuery(query);
            while(rs.next()){
                topFiveItems.put(rs.getString(1), rs.getLong(2));
            }
            
        } catch (Exception e) {
        }
        return topFiveItems;
    }
    
}
