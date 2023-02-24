package jmain.app;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
  public static void main(String[] args) {
    String people =
        """
          Flinstone, Fred, 1/1/1900, Programmer, {locpd=2000,yoe=10,iq=140}
          Flinstone, Fred, 1/1/1900, Programmer, {locpd=1300,yoe=14,iq=100}
          Flinstone, Fred, 1/1/1900, Programmer, {locpd=2300,yoe=8,iq=105}
          Flinstone, Fred, 1/1/1900, Programmer, {locpd=1630,yoe=3,iq=115}
          Flinstone, Fred, 1/1/1900, Programmer, {locpd=5,yoe=10,iq=100}
          Rubble, Barney, 2/2/1905, Manager, {orgSize=300,dr=10}
          Rubble, Barney, 2/2/1905, Manager, {orgSize=100,dr=4}
          Rubble, Barney, 2/2/1905, Manager, {orgSize=200,dr=2}
          Rubble, Barney, 2/2/1905, Manager, {orgSize=500,dr=8}
          Rubble, Barney, 2/2/1905, Manager, {orgSize=175,dr=20}
          Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=3}
          Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=4}
          Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=5}
          Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=6}
          Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=9}
          Rubble, Betty, 4/4/1915, CEO, {avgStockPrice=300}
        """;

    String regex =
        "(?<lastName>\\w+),\\s*(?<firstName>\\w+),\\s*(?<dob>\\d{1,2}/\\d{1,2}/\\d{4}),\\s*(?<role>\\w+)(?:,\\s*\\{(?<details>.*)\\})?\\n";
    Pattern peoplePat = Pattern.compile(regex);
    Matcher peopleMat = peoplePat.matcher(people);

    String analystRegex = "\\w+=(?<projectCount>\\w+)";
    Pattern analystPat = Pattern.compile(analystRegex);

    String ceoRegex = "\\w+=(?<avgStockPrice>\\w+)";
    Pattern ceoPat = Pattern.compile(ceoRegex);

    int totalSalaries = 0;
    while (peopleMat.find()) {
      totalSalaries +=
          switch (peopleMat.group("role")) {
            case "Programmer" -> {
              Programmer programmer = new Programmer(peopleMat.group());
              System.out.println(programmer.toString());
              yield programmer.getSalary();
            }
            case "Manager" -> {
              Manager manager = new Manager(peopleMat.group());
              System.out.println(manager.toString());
              yield manager.getSalary();
            }
            case "Analyst" -> {
              Analyst analyst = new Analyst(peopleMat.group());
              System.out.println(analyst.toString());
              yield analyst.getSalary();
            }
            case "CEO" -> {
              Matcher ceoMat = ceoPat.matcher(peopleMat.group("details"));
              int salary = 5000;
              if (ceoMat.find()) {
                int avgStockPrice = Integer.parseInt(ceoMat.group("avgStockPrice"));
                salary = 5000 + avgStockPrice * 100;
              } else {
                System.out.println("No match");
              }
              yield salary;
            }
            default -> {
              yield 0;
            }
          };
    }

    NumberFormat nf = NumberFormat.getCurrencyInstance();
    System.out.println("Total salaries: " + nf.format(totalSalaries));
  }
}
