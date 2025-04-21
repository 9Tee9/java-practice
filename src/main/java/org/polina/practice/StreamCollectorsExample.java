package org.polina.practice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );
        //Заказы, сгруппированные по продуктам:
        Map<String, List<Order>> groupedOrdersByProduct = orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct));


        //Общая стоимость всех заказов для продукта:
        Map<String, Double> totalCostPerProduct = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getProduct,
                        Collectors.summingDouble(Order::getCost)
                ));

        //Сортировка продуктов по убыванию общей стоимости
        List<Map.Entry<String, Double>> sortedProducts = totalCostPerProduct.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .toList();

        //3 самых дорогих продукта
        List<Map.Entry<String, Double>> top3Products = sortedProducts.stream()
                .limit(3)
                .toList();
        top3Products.forEach(entry -> {
            System.out.println("Продукт: " + entry.getKey() + ", Общая стоимость: " + entry.getValue());
        });

        }
    }
