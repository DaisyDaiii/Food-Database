package project.model.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mode class that includes the mode actions
 */
public abstract class Mode {
    private String name;
    private int number;

    public Mode(String name, int number){
        this.name = name;
        this.number = number;
    }

    /**
     * get name of the mode
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get the number of meals of mdoe
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * set the number of meals
     * @param number
     */
    public void setNumber(int number){
        this.number = number;
    }

    /**
     * update the quantity
     * @param num the original quantity
     * @return
     */
    public double quantity(double num) {
        return number*num;
    }

    /**
     * update the nutrition
     * @param nur the original nutrition
     * @return
     */
    public List<String> nutrition(List<String> nur){
        List<String> newList = new ArrayList<>();
        for(String s : nur){
            String[] array = s.split(" ");
            newList.add(array[0]+" "+Double.parseDouble(array[1])*number+" "+array[2]);
        }
        return newList;
    }

    /**
     * update the maximums
     * @param max the original maximums
     * @return
     */
    public Map<String, Integer> maximums(Map<String, Integer> max){
        Map<String, Integer> newMap = new HashMap<>();
        for(String key : max.keySet()){
            newMap.put(key, max.get(key)*number);
        }
        return newMap;
    }
}
