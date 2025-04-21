package com.polina.practice;

import org.polina.practice.Circle;
import org.polina.practice.Rectangle;
import org.polina.practice.Triangle;

public class Main {
    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle(12, 14);
        Rectangle rectangle2 = new Rectangle(8, 13);
        Triangle triangle = new Triangle(10, 15, 35, 35, 35);
        Circle circle = new Circle(20);
        System.out.println("Площадь прямоугольника: " + rectangle.getArea() + "\n" +
                "Периметр прямоугольника: " + rectangle.getPerimeter());
        System.out.println("Площадь треугольника: " + triangle.getArea() + "\n" +
                "Периметр треугольника: " + triangle.getPerimeter());
        System.out.println("Площадь круга: " + circle.getArea() + "\n" +
                "Периметр круга: " + circle.getPerimeter());
        System.out.println(GeometryClass.areAreasEqual(rectangle, rectangle2));
        Cube cube = new Cube(23);
        System.out.println(cube.getSurfaceArea());
        System.out.println(cube.getVolume());
    }
}