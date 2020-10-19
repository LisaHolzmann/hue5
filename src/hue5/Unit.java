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
public class Unit {

    private List<Cell> cells;

    public Unit(List<Cell> cells) {
        this.cells = cells;
    }

    public void reducePossibleValues() {
    }

    public boolean isCorrect() {
    }

    public boolean tryToSelectValue() {
    }

    @Override
    public String toString() {
        return "Unit{" + "cells=" + cells + '}';
    }

}
