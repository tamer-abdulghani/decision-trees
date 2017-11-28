/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tamer
 */
public class Row {

    private int rowId;
    private HashMap<Characteristic, Value> valuesMap;

    public Row(int rowId) {
        this.rowId = rowId;
        this.valuesMap = new HashMap<>();
    }

    /**
     * @return the rowId
     */
    public int getRowId() {
        return rowId;
    }

    /**
     * @param rowId the rowId to set
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    /**
     * @return the valuesMap
     */
    public HashMap<Characteristic, Value> getValuesMap() {
        return valuesMap;
    }

    /**
     * @param valuesMap the valuesMap to set
     */
    public void setValuesMap(HashMap<Characteristic, Value> valuesMap) {
        this.valuesMap = valuesMap;
    }

    public void insertNewValue(Characteristic c, String value) {
        if (!this.valuesMap.containsKey(c)) {
            switch (c.getType()) {
                case Boolean: {
                    this.valuesMap.put(c, new ValueBoolean(value));
                    break;
                }
                case Numerical: {
                    this.valuesMap.put(c, new ValueNumerical(value));
                    break;
                }
                case Textual: {
                    this.valuesMap.put(c, new ValueTextual(value));
                    break;
                }
            }
        }
    }

    public Value getTargetValue() {
        for (Map.Entry<Characteristic, Value> entry : valuesMap.entrySet()) {
            Characteristic key = entry.getKey();
            Value value = entry.getValue();
            if (key.getName().toLowerCase().equals("survived")) {
                return value;
            }
        }
        return null;
    }

}
