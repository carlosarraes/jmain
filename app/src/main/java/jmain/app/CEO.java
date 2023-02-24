package jmain.app;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CEO implements Employee {
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private int stockCount = 0;

  String regex =
      "(?<lastName>\\w+),\\s*(?<firstName>\\w+),\\s*(?<dob>\\d{1,2}/\\d{1,2}/\\d{4}),\\s*(?<role>\\w+)(?:,\\s*\\{(?<details>.*)\\})?\\n";
  Pattern peoplePat = Pattern.compile(regex);

  String ceoRegex = "\\w+=(?<stockCount>\\w+)";
  Pattern ceoPat = Pattern.compile(ceoRegex);

  private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
  private final NumberFormat nf = NumberFormat.getCurrencyInstance();

  public CEO(String personText) {
    Matcher peopleMat = peoplePat.matcher(personText);
    if (peopleMat.find()) {
      this.firstName = peopleMat.group("firstName");
      this.lastName = peopleMat.group("lastName");
      this.dob = LocalDate.from(dtFormatter.parse(peopleMat.group("dob")));
      Matcher ceoMat = ceoPat.matcher(peopleMat.group("details"));
      if (ceoMat.find()) {
        this.stockCount = Integer.parseInt(ceoMat.group("stockCount"));
      }
    }
  }

  public int getSalary() {
    return 5000 + this.stockCount * 20;
  }

  @Override
  public String toString() {
    return String.format(
        "%s %s, %s, %s", this.firstName, this.lastName, this.dob, nf.format(this.getSalary()));
  }
}
