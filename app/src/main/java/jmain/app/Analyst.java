package jmain.app;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyst {
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private int projectCount = 0;

  String regex =
      "(?<lastName>\\w+),\\s*(?<firstName>\\w+),\\s*(?<dob>\\d{1,2}/\\d{1,2}/\\d{4}),\\s*(?<role>\\w+)(?:,\\s*\\{(?<details>.*)\\})?\\n";
  Pattern peoplePat = Pattern.compile(regex);

  String analystRegex = "\\w+=(?<projectCount>\\w+)";
  Pattern analystPat = Pattern.compile(analystRegex);

  private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
  private final NumberFormat nf = NumberFormat.getCurrencyInstance();

  public Analyst(String personText) {
    Matcher peopleMat = peoplePat.matcher(personText);
    if (peopleMat.find()) {
      this.firstName = peopleMat.group("firstName");
      this.lastName = peopleMat.group("lastName");
      this.dob = LocalDate.from(dtFormatter.parse(peopleMat.group("dob")));
      Matcher analystMat = analystPat.matcher(peopleMat.group("details"));
      if (analystMat.find()) {
        this.projectCount = Integer.parseInt(analystMat.group("projectCount"));
      }
    }
  }

  public int getSalary() {
    return 2500 + this.projectCount * 1000;
  }

  @Override
  public String toString() {
    return String.format(
        "%s %s, %s, %s", this.firstName, this.lastName, this.dob, nf.format(this.getSalary()));
  }
}
