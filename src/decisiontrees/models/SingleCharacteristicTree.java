/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import decisiontrees.helpers.ValuePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 * @author Tamer
 */
public class SingleCharacteristicTree {

    private Characteristic profile;
    private Characteristic target;
    private HashMap<Value, Integer> profileTargetMap;

    /**
     * This constructor will build a decision tree related to only one
     * characteristic
     *
     * @param profile specific characteristic to be the profile class of the
     * tree
     * @param target specific characteristic to be the target class of the tree,
     * for example: @@survived class
     * @param values list of all rows representing the dataset values.
     *
     */
    public SingleCharacteristicTree(Characteristic profile, Characteristic target, ArrayList<Row> values) {
        this.profile = profile;
        this.target = target;
        this.profileTargetMap = new HashMap<>();

        ArrayList<ValuePair> pairsList = new ArrayList<>();
        for (Row r : values) {
            Value profileValue = r.getValuesMap().get(profile);
            Value targetValue = r.getValuesMap().get(target);
            pairsList.add(new ValuePair(profileValue, targetValue));
        }

        /**
         * Get the distinct values from the profile, for example: (male, female)
         * Or (1,2,3)
         */
        List<Value> uniqueProfileValues = pairsList.stream().map(x -> x.getSource()).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());

        int max = 0;
        Value survivedClass = null;
        Value targetClass = null;

        /**
         * for each profile value, get the count of survived = 1
         */
        for (Value a : uniqueProfileValues) {
            int count = 0;
            for (ValuePair el : pairsList) {
                if (el.getSource().equals(a)) {
                    count++;
                }
            }
            System.out.println("" + a.toString() + ":" + count);
            if (count > max) {
                max = count;
                survivedClass = a;
            }
        }

        for (Value a : uniqueProfileValues) {
            if (a == survivedClass) {
                this.profileTargetMap.put(a, 1);
            } else {
                this.profileTargetMap.put(a, 0);
            }
        }

    }

    @Override
    public String toString() {
        String result = "";
        result += this.profile.toString() + "\t";
        result += this.target.toString() + "\n";

        for (Map.Entry<Value, Integer> el : this.getProfileTargetMap().entrySet()) {
            result += el.getKey().toString() + "\t" + el.getValue().toString() + "\n";
        }
        return result;

    }

    public Integer getTargetValue(Value value) {
        if (this.getProfileTargetMap().containsKey(value)) {
            return this.getProfileTargetMap().get(value);
        }
        return null;
    }

    /**
     * @return the profileTargetMap
     */
    public HashMap<Value, Integer> getProfileTargetMap() {
        return profileTargetMap;
    }

    /**
     * @param profileTargetMap the profileTargetMap to set
     */
    public void setProfileTargetMap(HashMap<Value, Integer> profileTargetMap) {
        this.profileTargetMap = profileTargetMap;
    }

    /**
     * @return the profile
     */
    public Characteristic getProfile() {
        return profile;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfile(Characteristic profile) {
        this.profile = profile;
    }

    /**
     * @return the target
     */
    public Characteristic getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(Characteristic target) {
        this.target = target;
    }
}
