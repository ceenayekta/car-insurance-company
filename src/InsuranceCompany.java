import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

//lab3
public class InsuranceCompany implements Cloneable, Serializable {
  private String name;
  // private ArrayList<User> users;
  private HashMap<Integer, User> users;
  private String adminUsername;
  private String adminPassword;
  private int flatRate;
  public final static String delimitedKey = "IC";

  public InsuranceCompany(String name, HashMap<Integer, User> users, String adminUsername, String adminPassword, int flatRate) {
    this.name = name;
    this.users = users == null ? new HashMap<>() : users;
    this.adminUsername = adminUsername;
    this.adminPassword = adminPassword;
    this.flatRate = flatRate;
  }

  public InsuranceCompany(InsuranceCompany company) {
    initialize(company);
  }

  //lab6
  public InsuranceCompany() {}

  public void initialize(InsuranceCompany company) {
    this.name = company.name;
    this.adminUsername = company.adminUsername;
    this.adminPassword = company.adminPassword;
    this.flatRate = company.flatRate; 
    // this.users = new ArrayList<>();
    // for (User user : company.users) {
    //   users.add(new User(user));
    // }
    this.users = new HashMap<>();
    for (User user : company.users.values()) {
      users.put(user.getUserID(), new User(user));
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HashMap<Integer, User> getUsers() {
    return users;
  }

  public String getAdminUsername() {
    return adminUsername;
  }

  public int getFlatRate() {
    return flatRate;
  }

  public void setFlatRate(int flatRate) {
    this.flatRate = flatRate;
  }

  public boolean validateAdmin(String username, String password) {
    return username.equals(adminUsername) && password.equals(adminPassword);
  }

  public boolean addUser(User user) {
    if (findUser(user.getUserID()) != null) return false;
    // return users.add(user);
    return users.put(user.getUserID(), user) == null;
  }

  public boolean addUser(String name, Address address) {
    User user = new User(name, address, null, null);
    return addUser(user);
  }

  public boolean removeUser(User user) {
    return users.remove(user.getUserID(), user);
  }

  public User findUser(int userID) {
    // for (User user : users) {
    //   if (user.getUserID() == userID) return user;
    // }
    // return null;
    return users.get(userID);
  }

  public boolean addPolicy(int userID, InsurancePolicy policy) {
    User user = findUser(userID);
    if (user == null || user.findPolicy(policy.getId()) != null) return false;
    return user.addPolicy(policy);
  }

  public InsurancePolicy findPolicy(int userID ,int policyID) {
    User user = findUser(userID);
    if (user == null) return null;
    return user.findPolicy(policyID);
  }

  public void printPolicies(int userID) {
    User user = findUser(userID);
    if (user == null) System.out.println("User not found!");
    else user.printPolicies(flatRate);
  }

  public void print() {
    System.out.println("Name: " + name + " FlatRate: " + flatRate + " Users: {");
    for (User user : users.values()) {
      user.print();
      System.out.println();
    }
    System.out.println("}");
  }

  public String toString() {
    String string = "Name: " + name + " FlatRate: " + flatRate + " Users: /";
    for (User user : users.values()) {
      string += user.toString() + ", ";
    }
    return string + "}";
  }

  public boolean createThirdPartyPolicy(int userID, String policyHolderName, int policyId, Car car, int numberOfClaims, MyDate expiryDate, String comments) throws PolicyException, PolicyHolderNameException {
    User user = findUser(userID);
    if (user == null) return false;
    return user.createThirdPartyPolicy(policyHolderName, policyId, car, numberOfClaims, expiryDate, comments);
  }

  public boolean createComprehensivePolicy(int userID, String policyHolderName, int policyId, Car car, int numberOfClaims, MyDate expiryDate, int driverAge, int level) throws PolicyException, PolicyHolderNameException {
    User user = findUser(userID);
    if (user == null) return false;
    return user.createComprehensivePolicy(policyHolderName, policyId, car, numberOfClaims, expiryDate, driverAge, level);
  }

  public double calcTotalPayments(int userID) {
    User user = findUser(userID);
    if (user == null) return 0;
    return user.calcTotalPayments(flatRate);
  }

  public boolean carPriceRise(int userID, double risePercent) {
    User user = findUser(userID);
    if (user == null) return false;
    user.carRisePriceAll(risePercent);
    return true;
  }

  public void carPriceRise(double risePercent) {
    for (User user : users.values()) {
      user.carRisePriceAll(risePercent);
    }
  }

  // public ArrayList<InsurancePolicy> allPolicies() {
  //   ArrayList<InsurancePolicy> result = new ArrayList<>();
  //   for (User user : users.values()) {
  //     result.addAll(user.getPolicies());
  //   }
  //   return result;
  // }

  public HashMap<Integer, InsurancePolicy> allPolicies() {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    for (User user : users.values()) {
      result.putAll(user.getPolicies());
    }
    return result;
  }

  // public ArrayList<InsurancePolicy> filterByCarModel(int userID, String carModel) {
  //   User user = findUser(userID);
  //   if (user == null) return new ArrayList<>();
  //   return user.filterByCarModel(carModel);
  // }

  public HashMap<Integer, InsurancePolicy> filterByCarModel(int userID, String carModel) {
    User user = findUser(userID);
    if (user == null) return new HashMap<>();
    return user.filterByCarModel(carModel);
  }

  // public ArrayList<InsurancePolicy> filterByExpiryDate(int userID, MyDate date) {
  //   User user = findUser(userID);
  //   if (user == null) return new ArrayList<>();
  //   return user.filterByExpiryDate(date);
  // }

  public HashMap<Integer, InsurancePolicy> filterByExpiryDate(int userID, MyDate date) {
    User user = findUser(userID);
    if (user == null) return new HashMap<>();
    return user.filterByExpiryDate(date);
  }

  // public ArrayList<InsurancePolicy> filterByCarModel(String carModel) {
  //   ArrayList<InsurancePolicy> result = new ArrayList<>();
  //   for (User user : users) {
  //     result.addAll(user.filterByCarModel(carModel));
  //   }
  //   return result;
  // }

  public HashMap<Integer, InsurancePolicy> filterByCarModel(String carModel) {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    for (User user : users.values()) {
      result.putAll(user.filterByCarModel(carModel));
    }
    return result;
  }

  // public ArrayList<InsurancePolicy> filterByExpiryDate(MyDate date) {
  //   ArrayList<InsurancePolicy> result = new ArrayList<>();
  //   for (User user : users) {
  //     result.addAll(user.filterByExpiryDate(date));
  //   }
  //   return result;
  // }

  public HashMap<Integer, InsurancePolicy> filterByExpiryDate(MyDate date) {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    for (User user : users.values()) {
      result.putAll(user.filterByExpiryDate(date));
    }
    return result;
  }

  // Assignment 1
  public ArrayList<String> populateDistinctCityNames() {
    ArrayList<String> result = new ArrayList<>();
    for (User user : users.values()) {
      Address address = user.getAddress();
      if (!result.contains(address.getCity())) {
        result.add(address.getCity());
      }
    }
    return result;
  }

  public double getTotalPaymentForCity(String city) {
    double result = 0;
    for (User user : users.values()) {
      if (user.getAddress().getCity().equals(city)) {
        result += user.calcTotalPayments(flatRate);
      }
    }
    return result;
  }

  public ArrayList<Double> getTotalPaymentPerCity(ArrayList<String> cities) {
    ArrayList<Double> result = new ArrayList<>();
    for (String city : cities) {
      result.add(getTotalPaymentForCity(city));
    }
    return result;
  }

  public static void reportPaymentPerCity(ArrayList<String> cities, ArrayList<Double> payments) {
    Object[] titles = new Object[] { "City name", "Total Premium Payment" };
    System.out.format("%-15s%25s", titles);
    for (int i = 0; i < cities.size(); i++) {
      System.out.println();
      Object[] row = new Object[] { cities.get(i), "$" + payments.get(i) };
      System.out.format("%-15s%25s", row);
    }
  }

  public boolean updateAdminPassword(String username, String password, String newPassword) {
    if (validateAdmin(username, password)) {
      adminPassword = newPassword;
      return true;
    }
    return false;
  }

  public ArrayList<String> populateDistinctCarModels() {
    ArrayList<String> carModels = new ArrayList<>();
    for (User user : users.values()) {
      for (String carModel : user.populateDistinctCarModels()) {
        if (!carModel.contains(carModel)) {
          carModels.add(carModel);
        }
      }
    }
    return carModels;
  }

  public ArrayList<Integer> getTotalCountPerCarModel(ArrayList<String> carModels) {
    ArrayList<Integer> carModelCounts = new ArrayList<>();
    for (String carModel : populateDistinctCarModels()) {
      int carModelCount = 0;
      for (User user : users.values()) {
        carModelCount += user.getTotalCountForCarModel(carModel);
      }
      carModelCounts.add(carModelCount);
    }
    return carModelCounts;
  }

  public ArrayList<Double> getTotalPaymentPerCarModel(ArrayList<String> carModels) {
    ArrayList<Double> totalPaymentPerCars = new ArrayList<>();
    for (String carModel : populateDistinctCarModels()) {
      double carModelTotalPayment = 0;
      for (User user : users.values()) {
        carModelTotalPayment += user.getTotalPaymentForCarModel(carModel, flatRate);
      }
      totalPaymentPerCars.add(carModelTotalPayment);
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
  public HashMap<String, Double> getTotalPremiumPerCity() {
    HashMap<String, Double> totals = new HashMap<String, Double>();
    for (User user : users.values()) {
      Double total = totals.get(user.getAddress().getCity());
      Double calculatedTotal = user.calcTotalPayments(flatRate);
      total = total == null ? calculatedTotal : total + calculatedTotal;
      totals.put(user.getAddress().getCity(), total);
    }
    return totals;
  }
  
  public HashMap<String, Integer> getTotalCountPerCarModel() {
    HashMap<String, Integer> counts = new HashMap<String, Integer>();
    for (User user : users.values()) {
      HashMap<String, Integer> userCarModelCounts = user.getTotalCountForCarModel();
      for (String carModel : userCarModelCounts.keySet()) {
        Integer count = counts.get(carModel);
        Integer counted = userCarModelCounts.get(carModel);
        count = count == null ? counted : count + counted;
        counts.put(carModel, count);
      }
    }
    return counts;
  }
  
  public HashMap<String, Double> getTotalPremiumPerCarModel() {
    HashMap<String, Double> totals = new HashMap<String, Double>();
    for (User user : users.values()) {
      HashMap<String, Double> userCarModelTotals = user.getTotalPaymentForCarModel(flatRate);
      for (String carModel : userCarModelTotals.keySet()) {
        Double total = totals.get(user.getAddress().getCity());
        Double calculatedTotal = userCarModelTotals.get(carModel);
        total = total == null ? calculatedTotal : total + calculatedTotal;
        totals.put(carModel, total);
      }
    }
    return totals;
  }

  public static void reportPaymentsPerCity(HashMap<String, Double> premiumPaymentsMap) {
    Object[] titles = new Object[] { "City Name", "Total Premium Payment" };
    System.out.format("%-15s%25s", titles);
    for (String city : premiumPaymentsMap.keySet()) {
      System.out.println();
      Object[] row = new Object[] { city, "$" + premiumPaymentsMap.get(city) };
      System.out.format("%-15s%25s", row);
    }
  }
  
  public static void reportPaymentsPerCarModel(HashMap<String, Integer> counts, HashMap<String, Double> premiumPayments) {
    Object[] titles = new Object[] { "Car Model", "Total Premium Payment", "Average Premium Payment" };
    System.out.format("%-15s%25s%25s", titles);
    for (String key : premiumPayments.keySet()) {
      System.out.println();
      Object[] row = new Object[] { key, "$" + premiumPayments.get(key), "$" + premiumPayments.get(key) / counts.get(key) };
      System.out.format("%-15s%25s%25s", row);
    }
  }

  // ASM2
  public HashMap<String, ArrayList<User>> getUsersPerCity() {
    HashMap<String, ArrayList<User>> userCityHashMap = new HashMap<>();
    for (User user : users.values()) {
      String city = user.getAddress().getCity();
      ArrayList<User> foundUsers = userCityHashMap.get(city);
      if (foundUsers == null) foundUsers = new ArrayList<>();
      foundUsers.add(user);
      userCityHashMap.put(city, foundUsers);
    }
    return userCityHashMap;
  }
  
  public static void reportUsersPerCity(HashMap<String, ArrayList<User>> cityUsersMap, int flatRate) {
    for (String key : cityUsersMap.keySet()) {
      int padding = (50 - key.length()) / 2;
      System.out.format("%" + padding + "s%s%" + padding + "s", "", "City: " + key, "");
      System.out.println();
      Object[] titles = new Object[] { "ID", "Name", "Policies", "Total Payment" };
      System.out.format("%-10s%-15s%10s%15s", titles);
      System.out.println();
      ArrayList<User> users = cityUsersMap.get(key);
      for (User user : users) {
        Object[] row = new Object[] { user.getUserID(), user.getName(), user.getPolicies().size(), "$" + user.calcTotalPayments(flatRate) };
        System.out.format("%-10s%-15s%10s%15s", row);
        System.out.println();
      }
      System.out.println();
    }
  }

  public HashMap<String, ArrayList<InsurancePolicy>> filterPoliciesByExpiryDate(MyDate expiryDate) {
    HashMap<String, ArrayList<InsurancePolicy>> userCityHashMap = new HashMap<>();
    for (User user : users.values()) {
      ArrayList<InsurancePolicy> expiredPolicies = new ArrayList<>(user.filterByExpiryDate(expiryDate).values());
      userCityHashMap.put(user.getName(), expiredPolicies);
    }
    return userCityHashMap;
  }
  
  public static void reportPoliciesPerUser(HashMap<String, ArrayList<InsurancePolicy>> userPoliciesMap, int flatRate) {
    for (String key : userPoliciesMap.keySet()) {
      int padding = (70 - key.length()) / 2;
      System.out.format("%" + padding + "s%s%" + padding + "s", "", "User: " + key, "");
      System.out.println();
      Object[] titles = new Object[] { "ID", "Holder Name", "No. of Claims", "Payment", "Expiry Date" };
      System.out.format("%-10s%-15s%10s%15s%20s", titles);
      System.out.println();
      ArrayList<InsurancePolicy> policies = userPoliciesMap.get(key);
      for (InsurancePolicy policy : policies) {
        Object[] row = new Object[] { policy.getId(), policy.getPolicyHolderName(), policy.getNumberOfClaims(), "$" + policy.calcPayment(flatRate), policy.getExpiryDate() };
        System.out.format("%-10s%-15s%10s%15s%20s", row);
        System.out.println();
      }
      System.out.println();
    }
  }

  //lab4
  // @Override
  // public InsuranceCompany clone() throws CloneNotSupportedException {
  //   InsuranceCompany output=(InsuranceCompany) super.clone();
  //   output.users = new ArrayList<User>();
  //   for (User user : users) {
  //     output.users.add(user.clone());
  //   }
  //   return output;
  // }

  @Override
  public InsuranceCompany clone() throws CloneNotSupportedException {
    InsuranceCompany output=(InsuranceCompany) super.clone();
    output.users = new HashMap<Integer, User>();
    for (User user : users.values()) {
      output.users.put(user.getUserID(), user.clone());
    }
    return output;
  }

  // public static ArrayList<User> shallowCopyUsers(ArrayList<User> users) {
  //   return User.shallowCopy(users);
  // }
  
  public ArrayList<User> shallowCopyUsers() {
    return User.shallowCopy(users);
  }
  
  public HashMap<Integer, User> shallowCopyUsersHashMap() {
    return User.shallowCopyHashMap(users);
  }

	// public static ArrayList<User> deepCopyUsers(ArrayList<User> users) throws CloneNotSupportedException {
  //   return User.deepCopy(users);
  // }

	public ArrayList<User> deepCopyUsers() throws CloneNotSupportedException {
    return User.deepCopy(users);
  }

	public HashMap<Integer, User> deepCopyUsersHashMap() throws CloneNotSupportedException {
    return User.deepCopyHashMap(users);
  }

  public ArrayList<User> sortUsers() throws CloneNotSupportedException {
    ArrayList<User> shallowCopyUsers = User.shallowCopy(users);
    Collections.sort(shallowCopyUsers);
    return shallowCopyUsers;
  }

  // ASM2
  class UserTotalPaymentComparator implements Comparator<User> {
    public int compare(User firstUser, User secondUser) {
      return Double.compare(firstUser.calcTotalPayments(flatRate), secondUser.calcTotalPayments(flatRate));
    }
  }
  
  public ArrayList<User> sortUsersByPremium() {
    ArrayList<User> usersShallowCopy = User.shallowCopy(users);
    Collections.sort(usersShallowCopy, new UserTotalPaymentComparator());
    return usersShallowCopy;
  }

  //lab6
  public boolean save(String fileName) {
    String errorMessage = "";
    try {
      errorMessage = "Error in create/open the file!";
      ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
      errorMessage = "Error in adding the company to the file!";
      outputStream.writeObject(this);
      errorMessage = "Error in closing the file!";
      if (outputStream != null) outputStream.close();
      errorMessage = "";
      return true;
    } catch(IOException ex) {
      System.err.println(errorMessage);
      return false;
    }
  }
  
  public boolean load(String fileName) {
    String errorMessage = "";
    try {
      errorMessage = "Error in create/open the file!";
      ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
      errorMessage = "Error in reading company from file!";
      InsuranceCompany company = (InsuranceCompany) inputStream.readObject();
      initialize(company);
      errorMessage = "Error in closing the file!";
      if (inputStream != null) inputStream.close();
      errorMessage = "";
      return true;
    } catch(IOException ex) {
      System.err.println(errorMessage);
      return false;
    } catch (ClassNotFoundException ex)  {
      System.err.println("Error in wrong class in the file.");
      return false;
    }
  }

  //lab6
  public String toDelimitedString() {
    String result = delimitedKey + "," + name + "," + adminUsername + "," + adminPassword + "," + flatRate + "," + users.size();
    for (User user : users.values()) {
      result +=  "," + user.toDelimitedString();
    }
    return result;
  }

  public boolean saveTextFile(String fileName) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
      out.write(toDelimitedString());
      out.close();
      return true;
    } catch (IOException e) {
      System.out.println(e);
      return false;
    }
  }

  public boolean loadTextFile(String fileName) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(fileName));
      String line = in.readLine();
      while (line != null) {
        line = line.trim();
        String[] fields = line.split(",");

        String name = fields[1];
        String adminUsername = fields[2];
        String adminPassword = fields[3];
        int flatRate = Integer.parseInt(fields[4]);
        int numberOfUsers = Integer.parseInt(fields[5]);
        HashMap<Integer, User> extractedUsers = User.extractUsersFromFields(numberOfUsers, 6, fields);
        InsuranceCompany company = new InsuranceCompany(name, extractedUsers, adminUsername, adminPassword, flatRate);
        initialize(company);
        line = in.readLine();
      }
      in.close();
      return true;
    } catch (IOException e) {
      System.out.println(e);
      return false;
    } catch (PolicyException e) {
      System.out.println(e);
      return false;
    } catch (PolicyHolderNameException e) {
      System.out.println(e);
      return false;
    }
  }

}
