/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import decisiontrees.helpers.ValuePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Tamer
 */
public class SingleCharacteristicTree {

    private Characteristic profile;
    private Characteristic target;
    private Map<Value, Value> profileTargetMap;

    /**
     * This constructor will build a decision tree related to only one
     * characteristic
     *
     * @param profile specific characteristic to be the profile class of the
     * tree
     * @param target specific characteristic to be the target class of the tree,
     * for example: @@survived class
     *
     */
    public SingleCharacteristicTree(Characteristic profile, Characteristic target) {
        this.profile = profile;
        this.target = target;
        this.profileTargetMap = new HashMap<>();
    }

    /**
     * This method is for querying the tree for getting target value for a
     * specific profile value
     *
     * @param profileValue the value of profile characteristic
     * @return the value of the target characteristic
     */
    public Value getTargetValue(Value profileValue) {
        if (this.getProfileTargetMap().containsKey(profileValue)) {
            return this.getProfileTargetMap().get(profileValue);
        }

        // This means that the value from Testing Dataset not exists in our decision tree, then return random guess from possible target values (Survived: 1 or 0)
        return this.getTarget().getPossibleValues().get(new Random().nextInt(1) + 1);
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

    /**
     * @return the profileTargetMap
     */
    public Map<Value, Value> getProfileTargetMap() {
        return profileTargetMap;
    }

    /**
     * @param profileTargetMap the profileTargetMap to set
     */
    public void setProfileTargetMap(Map<Value, Value> profileTargetMap) {
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
