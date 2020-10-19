/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hue5;

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
    }

    public Cell() {
    }

    public boolean hasSelectedValue() {
    }

    public int getSelectedValue() {
    }

    public void removePossibleValue(int value) {
    }

    public List<Integer> getPossibleValues() {
    }

    public boolean selectValue(int value) {
    }

    public boolean hasSinglePossibleValue() {
    }

    public int getSinglePossibleValue() {
    }

    @Override
    public String toString() {
        return "Cell{" + "selectValue=" + selectValue + ", possibleValues=" + possibleValues + '}';
    }

}
