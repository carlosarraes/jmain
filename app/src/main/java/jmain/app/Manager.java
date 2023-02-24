package jmain.app;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Manager {
  private String firstName;
  private String lastName;
  private LocalDate dob;
  private int orgSize = 0;
  private int directReports = 0;

  String regex =
      "(?<lastName>\\w+),\\s*(?<firstName>\\w+),\\s*(?<dob>\\d{1,2}/\\d{1,2}/\\d{4}),\\s*(?<role>\\w+)(?:,\\s*\\{(?<details>.*)\\})?\\n";
  Pattern peoplePat = Pattern.compile(regex);

  private final String mgrRegex = "\\w+=(?<orgSize>\\w+),\\w+=(?<dr>\\w+)";
  private final Pattern mgrPat = Pattern.compile(mgrRegex);

  private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
  private final NumberFormat nf = NumberFormat.getCurrencyInstance();

  public Manager(String personText) {
    Matcher peopleMat = peoplePat.matcher(personText);
    if (peopleMat.find()) {
      this.firstName = peopleMat.group("firstName");
      this.lastName = peopleMat.group("lastName");
      this.dob = LocalDate.from(dtFormatter.parse(peopleMat.group("dob")));
      Matcher mgrMat = mgrPat.matcher(peopleMat.group("details"));
      if (mgrMat.find()) {
        this.orgSize = Integer.parseInt(mgrMat.group("orgSize"));
        this.directReports = Integer.parseInt(mgrMat.group("dr"));
      }
    }
  }

  public int getSalary() {
    return 3500 + this.orgSize * this.directReports;
  }

  @Override
  public String toString() {
    return String.format(
        "%s %s, %s, %s", this.firstName, this.lastName, this.dob, nf.format(this.getSalary()));
  }
}
