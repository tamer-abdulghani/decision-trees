/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Tamer
 */
public class Characteristic {

    private int id;
    private String name;
    private CharacteristicType type;
    private List<Value> possibleValues;
    private boolean isCategorical;

    public Characteristic(int id, String nameChar, CharacteristicType type) {
        this.id = id;
        this.name = nameChar;
        this.type = type;
        this.possibleValues = new ArrayList<Value>();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public CharacteristicType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(CharacteristicType type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.type);
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
        final Characteristic other = (Characteristic) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    /**
     * @return the possibleValues
     */
    public List<Value> getPossibleValues() {
        return possibleValues;
    }

    /**
     * @param possibleValues the possibleValues to set
     */
    public void setPossibleValues(List<Value> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public void setCategorical(boolean b) {
        this.isCategorical = b;
    }

    public boolean getCategorical() {
        return this.isCategorical;
    }

}
