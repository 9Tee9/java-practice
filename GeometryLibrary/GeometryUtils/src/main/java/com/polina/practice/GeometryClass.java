package com.polina.practice;


import org.polina.practice.Shape;

public class GeometryClass {
    public static boolean areAreasEqual(Shape s1, Shape s2) {
        return Double.compare(s1.getArea(), s2.getArea()) == 0;
    }
}
