import java.io.Serializable;

public class Car implements Cloneable, Serializable {
  private String model;
  private CarType type;
  private int manufacturingYear;
  private double price;

  public Car(String model, CarType type, int manufacturingYear, double price) {
    this.model = model;
    this.type = type;
    this.manufacturingYear = manufacturingYear;
    this.price = price;
  }

  public Car(Car car) {
    this.model = car.model;
    this.type = car.type;
    this.manufacturingYear = car.manufacturingYear;
    this.price = car.price;
  }

  public String getModel() {
    return model;
  }
  
  public void setModel(String model) {
    this.model = model;
  }

  public CarType getType() {
    return type;
  }

  public void setType(CarType type) {
    this.type = type;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getManufacturingYear() {
    return manufacturingYear;
  }

  public void setManufacturingYear(int manufacturingYear) {
    this.manufacturingYear = manufacturingYear;
  }

  public void print() {
    System.out.print("Model: " + model + " Type: " + type + " ManufacturingYear: " + manufacturingYear + " Price: " + price);
  }

  public String toString() {
    return "Model: " + model + " Type: " + type + " ManufacturingYear: " + manufacturingYear + " Price: " + price;
  }

  public void priceRise(double rise) {
    price += rise;
  }

  //lab4
  @Override
  public Car clone() throws CloneNotSupportedException {
    return (Car) super.clone();
  }
}