import java.io.Serializable;

class MyDate implements Cloneable, Comparable<MyDate>, Serializable {
  private int year;
  private int month;
  private int day;
    
  public MyDate(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public MyDate(MyDate date) {
    this.year = date.year;
    this.month = date.month;
    this.day = date.day;
  }

  public int getDay() {
    return day;
  }
  
  public void setDay(int day) {
    this.day = day;
  }
  
  public int getMonth() {
    return month;
  }
  public void setMonth(int month) {
    this.month = month;
  }

  public int getYear() {
    return year;
  }
  public void setYear(int year) {
    this.year = year;
  }

  public void print() {
    System.out.println(year + "/" + month + "/" + day);
  }

  public String toString() {
    return year + "/" + month + "/" + day;
  }

  //lab3
  public boolean isExpired(MyDate expiryDate) {
    if (expiryDate.year > year) return true;
    if (expiryDate.year == year && expiryDate.month > month) return true;
    if (expiryDate.year == year && expiryDate.month == month && expiryDate.day > day) return true;
    return false;
  }

  public static MyDate createValidDate(int year, int month, int day) throws Exception {
    int[] daysOfMonth = { 31, 29, 30, 31, 30, 31, 30, 31, 30, 31, 30, 31 };
    String[] nameOfMonth = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    if (year < 1 || month < 1 || day < 1) {
      throw new Exception("Negative value or zero not allowed.");
    }
    if (month > 12) {
      throw new Exception("Month should be between 1 to 12.");
    }
    if (day > daysOfMonth[month - 1]) {
      throw new Exception("Day of " + nameOfMonth[month - 1] + " should be between 1 to " + daysOfMonth[month - 1] + ".");
    }
    return new MyDate(year, month, day);
  }

  //lab4
  @Override
  public MyDate clone() throws CloneNotSupportedException {
    return (MyDate) super.clone();
  }

  @Override
  public int compareTo(MyDate d) {
    if (year > d.year) return 1;
    if (year == d.year && month > d.month) return 1;
    if (year == d.year && month == d.month && day > d.day) return 1;
    if (year == d.year && month == d.month && day == d.day) return 0;
    return -1;
  }
}