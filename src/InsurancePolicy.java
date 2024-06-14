import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class InsurancePolicy implements Cloneable, Comparable<InsurancePolicy>, Serializable {
  protected int id;
  protected Car car;
  protected int numberOfClaims;
  protected String policyHolderName;
  protected MyDate expiryDate;
  private static int[] idRange = { 300000, 400000 };

  public InsurancePolicy(int id, Car car, int numberOfClaims, String policyHolderName, MyDate expiryDate) throws PolicyException {
    this.id = id;
    this.car = car;
    this.numberOfClaims = numberOfClaims;
    this.policyHolderName = policyHolderName;
    this.expiryDate = expiryDate;
    if (id < idRange[0] || id > idRange[1]) {
      this.id = generateRandomId();
      throw new PolicyException(this.id);
    }
  }

  public InsurancePolicy(InsurancePolicy policy) {
    this.id = generateRandomId();
    this.car = policy.car;
    this.numberOfClaims = policy.numberOfClaims;
    this.policyHolderName = policy.policyHolderName;
    this.expiryDate = policy.expiryDate;
  }

  public int getId() {
    return id;
  }

  public Car getCar() {
    return car;
  }
  
  public void setCar(Car car) {
    this.car = car;
  }

  public int getNumberOfClaims() {
    return numberOfClaims;
  }

  public void setNumberOfClaims(int numberOfClaims) {
    this.numberOfClaims = numberOfClaims;
  }

  public String getPolicyHolderName() {
    return policyHolderName;
  }

  public void setPolicyHolderName(String policyHolderName) {
    this.policyHolderName = policyHolderName;
  }

  public MyDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(MyDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  public void print() {
    System.out.print("ID: " + id + " Car: {");
    car.print();
    System.out.print("} NumberOfClaims: " + numberOfClaims + " PolicyHolderName: " + policyHolderName + " ExpiryDate: " + expiryDate);
  }

  public String toString() {
    return "ID: " + id + " Car: {" + car + "} NumberOfClaims: " + numberOfClaims + " PolicyHolderName: " + policyHolderName + " ExpiryDate: " + expiryDate;
  }

  abstract double calcPayment(double flatRate);

  public void carPriceRise(double risePercent) {
    car.priceRise(car.getPrice()*(1+risePercent));
  }

  public void setCarModel(String model) {
    car.setModel(model);
  }
  
  public static void printPolicies(ArrayList<InsurancePolicy> policies) {
    for (InsurancePolicy policy : policies) {
      System.out.println(policy);
    }
  }
  
  //lab5
  public static void printPolicies(HashMap<Integer, InsurancePolicy> policies) {
    for (InsurancePolicy policy : policies.values()) {
      System.out.println(policy);
    }
  }

  public static double calcTotalPayments(ArrayList<InsurancePolicy> policies, int flatRate) {
    double totalPayment = 0;
    for (InsurancePolicy policy : policies) {
        totalPayment += policy.calcPayment(flatRate);
    }
    return totalPayment;
  }

  //lab5
  public static double calcTotalPayments(HashMap<Integer, InsurancePolicy> policies, int flatRate) {
    double totalPayment = 0;
    for (InsurancePolicy policy : policies.values()) {
        totalPayment += policy.calcPayment(flatRate);
    }
    return totalPayment;
  }

  public static void carPriceRiseAll(ArrayList<InsurancePolicy> policies, double flatRate) {
    for (InsurancePolicy policy : policies) {
      policy.carPriceRise(flatRate);
    }
  }
  
  //lab5
  public static void carPriceRiseAll(HashMap<Integer, InsurancePolicy> policies, double flatRate) {
    for (InsurancePolicy policy : policies.values()) {
      policy.carPriceRise(flatRate);
    }
  }

  public static ArrayList<InsurancePolicy> filterByCarModel(ArrayList<InsurancePolicy> policies, String model) {
    ArrayList<InsurancePolicy> result = new ArrayList<>();
    for (InsurancePolicy policy : policies) {
      if (policy.getCar().getModel().contains(model)) {
        result.add(policy);
      }
    }
    return result;
  }
  
  //lab5
  public static HashMap<Integer, InsurancePolicy> filterByCarModel(HashMap<Integer, InsurancePolicy> policies, String model) {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    for (InsurancePolicy policy : policies.values()) {
      if (policy.getCar().getModel().contains(model)) {
        result.put(policy.id, policy);
      }
    }
    return result;
  }

  //lab3
  public static ArrayList<InsurancePolicy> filterByExpiryDate(ArrayList<InsurancePolicy> policies, MyDate date) {
    ArrayList<InsurancePolicy> result = new ArrayList<>();
    for (InsurancePolicy policy : policies) {
      if (policy.getExpiryDate().isExpired(date)) {
        result.add(policy);
      }
    }
    return result;
  }
  
  //lab5
  public static HashMap<Integer, InsurancePolicy> filterByExpiryDate(HashMap<Integer, InsurancePolicy> policies, MyDate date) {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    for (InsurancePolicy policy : policies.values()) {
      if (policy.getExpiryDate().isExpired(date)) {
        result.put(policy.id, policy);
      }
    }
    return result;
  }

  //lab4
  @Override
  protected InsurancePolicy clone() throws CloneNotSupportedException {
    InsurancePolicy output = (InsurancePolicy) super.clone();
    output.expiryDate = expiryDate.clone();
    output.car = car.clone();
    return output;
  }
  
  public static ArrayList<InsurancePolicy> shallowCopy(ArrayList<InsurancePolicy> policies) {
    ArrayList<InsurancePolicy> shallowCopy = new ArrayList<InsurancePolicy>();
    for (InsurancePolicy policy : policies) {
      shallowCopy.add(policy);
    }
    return shallowCopy;
  }

  //lab5
  public static ArrayList<InsurancePolicy> shallowCopy(HashMap<Integer, InsurancePolicy> policies) {
    ArrayList<InsurancePolicy> shallowCopy = new ArrayList<InsurancePolicy>();
    for (InsurancePolicy policy : policies.values()) {
      shallowCopy.add(policy);
    }
    return shallowCopy;
  }
  
  //lab5
  public static HashMap<Integer, InsurancePolicy> shallowCopyHashMap(HashMap<Integer, InsurancePolicy> policies) {
    HashMap<Integer, InsurancePolicy> shallowCopy = new HashMap<Integer, InsurancePolicy>();
    for (InsurancePolicy policy : policies.values()) {
      shallowCopy.put(policy.id, policy);
    }
    return shallowCopy;
  }

	public static ArrayList<InsurancePolicy> deepCopy(ArrayList<InsurancePolicy> policies) throws CloneNotSupportedException {
    ArrayList<InsurancePolicy> deepCopy = new ArrayList<InsurancePolicy>();
    for (InsurancePolicy policy : policies) {
      deepCopy.add(policy.clone());
    }
    return deepCopy;
  }

  //lab5
	public static ArrayList<InsurancePolicy> deepCopy(HashMap<Integer, InsurancePolicy> policies) throws CloneNotSupportedException {
    ArrayList<InsurancePolicy> deepCopy = new ArrayList<InsurancePolicy>();
    for (InsurancePolicy policy : policies.values()) {
      deepCopy.add(policy.clone());
    }
    return deepCopy;
  }
  
  //lab5
	public static HashMap<Integer, InsurancePolicy> deepCopyHashMap(HashMap<Integer, InsurancePolicy> policies) throws CloneNotSupportedException {
    HashMap<Integer, InsurancePolicy> deepCopy = new HashMap<Integer, InsurancePolicy>();
    for (InsurancePolicy policy : policies.values()) {
      deepCopy.put(policy.id, policy.clone());
    }
    return deepCopy;
  }

  static public int generateRandomId() {
    return (int) ((Math.random() * (idRange[1] - idRange[0])) + idRange[0]);
  }

  @Override
  public int compareTo(InsurancePolicy ip) {
    return expiryDate.compareTo(ip.expiryDate);
  }

  //lab6
  public static boolean save(HashMap<Integer, InsurancePolicy> policies, String fileName) {
    String errorMessage = "";
    try {
      errorMessage = "Error in create/open the file!";
      ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
      errorMessage = "Error in adding policies to the file!";
      for (InsurancePolicy policy : policies.values()) {
        outputStream.writeObject(policy);
      }
      errorMessage = "Error in closing the file!";
      if (outputStream != null) outputStream.close();
      errorMessage = "";
      return true;
    } catch(IOException ex) {
      System.err.println(errorMessage);
    }
    return false;
  }
  
  public static HashMap<Integer, InsurancePolicy> load(String fileName) {
    String errorMessage = "";
    HashMap<Integer, InsurancePolicy> policies = new HashMap<Integer, InsurancePolicy>();
    try {
      errorMessage = "Error in create/open the file!";
      ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
      errorMessage = "Error in reading policies from file!";

      try {
        while(true) {
          InsurancePolicy policy = (InsurancePolicy) inputStream.readObject();
          policies.put(policy.id, policy);
        }
      } catch(EOFException ex) {
        System.out.println("End of " + fileName + ".");
      }

      errorMessage = "Error in closing the file!";
      if (inputStream != null) inputStream.close();
      errorMessage = "";
    } catch(IOException ex) {
      System.err.println(errorMessage);
    } catch (ClassNotFoundException ex)  {
      System.err.println("Error in wrong class in the file.");
    }
    return policies;
  }
}

class PolicyException extends Exception
{
    int ID;
    public PolicyException(int ID) {
      this.ID = ID;
    }
    public String toString() {
      return "The Policy ID was not valid and a new ID (the generated ID) is generated by admin and assigned for the Policy";
    }
}