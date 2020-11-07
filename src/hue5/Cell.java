/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hue5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author holzm
 */
public class Cell {

    private int selectValue;
    private List<Integer> possibleValues;

    public Cell(int selectValue) {
        this.selectValue = selectValue;
        this.possibleValues = new ArrayList<>();
        possibleValues.add(selectValue);
    }

    public Cell() {
        this.selectValue = 0;
        this.possibleValues = new ArrayList<>();
        Collections.addAll(possibleValues, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    public boolean hasSelectedValue() {
        return selectValue != 0;
    }

    public int getSelectedValue() {
        return selectValue;
    }

    public void removePossibleValue(int value) {
        possibleValues.remove(Integer.valueOf(value));
    }

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }

    public boolean selectValue(int value) {
        if (possibleValues.contains(Integer.valueOf(value))) {
            selectValue = value;
            return true;
        }
        return false;
    }

    public boolean hasSinglePossibleValue() {
        return possibleValues.size() == 1;
    }

    public int getSinglePossibleValue() {
        return (hasSinglePossibleValue() ? possibleValues.get(0) : 0);
    }

    @Override
    public String toString() {
        return "Cell{" + "selectValue=" + selectValue + ", possibleValues=" + possibleValues + '}';
    }

}
