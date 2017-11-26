/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.util.Objects;

/**
 *
 * @author Tamer
 */
public class ValueTextual extends Value {

    private String textValue;

    public ValueTextual(String value) {
        this.textValue = value;
    }

    /**
     * @return the textValue
     */
    public String getValue() {
        return textValue;
    }

    /**
     * @param textValue the textValue to set
     */
    public void setValue(String textValue) {
        this.textValue = textValue;
    }

    public String toString() {
        return String.valueOf(this.textValue);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.textValue);
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
        final ValueTextual other = (ValueTextual) obj;
        if (!Objects.equals(this.textValue, other.textValue)) {
            return false;
        }
        return true;
    }

}
