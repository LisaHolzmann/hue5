/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hue5;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author holzm
 */
public class Unit {

    private List<Cell> cells;

    public Unit(List<Cell> cells) {
        this.cells = cells;
    }

    public void reducePossibleValues() {
        for (Cell reducingCell : cells) {
            for (Cell checkedCell : cells) {
                if (checkedCell.hasSelectedValue() && checkedCell != reducingCell) {
                    reducingCell.removePossibleValue(checkedCell.getSelectedValue());
                }
            }
        }
    }

    public boolean isCorrect() {
        Set<Integer> numbers = new HashSet<>();
        for (Cell cell : cells) {
            if (cell.hasSelectedValue()) {
                if (!numbers.add(cell.getSelectedValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean tryToSelectValue() {
        boolean changed = false;

        for (Cell cell : cells) {
            if (cell.hasSinglePossibleValue() && !cell.hasSelectedValue()) {
                cell.selectValue(cell.getSinglePossibleValue());
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public String toString() {
        return "Unit{" + "cells=" + cells + '}';
    }

}
