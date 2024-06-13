import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class User implements Cloneable, Comparable<User>, Serializable {
  public static int idCounter = 0;
  private String name;
  private int userID;
  private Address address;
  // private ArrayList<InsurancePolicy> policies;
  private HashMap<Integer, InsurancePolicy> policies;

  public User(
    String name,
    Address address,
    HashMap<Integer, InsurancePolicy> policies
  ) {
    this.name = name;
    this.userID = idCounter;
    this.address = address;
    this.policies = policies == null ? new HashMap<Integer, InsurancePolicy>() : policies;
    idCounter++;
  }

  public User(User user) {
    this.name = user.name;
    this.userID = idCounter;
    this.address = new Address(user.address);
    this.policies = new HashMap<Integer, InsurancePolicy>();
    // for (InsurancePolicy policy : user.policies) {
    //   if (policy instanceof ComprehensivePolicy) {
    //     policies.add(new ComprehensivePolicy((ComprehensivePolicy) policy));
    //   } else if (policy instanceof ThirdPartyPolicy) {
    //     policies.add(new ThirdPartyPolicy((ThirdPartyPolicy) policy));
    //   }
    // }
    for (InsurancePolicy policy : user.policies.values()) {
      if (policy instanceof ComprehensivePolicy) {
        policies.put(policy.getId(), new ComprehensivePolicy((ComprehensivePolicy) policy));
      } else if (policy instanceof ThirdPartyPolicy) {
        policies.put(policy.getId(), new ThirdPartyPolicy((ThirdPartyPolicy) policy));
      }
    }
    idCounter++;
  }

  public int getUserID() {
    return userID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public HashMap<Integer, InsurancePolicy> getPolicies() {
    return policies;
  }

  public void print() {
    System.out.println("Name: " + name + " UserID: " + userID + " Address: {" + address + "} Policies: {");
    for (InsurancePolicy policy : policies.values()) {
      policy.print();
      System.out.println();
    }
    System.out.println("}");
  }

  public String toString() {
    String string = "Name: " + name + " UserID: " + userID + " Address: {" + address + "} Policies: {";
    for (InsurancePolicy policy : policies.values()) {
      string += policy.toString() + ", ";
    }
    return string + "}";
  }

  public void setCity(String city) {
    address.setCity(city);
  }

  public void printPolicies(int flatRate) {
    System.out.println("");
    for (InsurancePolicy policy : policies.values()) {
      policy.print();
      System.out.print("PremiumPayment: " + policy.calcPayment(flatRate));
      System.out.println();
    }
    System.out.println(", ");
  }
  
  public static void printUsers(HashMap<Integer, User> users) {
    for (User user : users.values()) {
      System.out.println(user);
    }
  }

  public boolean addPolicy(InsurancePolicy policy) {
    // return policies.add(policy);
    if (findPolicy(policy.getId()) != null) return false;
    return policies.put(policy.getId(), policy) == null;
  }

  public boolean removePolicy(InsurancePolicy policy) {
    // return policies.remove(policy);
    return policies.remove(policy.getId(), policy);
  }

  public InsurancePolicy findPolicy(int policyId) {
    // for (InsurancePolicy policy : policies) {
    //   if (policy.getId() == policyId) return policy;
    // }
    // return null;
    return policies.get(policyId);
  }

  public double calcTotalPayments(int flatRate) {
    return InsurancePolicy.calcTotalPayments(policies, flatRate);
  }

  public void carRisePriceAll(double risePercent) {
    InsurancePolicy.carPriceRiseAll(policies, risePercent);
  }

  // public ArrayList<InsurancePolicy> filterByCarModel(String carModel) {
  //   return InsurancePolicy.filterByCarModel(policies, carModel);
  // }

  public HashMap<Integer, InsurancePolicy> filterByCarModel(String carModel) {
    return InsurancePolicy.filterByCarModel(policies, carModel);
  }

  // public ArrayList<InsurancePolicy> filterByExpiryDate(MyDate date) {
  //   return InsurancePolicy.filterByExpiryDate(policies, date);
  // }

  public HashMap<Integer, InsurancePolicy> filterByExpiryDate(MyDate date) {
    return InsurancePolicy.filterByExpiryDate(policies, date);
  }

  //lab3
  public boolean createThirdPartyPolicy(String policyHolderName, int id, Car car, int numberOfClaims, MyDate expiryDate, String comments) throws PolicyException {
    if (findPolicy(id) != null) return false;
    ThirdPartyPolicy thirdPartyPolicy = new ThirdPartyPolicy(id, car, numberOfClaims, policyHolderName, expiryDate, comments);
    return addPolicy(thirdPartyPolicy);
  }

  public boolean createComprehensivePolicy(String policyHolderName, int id, Car car, int numberOfClaims, MyDate expiryDate, int driverAge, int level) throws PolicyException {
    if (findPolicy(id) != null) return false;
    ComprehensivePolicy comprehensivePolicy = new ComprehensivePolicy(id, car, numberOfClaims, policyHolderName, expiryDate, driverAge, level);
    return addPolicy(comprehensivePolicy);
  }

  // Assignment 1
  public ArrayList<String> populateDistinctCarModels() {
    ArrayList<String> result = new ArrayList<>();
    for (InsurancePolicy policy : policies.values()) {
      String model = policy.getCar().getModel();
      if (!result.contains(model)) {
        result.add(model);
      }
    }
    return result;
  }
  
  public int getTotalCountForCarModel(String carModel) {
    int count = 0;
    for (InsurancePolicy policy : policies.values()) {
      if (policy.getCar().getModel().equals(carModel)) {
        count++;
      }
    }
    return count;
  }
  
  //lab5
  public HashMap<String, Integer> getTotalCountForCarModel() {
    HashMap<String, Integer> counts = new HashMap<String, Integer>();
    for (InsurancePolicy policy : policies.values()) {
      Integer count = counts.get(policy.getCar().getModel());
      count = count == null ? 1 : count + 1;
      counts.put(policy.getCar().getModel(), count);
    }
    return counts;
  }

  public double getTotalPaymentForCarModel(String carModel, int flatRate) {
    HashMap<Integer, InsurancePolicy> filteredPolicies = InsurancePolicy.filterByCarModel(policies, carModel);
    return InsurancePolicy.calcTotalPayments(filteredPolicies, flatRate);
  }
  
  //lab5
  public HashMap<String, Double> getTotalPaymentForCarModel(int flatRate) {
    HashMap<String, Double> totals = new HashMap<String, Double>();
    for (InsurancePolicy policy : policies.values()) {
      Double total = totals.get(policy.getCar().getModel());
      Double calculatedTotal = policy.calcPayment(flatRate);
      total = total == null ? calculatedTotal : total + calculatedTotal;
      totals.put(policy.getCar().getModel(), total);
    }
    return totals;
  }

  public ArrayList<Integer> getTotalCountPerCarModel(ArrayList<String> carModels) {
    ArrayList<Integer> carModelCounts = new ArrayList<>();
    for (String carModel : populateDistinctCarModels()) {
      carModelCounts.add(getTotalCountForCarModel(carModel));
    }
    return carModelCounts;
  }

  public ArrayList<Double> getTotalPaymentPerCarModel(ArrayList<String> carModels, int flatRate) {
    ArrayList<Double> totalPaymentPerCars = new ArrayList<>();
    for (String carModel : populateDistinctCarModels()) {
      totalPaymentPerCars.add(getTotalPaymentForCarModel(carModel, flatRate));
    }
    return totalPaymentPerCars;
  }

  public static void reportPaymentsPerCarModel(ArrayList<String> carModels, ArrayList<Integer>counts, ArrayList<Double> premiumPayments) {
    Object[] titles = new Object[] { "Car Model", "Total Premium Payment", "Average Premium Payment" };
    System.out.format("%-15s%25s%25s", titles);
    for (int i = 0; i < carModels.size(); i++) {
      System.out.println();
      Object[] row = new Object[] { carModels.get(i), "$" + premiumPayments.get(i), "$" + premiumPayments.get(i) / counts.get(i) };
      System.out.format("%-15s%25s%25s", row);
    }
  }

  //lab5
  public static void reportPaymentsPerCarModel(HashMap<String, Integer> counts, HashMap<String, Double> premiumPayments) {
    Object[] titles = new Object[] { "Car Model", "Total Premium Payment", "Average Premium Payment" };
    System.out.format("%-15s%25s%25s", titles);
    for (String key : premiumPayments.keySet()) {
      System.out.println();
      Object[] row = new Object[] { key, "$" + premiumPayments.get(key), "$" + premiumPayments.get(key) / counts.get(key) };
      System.out.format("%-15s%25s%25s", row);
    }
  }

  //lab4
  @Override
  public User clone() throws CloneNotSupportedException {
    User output = (User) super.clone();
    output.address = address.clone();
    output.policies = new HashMap<Integer, InsurancePolicy>();
    for (InsurancePolicy policy : policies.values()) {
      output.policies.put(policy.getId(), policy.clone());
    }
    return output;
  }
  
  // public static ArrayList<User> shallowCopy(ArrayList<User> users) {
  //   ArrayList<User> shallowCopy = new ArrayList<User>();
  //   for (User user : users) {
  //     shallowCopy.add(user);
  //   }
  //   return shallowCopy;
  // }
  
  public static ArrayList<User> shallowCopy(HashMap<Integer, User> users) {
    ArrayList<User> shallowCopy = new ArrayList<User>();
    for (User user : users.values()) {
      shallowCopy.add(user);
    }
    return shallowCopy;
  }
  
  public static HashMap<Integer, User> shallowCopyHashMap(HashMap<Integer, User> users) {
    HashMap<Integer, User> shallowCopy = new HashMap<Integer, User>();
    for (User user : users.values()) {
      shallowCopy.put(user.userID, user);
    }
    return shallowCopy;
  }

	// public static ArrayList<User> deepCopy(ArrayList<User> users) throws CloneNotSupportedException {
  //   ArrayList<User> deepCopy = new ArrayList<User>();
  //   for (User user : users) {
  //     deepCopy.add(user.clone());
  //   }
  //   return deepCopy;
  // }

	public static ArrayList<User> deepCopy(HashMap<Integer, User> users) throws CloneNotSupportedException {
    ArrayList<User> deepCopy = new ArrayList<User>();
    for (User user : users.values()) {
      deepCopy.add(user.clone());
    }
    return deepCopy;
  }

	public static HashMap<Integer, User> deepCopyHashMap(HashMap<Integer, User> users) throws CloneNotSupportedException {
    HashMap<Integer, User> deepCopy = new HashMap<Integer, User>();
    for (User user : users.values()) {
      deepCopy.put(user.userID, user.clone());
    }
    return deepCopy;
  }
  
  public static ArrayList<InsurancePolicy> shallowCopyPolicies(HashMap<Integer, InsurancePolicy> policies) {
    return InsurancePolicy.shallowCopy(policies);
  }
  
  public static HashMap<Integer, InsurancePolicy> shallowCopyPoliciesHashMap(HashMap<Integer, InsurancePolicy> policies) {
    return InsurancePolicy.shallowCopyHashMap(policies);
  }

	public static ArrayList<InsurancePolicy> deepCopyPolicies(HashMap<Integer, InsurancePolicy> policies) throws CloneNotSupportedException {
    return InsurancePolicy.deepCopy(policies);
  }

	public static HashMap<Integer, InsurancePolicy> deepCopyPoliciesHashMap(HashMap<Integer, InsurancePolicy> policies) throws CloneNotSupportedException {
    return InsurancePolicy.deepCopyHashMap(policies);
  }
  
  @Override
  public int compareTo(User u) {
    return address.compareTo(u.address);
  }

  public int compareTo1(User u) {
    return Double.compare(calcTotalPayments(5), u.calcTotalPayments(5));
  }

  public ArrayList<InsurancePolicy> sortPoliciesByDate() throws CloneNotSupportedException {
    ArrayList<InsurancePolicy> shallowCopyPolicies = InsurancePolicy.shallowCopy(policies);
    Collections.sort(shallowCopyPolicies);
    return shallowCopyPolicies;
  }
}
