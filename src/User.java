import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class User implements Cloneable, Comparable<User>, Serializable {
  public static int idCounter = 0;
  private int userID;
  private String name;
  private String password;
  private Address address;
  // private ArrayList<InsurancePolicy> policies;
  private HashMap<Integer, InsurancePolicy> policies;
  public final static String delimitedKey = "U";

  public User(
    String name,
    String password,
    Address address,
    HashMap<Integer, InsurancePolicy> policies,
    Integer userID
  ) {
    this.name = name;
    this.password = password;
    this.userID = userID == null ? idCounter : userID;
    this.address = address;
    this.policies = policies == null ? new HashMap<Integer, InsurancePolicy>() : policies;
    if (userID == null) idCounter++;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public void setCity(String city) {
    address.setCity(city);
  }
  
  public boolean validateUser(int userID, String password) {
    return this.userID == userID && this.password.equals(password);
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

  public void printPolicies(int userID, String password, int flatRate) {
    if (!validateUser(userID, password)) return;
    System.out.println("");
    for (InsurancePolicy policy : policies.values()) {
      policy.print();
      System.out.print("PremiumPayment: " + policy.calcPayment(flatRate));
      System.out.println();
    }
    System.out.println(", ");
  }
  
  public static void printUsers(ArrayList<User> users) {
    for (User user : users) {
      System.out.println(user);
    }
  }
  
  public static void printUsers(HashMap<Integer, User> users) {
    for (User user : users.values()) {
      System.out.println(user);
    }
  }

  public boolean addPolicy(int userID, String password, InsurancePolicy policy) {
    // return policies.add(policy);
    if (!validateUser(userID, password)) return false;
    if (findPolicy(userID, password, policy.getId()) != null) return false;
    return policies.put(policy.getId(), policy) == null;
  }

  public boolean removePolicy(int userID, String password, InsurancePolicy policy) {
    if (!validateUser(userID, password)) return false;
    // return policies.remove(policy);
    return policies.remove(policy.getId(), policy);
  }

  public InsurancePolicy findPolicy(int userID, String password, int policyId) {
    if (!validateUser(userID, password)) return null;
    // for (InsurancePolicy policy : policies) {
    //   if (policy.getId() == policyId) return policy;
    // }
    // return null;
    return policies.get(policyId);
  }

  public double calcTotalPayments(int userID, String password, int flatRate) {
    if (!validateUser(userID, password)) return 0;
    return InsurancePolicy.calcTotalPayments(policies, flatRate);
  }

  public void carRisePriceAll(int userID, String password, double risePercent) {
    if (!validateUser(userID, password)) return;
    InsurancePolicy.carPriceRiseAll(policies, risePercent);
  }

  // public ArrayList<InsurancePolicy> filterByCarModel(String carModel) {
  //   return InsurancePolicy.filterByCarModel(policies, carModel);
  // }

  public HashMap<Integer, InsurancePolicy> filterByCarModel(int userID, String password, String carModel) {
    if (!validateUser(userID, password)) return new HashMap<>();
    return InsurancePolicy.filterByCarModel(policies, carModel);
  }

  // public ArrayList<InsurancePolicy> filterByExpiryDate(MyDate date) {
  //   return InsurancePolicy.filterByExpiryDate(policies, date);
  // }

  public HashMap<Integer, InsurancePolicy> filterByExpiryDate(int userID, String password, MyDate date) {
    if (!validateUser(userID, password)) return new HashMap<>();
    return InsurancePolicy.filterByExpiryDate(policies, date);
  }

  //lab3
  public boolean createThirdPartyPolicy(int userID, String password, String policyHolderName, int id, Car car, int numberOfClaims, MyDate expiryDate, String comments) throws PolicyException, PolicyHolderNameException {
    if (!validateUser(userID, password)) return false;
    if (findPolicy(userID, password, id) != null) return false;
    ThirdPartyPolicy thirdPartyPolicy = new ThirdPartyPolicy(id, car, numberOfClaims, policyHolderName, expiryDate, comments);
    return addPolicy(userID, password, thirdPartyPolicy);
  }

  public boolean createComprehensivePolicy(int userID, String password, String policyHolderName, int id, Car car, int numberOfClaims, MyDate expiryDate, int driverAge, int level) throws PolicyException, PolicyHolderNameException {
    if (!validateUser(userID, password)) return false;
    if (findPolicy(userID, password, id) != null) return false;
    ComprehensivePolicy comprehensivePolicy = new ComprehensivePolicy(id, car, numberOfClaims, policyHolderName, expiryDate, driverAge, level);
    return addPolicy(userID, password, comprehensivePolicy);
  }

  // Assignment 1
  public ArrayList<String> populateDistinctCarModels(int userID, String password) {
    if (!validateUser(userID, password)) return new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    for (InsurancePolicy policy : policies.values()) {
      String model = policy.getCar().getModel();
      if (!result.contains(model)) {
        result.add(model);
      }
    }
    return result;
  }
  
  public int getTotalCountPerCarModel(int userID, String password, String carModel) {
    int count = 0;
    if (!validateUser(userID, password)) return count;
    for (InsurancePolicy policy : policies.values()) {
      if (policy.getCar().getModel().equals(carModel)) {
        count++;
      }
    }
    return count;
  }
  
  //lab5
  public HashMap<String, Integer> getTotalCountPerCarModel(int userID, String password) {
    HashMap<String, Integer> counts = new HashMap<String, Integer>();
    if (!validateUser(userID, password)) return counts;
    for (InsurancePolicy policy : policies.values()) {
      Integer count = counts.get(policy.getCar().getModel());
      count = count == null ? 1 : count + 1;
      counts.put(policy.getCar().getModel(), count);
    }
    return counts;
  }

  public double getTotalPaymentForCarModel(int userID, String password, String carModel, int flatRate) {
    if (!validateUser(userID, password)) return 0;
    HashMap<Integer, InsurancePolicy> filteredPolicies = InsurancePolicy.filterByCarModel(policies, carModel);
    return InsurancePolicy.calcTotalPayments(filteredPolicies, flatRate);
  }
  
  //lab5
  public HashMap<String, Double> getTotalPaymentForCarModel(int userID, String password, int flatRate) {
    HashMap<String, Double> totals = new HashMap<String, Double>();
    if (!validateUser(userID, password)) return totals;
    for (InsurancePolicy policy : policies.values()) {
      Double total = totals.get(policy.getCar().getModel());
      Double calculatedTotal = policy.calcPayment(flatRate);
      total = total == null ? calculatedTotal : total + calculatedTotal;
      totals.put(policy.getCar().getModel(), total);
    }
    return totals;
  }

  public ArrayList<Integer> getTotalCountPerCarModel(int userID, String password, ArrayList<String> carModels) {
    ArrayList<Integer> carModelCounts = new ArrayList<>();
    if (!validateUser(userID, password)) return carModelCounts;
    for (String carModel : populateDistinctCarModels(userID, password)) {
      carModelCounts.add(getTotalCountPerCarModel(userID, password, carModel));
    }
    return carModelCounts;
  }

  public ArrayList<Double> getTotalPaymentPerCarModel(int userID, String password, ArrayList<String> carModels, int flatRate) {
    ArrayList<Double> totalPaymentPerCars = new ArrayList<>();
    if (!validateUser(userID, password)) return totalPaymentPerCars;
    for (String carModel : populateDistinctCarModels(userID, password)) {
      totalPaymentPerCars.add(getTotalPaymentForCarModel(userID, password, carModel, flatRate));
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
  
  public static ArrayList<User> shallowCopy(ArrayList<User> users) {
    ArrayList<User> shallowCopy = new ArrayList<User>();
    for (User user : users) {
      shallowCopy.add(user);
    }
    return shallowCopy;
  }
  
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

	public static ArrayList<User> deepCopy(ArrayList<User> users) throws CloneNotSupportedException {
    ArrayList<User> deepCopy = new ArrayList<User>();
    for (User user : users) {
      deepCopy.add(user.clone());
    }
    return deepCopy;
  }

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
  
  public ArrayList<InsurancePolicy> shallowCopyPolicies(int userID, String password) {
    if (!validateUser(userID, password)) return new ArrayList<>();
    return InsurancePolicy.shallowCopy(policies);
  }
  
  public HashMap<Integer, InsurancePolicy> shallowCopyPoliciesHashMap(int userID, String password) {
    if (!validateUser(userID, password)) return new HashMap<>();
    return InsurancePolicy.shallowCopyHashMap(policies);
  }

	public ArrayList<InsurancePolicy> deepCopyPolicies(int userID, String password) throws CloneNotSupportedException {
    if (!validateUser(userID, password)) return new ArrayList<>();
    return InsurancePolicy.deepCopy(policies);
  }

	public HashMap<Integer, InsurancePolicy> deepCopyPoliciesHashMap(int userID, String password) throws CloneNotSupportedException {
    if (!validateUser(userID, password)) return new HashMap<>();
    return InsurancePolicy.deepCopyHashMap(policies);
  }
  
  @Override
  public int compareTo(User u) {
    return address.compareTo(u.address);
  }

  // public int compareTo1(User u) {
  //   return Double.compare(calcTotalPayments(5), u.calcTotalPayments(5));
  // }

  public ArrayList<InsurancePolicy> sortPoliciesByDate(int userID, String password) throws CloneNotSupportedException {
    if (!validateUser(userID, password)) return new ArrayList<>();
    ArrayList<InsurancePolicy> shallowCopyPolicies = InsurancePolicy.shallowCopy(policies);
    Collections.sort(shallowCopyPolicies);
    return shallowCopyPolicies;
  }

  // ASM2
  private int getPaymentRangeIndex(double payment, int[] ranges) {
    for (int i = 0; i < ranges.length; i++) {
      if (payment <= ranges[i]) {
        return i;
      }
    }
    return 0;
  }

  public int[] policyCount(int userID, String password, int[] ranges, double flatRate) {
    int[] count = new int[ranges.length];
    Arrays.fill(count, 0);
    if (!validateUser(userID, password)) return count;
    for (InsurancePolicy policy : policies.values()) {
      double payment = policy.calcPayment(flatRate);
      int index = getPaymentRangeIndex(payment, ranges);
      count[index] += 1;
    }
    return count;
  }

  public HashMap<String, Integer[]> policyCarModelCount(int userID, String password, int[] ranges, double flatRate) {
    HashMap<String, Integer[]> count = new HashMap<>();
    if (!validateUser(userID, password)) return count;
    for (InsurancePolicy policy : policies.values()) {
      String carModel = policy.getCar().getModel();
      Integer[] currentCount = count.get(carModel);
      if (currentCount == null) {
        currentCount = new Integer[ranges.length];
        Arrays.fill(currentCount, 0);
      }
      double payment = policy.calcPayment(flatRate);
      int index = getPaymentRangeIndex(payment, ranges);
      currentCount[index] += 1;
      count.put(carModel, currentCount);
    }
    return count;
  }

  //lab6
  public static boolean save(HashMap<Integer, User> users, String fileName) {
    String errorMessage = "";
    try {
      errorMessage = "Error in create/open the file!";
      ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
      errorMessage = "Error in adding users to the file!";
      for (User user : users.values()) {
        outputStream.writeObject(user);
      }
      errorMessage = "Error in closing the file!";
      if (outputStream !=null) outputStream.close();
      errorMessage = "";
      return true;
    } catch(IOException ex) {
      System.err.println(errorMessage);
      return false;
    }
  }
  
  public static HashMap<Integer, User> load(String fileName) {
    String errorMessage = "";
    HashMap<Integer, User> users = new HashMap<Integer, User>();
    try {
      errorMessage = "Error in create/open the file!";
      ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
      errorMessage = "Error in reading users from file!";

      try {
        while(true) {
          User user = (User) inputStream.readObject();
          users.put(user.userID, user);
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
    return users;
  }

  public String toDelimitedString() {
    String result = delimitedKey + "," + userID + "," + name + "," + password + "," + address.toDelimitedString() + "," + policies.size();
    for (InsurancePolicy policy : policies.values()) {
      result += "," + policy.toDelimitedString();
    }
    return result;
  }

  public static boolean saveTextFile(HashMap<Integer, User> users, String fileName) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
      for (User user : users.values()) {
        out.write(user.toDelimitedString() + "\n");
      }
      out.close();
      return true;
    } catch (IOException e) {
      System.out.println(e);
      return false;
    }
  }

  public static HashMap<Integer, User> loadTextFile(String fileName) {
    HashMap<Integer, User> users = new HashMap<>();
    try {
      BufferedReader in = new BufferedReader(new FileReader(fileName));
      String line = in.readLine();
      while (line != null) {
        line = line.trim();
        String[] fields = line.split(",");
        int userID = Integer.parseInt(fields[1]);
        User extractedUser = extractUsersFromFields(1, 0, fields).get(userID);
        users.put(userID, extractedUser);
        line = in.readLine();
      }
      in.close();
    } catch (IOException e) {
      System.out.println(e);
    } catch (PolicyException e) {
      System.out.println(e);
    } catch (PolicyHolderNameException e) {
      System.out.println(e);
    }
    return users;
  }

  public static HashMap<Integer, User> extractUsersFromFields(int numberOfUsers, int startIndex, String[] fields) throws PolicyException, PolicyHolderNameException {
    HashMap<Integer, User> users = new HashMap<>();
    if (numberOfUsers == 0 || fields.length == 0) return users;
    for(int i = 0; i < numberOfUsers; i++) {
      startIndex++; // userDelimitedKey
      int userID = Integer.parseInt(fields[startIndex++]);
      String name = fields[startIndex++];
      String password = fields[startIndex++];
      startIndex++; // addressDelimitedKey
      int streetNum = Integer.parseInt(fields[startIndex++]);
      String street = fields[startIndex++];
      String suburb = fields[startIndex++];
      String city = fields[startIndex++];
      int numberOfPolicies = Integer.parseInt(fields[startIndex++]);

      Address address = new Address(streetNum, street, suburb, city);
      HashMap<Integer, InsurancePolicy> policies = InsurancePolicy.extractPoliciesFromFields(numberOfPolicies, startIndex, fields);
      User user = new User(name, password, address, policies, userID);
      users.put(userID, user);

      // evaluate startIndex for next step
      for (InsurancePolicy policy : policies.values()) {
        if (policy instanceof ComprehensivePolicy) startIndex += 15;
        if (policy instanceof ThirdPartyPolicy) startIndex += 14;
      }
    }
    return users;
  }

  // ASM2
  // proxy pattern

  public void printPolicies(int userID, String password) {
    if (!validateUser(userID, password)) return;
    InsurancePolicy.printPolicies(policies);
  }

  public static void printPolicies(ArrayList<InsurancePolicy> policies) {
    InsurancePolicy.printPolicies(policies);
  }

  public static void printPolicies(HashMap<Integer, InsurancePolicy> policies) {
    InsurancePolicy.printPolicies(policies);
  }

  public static boolean savePolicies(HashMap<Integer, InsurancePolicy> policies, String fileName) {
    return InsurancePolicy.save(policies, fileName);
  }

  public static HashMap<Integer, InsurancePolicy> loadPolicies(String fileName) {
    return InsurancePolicy.load(fileName);
  }

  public static boolean saveTextFilePolicies(HashMap<Integer, InsurancePolicy> policies, String fileName) {
    return InsurancePolicy.saveTextFile(policies, fileName);
  }

  public static HashMap<Integer, InsurancePolicy> loadTextFilePolicies(String fileName) {
    return InsurancePolicy.loadTextFile(fileName);
  }

  public static Address createAddress(int streetNum, String street, String suburb, String city) {
    return new Address(streetNum, street, suburb, city);
  }

  public static Car createCar(String model, CarType type, int manufacturingYear, double price) {
    return new Car(model, type, manufacturingYear, price);
  }

  public static MyDate createValidDate(int year, int month, int day) throws Exception {
    return MyDate.createValidDate(year, month, day);
  }

  public static int generateRandomPolicyId() {
    return InsurancePolicy.generateRandomId();
  }

}