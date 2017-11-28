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
import java.util.Random;
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
    private HashMap<Value, Value> profileTargetMap;

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

        HashMap<Value, HashMap<Value, Integer>> list = new HashMap<>();

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

        /**
         * for each profile value (pclass 1, 2, or 3), get the count of people
         * survived or died (1 or 0)
         */
        for (Value a : uniqueProfileValues) {
            HashMap<Value, Integer> targetCountList = new HashMap<>();
            for (ValuePair el : pairsList) {
                if (el.getSource().equals(a)) {
                    if (targetCountList.containsKey(el.target)) {
                        targetCountList.replace(el.target, targetCountList.get(el.target) + 1);
                    } else {
                        targetCountList.put(el.target, 1);
                    }
                }
            }

            /*
            for (Map.Entry<Value, Integer> el : targetCountList.entrySet()) {
                System.out.println("" + a + "->" + el.getKey() + ":" + el.getValue());
            }
             */
            list.put(a, targetCountList);
        }

        for (Map.Entry<Value, HashMap<Value, Integer>> ele : list.entrySet()) {

            this.profileTargetMap.put(
                    ele.getKey(),
                    ele.getValue().entrySet().stream().max((x, y) -> x.getValue() > y.getValue() ? 1 : -1).get().getKey()
            );

            // Means from the list that we build (profile , (target,count) ), give me the maximum value correspnd to specific target
            // System.out.println(ele.getKey() + ":" + ele.getValue().entrySet().stream().max((x, y) -> x.getValue() > y.getValue() ? 1 : -1).get().getValue());
        }

        for (Map.Entry<Value, Value> a : this.profileTargetMap.entrySet()) {
            System.out.println(a.getKey() + " -> " + a.getValue());
        }

    }

    @Override
    public String toString() {
        String result = "";
        result += this.profile.toString() + "\t";
        result += this.target.toString() + "\n";

        for (Map.Entry<Value, Value> el : this.getProfileTargetMap().entrySet()) {
            result += el.getKey().toString() + "\t" + el.getValue().toString() + "\n";
        }
        return result;

    }

    public Value getTargetValue(Value value) {
        if (this.getProfileTargetMap().containsKey(value)) {
            return this.getProfileTargetMap().get(value);
        }

        // This means that the value from Testing Dataset not exists in our decision tree, then return random guess.
        return this.getTarget().getPossibleValues().get(new Random().nextInt(1) + 1);
    }

    /**
     * @return the profileTargetMap
     */
    public HashMap<Value, Value> getProfileTargetMap() {
        return profileTargetMap;
    }

    /**
     * @param profileTargetMap the profileTargetMap to set
     */
    public void setProfileTargetMap(HashMap<Value, Value> profileTargetMap) {
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
