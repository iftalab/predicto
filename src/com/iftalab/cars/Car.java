package com.iftalab.cars;

public class Car {
    String make;
    String model;
    int year;
    int price;
    int odometer;
    String title;
    String regionalSpec;
    String location;

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", odometer=" + odometer +
                ", title='" + title + '\'' +
                ", regionalSpec='" + regionalSpec + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public static CarBuilder newBuilder() {
        return new CarBuilder();
    }

    public String getCSV() {
        return make+","+model+","+year+","+price+","+odometer+","+regionalSpec+","+location+","+title+"\n";
    }

    public static final class CarBuilder {
        private String make;
        private String model;
        private int year;
        private int price;
        private int odometer;
        private String regionalSpec;
        private String title;
        private String location;

        private CarBuilder() {
        }

        public CarBuilder withMake(String make) {
            this.make = make;
            return this;
        }

        public CarBuilder withModel(String model) {
            this.model = model;
            return this;
        }

        public CarBuilder withYear(int year) {
            this.year = year;
            return this;
        }

        public CarBuilder withPrice(int price) {
            this.price = price;
            return this;
        }

        public CarBuilder withOdometer(int odometer) {
            this.odometer = odometer;
            return this;
        }

        public CarBuilder withRegionalSpec(String regionalSpec) {
            this.regionalSpec = regionalSpec;
            return this;
        }

        public CarBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public CarBuilder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Car build() {
            Car car = new Car();
            car.regionalSpec = this.regionalSpec;
            car.year = this.year;
            car.price = this.price;
            car.title = this.title;
            car.model = this.model;
            car.odometer = this.odometer;
            car.make = this.make;
            car.location = this.location;
            return car;
        }
    }
}
