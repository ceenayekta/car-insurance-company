public class ComprehensivePolicy extends InsurancePolicy {
  private int driverAge;
  private int level;

  public ComprehensivePolicy(int id, Car car, int numberOfClaims, String policyHolderName, MyDate expiryDate, int driverAge, int level) throws PolicyException {
    super(id, car, numberOfClaims, policyHolderName, expiryDate);
    this.driverAge = driverAge;
    this.level = level;
  }

  public ComprehensivePolicy(ComprehensivePolicy policy) {
    super(policy);
    this.driverAge = policy.driverAge;
    this.level = policy.level;
  }

  public int getDriverAge() {
    return driverAge;
  }

  public void setDriverAge(int driverAge) {
    this.driverAge = driverAge;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String toString() {
    return super.toString() + " DriverAge: " + driverAge + " Level: " + level;
  }

  public void print() {
    super.print();
    System.out.print(" DriverAge: " + driverAge + " Level: " + level);
  }

  @Override
  public double calcPayment(double flatRate) {
    int impactfulDriverAge = 30;
    double premium = this.getCar().getPrice() / 50 + this.getNumberOfClaims() * 200 + flatRate;
    if (this.getDriverAge() < impactfulDriverAge) {
      premium += (impactfulDriverAge - this.getDriverAge()) * 50;
    }
    return premium;
  }

  //lab4
  @Override
  public ComprehensivePolicy clone() throws CloneNotSupportedException {
    return (ComprehensivePolicy) super.clone();
    
  }
}
