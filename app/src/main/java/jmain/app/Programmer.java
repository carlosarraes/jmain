package jmain.app;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programmer implements Employee {
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private int linesOfCode = 0;
  private int yearsofExp = 0;
  private int iq = 0;

  private final String regex =
      "(?<lastName>\\w+),\\s*(?<firstName>\\w+),\\s*(?<dob>\\d{1,2}/\\d{1,2}/\\d{4}),\\s*(?<role>\\w+)(?:,\\s*\\{(?<details>.*)\\})?\\n";
  private final Pattern peoplePat = Pattern.compile(regex);

  private final String progRegex = "\\w+=(?<locpd>\\w+),\\w+=(?<yoe>\\w+),\\w+=(?<iq>\\w+)";
  private final Pattern progPat = Pattern.compile(progRegex);

  private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
  private final NumberFormat nf = NumberFormat.getCurrencyInstance();

  public Programmer(String personText) {
    Matcher peopleMat = peoplePat.matcher(personText);
    if (peopleMat.find()) {
      this.firstName = peopleMat.group("firstName");
      this.lastName = peopleMat.group("lastName");
      this.dob = LocalDate.from(dtFormatter.parse(peopleMat.group("dob")));
      Matcher progMat = progPat.matcher(peopleMat.group("details"));
      if (progMat.find()) {
        this.linesOfCode = Integer.parseInt(progMat.group("locpd"));
        this.yearsofExp = Integer.parseInt(progMat.group("yoe"));
        this.iq = Integer.parseInt(progMat.group("iq"));
      }
    }
  }

  public int getSalary() {
    return 3000 + this.linesOfCode * this.yearsofExp * this.iq / 1000;
  }

  @Override
  public String toString() {
    return String.format(
        "%s %s, %s, %s", this.firstName, this.lastName, this.dob, nf.format(this.getSalary()));
  }
}
