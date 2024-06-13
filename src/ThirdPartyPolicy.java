public class ThirdPartyPolicy extends InsurancePolicy {
  private String comments;

  public ThirdPartyPolicy(int id, Car car, int numberOfClaims, String policyHolderName, MyDate expiryDate, String comments) throws PolicyException {
    super(id, car, numberOfClaims, policyHolderName, expiryDate);
    this.comments = comments;
  }

  public ThirdPartyPolicy(ThirdPartyPolicy policy) {
    super(policy);
    this.comments = policy.comments;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String toString() {
    return super.toString() + " Comments: " + comments;
  }

  public void print() {
    super.print();
    System.out.print(" Comments: " + comments);
  }

  @Override
  public double calcPayment(double flatRate) {
    return this.getCar().getPrice() / 100 + this.getNumberOfClaims() * 200 + flatRate;
  }
}
