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

    public Value source;
    public Value target;

    public ValuePair(Value profileValue, Value targetValue) {
        this.source = profileValue;
        this.target = targetValue;
    }

    public Value getSource() {
        return this.source;
    }
}
