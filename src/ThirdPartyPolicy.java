public class ThirdPartyPolicy extends InsurancePolicy {
  private String comments;
  public final static String delimitedKey = "TPP";

  public ThirdPartyPolicy(int id, Car car, int numberOfClaims, String policyHolderName, MyDate expiryDate, String comments) throws PolicyException, PolicyHolderNameException {
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

  //lab4
  @Override
  public ThirdPartyPolicy clone() throws CloneNotSupportedException {
    return (ThirdPartyPolicy) super.clone();
  }

  //lab6
  public String toDelimitedString() {
    return delimitedKey + "," + super.toDelimitedString() + "," + comments;
  }
}
