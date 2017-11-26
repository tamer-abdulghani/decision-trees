/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

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

    public SingleCharacteristicTree(Characteristic profile, Characteristic target, ArrayList<Row> values) {
        this.profile = profile;
        this.target = target;
        this.profileTargetMap = new HashMap<>();
        ArrayList<Tempsfd> temp = new ArrayList<>();
        for (Row r : values) {
            Value profileValue = r.getValuesMap().get(this.profile);
            Value targetValue = r.getValuesMap().get(this.target);
            temp.add(new Tempsfd(profileValue, targetValue));
        }

        System.out.println("" + temp.size());

        List<Value> uniqueClassValues = temp.stream().map(x -> x.getSource()).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());

        int max = 0;
        Value survivedClass = null;
        Value targetClass = null;
        for (Value a : uniqueClassValues) {
            int count = 0;
            for (Tempsfd el : temp) {
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

        for (Value a : uniqueClassValues) {
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

    public class Tempsfd {

        public Value source;
        public Value target;

        private Tempsfd(Value profileValue, Value targetValue) {
            this.source = profileValue;
            this.target = targetValue;
        }

        public Value getSource() {
            return this.source;
        }
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
