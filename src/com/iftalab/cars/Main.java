package com.iftalab.cars;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Main {
    private static String[] makes = {
            "Shenlong-Sunlong",
            "Abarth",
            "Acura",
            "Alfa Romeo",
            "Ariel",
            "Aston Martin",
            "Audi",
            "BAC",
            "BAIC",
            "Bentley",
            "Bestune",
            "Bizzarrini",
            "BMW",
            "Borgward",
            "Brilliance",
            "Bufori",
            "Bugatti",
            "Buick",
            "BYD",
            "Cadillac",
            "Can-am",
            "Caterham",
            "CEVO",
            "Changan",
            "Chery",
            "Chevrolet",
            "Chrysler",
            "Citroen",
            "CMC",
            "Daewoo",
            "Daihatsu",
            "Datsun",
            "DeLorean",
            "DFSK",
            "Dodge",
            "DongFeng",
            "Dorcen",
            "Equus",
            "Faw",
            "Fenyr",
            "Ferrari",
            "Fiat",
            "Fisker",
            "Force",
            "Ford",
            "Foton",
            "GAC",
            "GAC Gonow",
            "Geely",
            "Genesis",
            "GMC",
            "Grand Tiger",
            "Great Wall",
            "Gumpert",
            "Haval",
            "HiPhi",
            "Honda",
            "Hongqi",
            "Hummer",
            "Hyundai",
            "Infiniti",
            "International",
            "Isuzu",
            "Iveco",
            "JAC",
            "Jaguar",
            "Jeep",
            "Jetour",
            "Jinbei",
            "JMC",
            "Kia",
            "King Long",
            "Koenigsegg",
            "KTM",
            "Lada",
            "Lamborghini",
            "Lancia",
            "Land Rover",
            "LEVC",
            "Lexus",
            "Lincoln",
            "Lotus",
            "Luxgen",
            "Mahindra",
            "Maserati",
            "Maxus",
            "Maybach",
            "Mazda",
            "McLaren",
            "Mercedes-Benz",
            "Mercedes-Maybach",
            "Mercury",
            "MG",
            "Milan",
            "MINI",
            "Mitsubishi",
            "Morgan",
            "Morris",
            "Nio",
            "Nissan",
            "Noble",
            "Opel",
            "Oullim",
            "Pagani",
            "PAL-V",
            "Peugeot",
            "PGO",
            "Polestar",
            "Pontiac",
            "Porsche",
            "Proton",
            "RAM",
            "Renault",
            "Rivian",
            "Rolls Royce",
            "Rover",
            "Saab",
            "Seat",
            "Skoda",
            "Smart",
            "Soueast",
            "Speranza",
            "Spyker",
            "Ssang Yong",
            "Subaru",
            "Suzuki",
            "TATA",
            "Tesla",
            "Toyota",
            "UAZ",
            "Victory",
            "Volkswagen",
            "Volvo",
            "W Motors",
            "Westfield Sportscars",
            "Wiesmann",
            "XPeng",
            "ZNA",
            "Zotye",
            "Mercedes-AMG",
            "Other Make",
            "Qiantu",
            "Studebaker",
            "TANK",
            "Triumph",
            "Voyah"
    };

    public static void main(String[] args) {
        try {
            getCars();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void print(int n){
        for(int i = 0 ; i <= n; i++){
            System.out.println(i);
        }
    }

    private static void getCars() throws IOException {
        int totalPage = 0;
        int totalCar = 0;
        File output = new File("CarDB.csv");
        Document doc = null;
        FileOutputStream fileOutputStream = new FileOutputStream(output);
        fileOutputStream.write("make,model,year,price,odometer,regionalSpec,location,title\n".getBytes());
        for (String make : makes) {
            for (int i = 1; i <= 400; i++) {
                totalPage++;
                System.out.print("Processing page #" + i + " for " + make + ". ");
                String url = "https://uae.dubizzle.com/motors/used-cars/" + make.replaceAll(" ", "-").toLowerCase() + "/?page=" + i;
                try {
                    doc = Jsoup.connect(url).get();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

                if(!doc.location().equals(url)){
                    break;
                }

                if (doc.text().contains("Try broadening your search using filters")) {
                    System.out.println("Completed " + make);
                    System.out.println("Total crawled " + totalPage + " page and " + totalCar + " cars");
                    break;
                }
                Elements cars = doc.select("div.cSGUbJ");
                totalCar += cars.size();
                System.out.println("Found #" + cars.size() + " cars.");
                for (Element car : cars) {
                    fileOutputStream.write(parseCar(car).getCSV().getBytes());
                }
            }
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private static Car parseCar(Element element) {
        String make = element.select("div.heading-text-1").text();
        String model = element.select("div.heading-text-2").text();
        String year = element.select("div.bbKsmq").get(0).text();
        String odometer = element.select("div.bbKsmq").get(1).text();
        String price = element.select("div.dMMLUo").text();
        String title = element.select("div.cJfjGn").text();
        String location = element.select("div.hRlQnb").text();
        return Car.newBuilder().withMake(make)
                .withModel(model)
                .withPrice(Integer.parseInt(price.replaceAll(",", "")))
                .withYear(Integer.parseInt(year))
                .withOdometer(Integer.parseInt(odometer.toLowerCase(Locale.ROOT).replaceAll("[^0-9.]", "")))
                .withTitle(title.replaceAll(",", "#"))
                .withRegionalSpec(getRegionalSpec(element))
                .withLocation(location.replaceAll(",", "#"))
                .build();
    }

    private static String getRegionalSpec(Element element) {
        if (element.text().toLowerCase().contains("gcc")) return "GCC";
        if (element.text().toLowerCase().contains("america")) return "America";
        if (element.text().toLowerCase().contains("canada")) return "Canada";
        if (element.text().toLowerCase().contains("europe")) return "Europe";
        if (element.text().toLowerCase().contains("japan")) return "Japan";
        return "Unknown";
    }
}
