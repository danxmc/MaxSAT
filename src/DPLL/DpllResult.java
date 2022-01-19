package DPLL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DpllResult {

    private HashMap<String, Boolean> map;
    private long timeTaken;

    public DpllResult(HashMap<String, Boolean> map) {
        this.map = map;
    }

    public void setTimeTaken(long time) {
        this.timeTaken = time;
    }

    /**
     * Returns how many milliseconds the algorithm has taken
     * 
     * @return
     */
    public long getTimeTaken() {
        return timeTaken;
    }

    /**
     * Returns the DPLL result as a String
     * 
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String literal = iterator.next();
            boolean value = map.get(literal);
            sb.append(literal + " -> ");
            sb.append(value ? "1" : "0");
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public HashMap<String, Boolean> getMap() {
        return map;
    }

    public List<Integer> getList() {
        List<Integer> list = new ArrayList<Integer>();
        for (Boolean bool : map.values()) {
            list.add(bool ? 1 : 0);
        }
        return list;
    }

}
