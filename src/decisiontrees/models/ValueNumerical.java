/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

/**
 *
 * @author Tamer
 */
public class ValueNumerical extends Value {

    private float numValue;

    public ValueNumerical(String value) {
        this.numValue = Float.parseFloat(value);
    }

    /**
     * @return the value
     */
    public float getValue() {
        return numValue;
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
        this.numValue = value;
    }

    public String toString() {
        return String.valueOf(this.numValue);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Float.floatToIntBits(this.numValue);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValueNumerical other = (ValueNumerical) obj;
        if (Float.floatToIntBits(this.numValue) != Float.floatToIntBits(other.numValue)) {
            return false;
        }
        return true;
    }
    
    
}
