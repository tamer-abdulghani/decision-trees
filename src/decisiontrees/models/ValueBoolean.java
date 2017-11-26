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
public class ValueBoolean extends Value {

    private boolean value;

    public ValueBoolean(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    /**
     * @return the value
     */
    public boolean getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.value ? 1 : 0);
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
        final ValueBoolean other = (ValueBoolean) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
    
    
}
