/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.helpers;

import decisiontrees.models.Value;

/**
 *
 * @author Tamer
 */
public class ValuePair {

    private Value profile;
    private Value target;

    public ValuePair(Value profileValue, Value targetValue) {
        this.profile = profileValue;
        this.target = targetValue;
    }

    /**
     * @return the target
     */
    public Value getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(Value target) {
        this.target = target;
    }

    /**
     * @return the profile
     */
    public Value getProfile() {
        return profile;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfile(Value profile) {
        this.profile = profile;
    }
}
