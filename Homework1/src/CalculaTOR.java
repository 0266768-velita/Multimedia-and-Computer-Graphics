package Calculator;

public class CalculaTOR {

    public void rectangle(double base, double high) {
        double area = base * high;
        double perimeter = 2 * (base + high);
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void triangle(double base, double high, double side1, double side2, double side3) {
        double area = (base * high) / 2;
        double perimeter = side1 + side2 + side3;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void circle(double radius) {
        double area = Math.PI * radius * radius;
        double perimeter = 2 * Math.PI * radius;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void square(double side) {
        double area = side * side;
        double perimeter = 4 * side;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void pentagon(double side, double apothem) {
        double perimeter = 5 * side;
        double area = (perimeter * apothem) / 2;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }
}
