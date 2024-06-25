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

  public String getAdminPassword() {
    return adminPassword;
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

  public boolean addUser(String username, String password, User user) {
    if (!validateAdmin(username, password)) return false;
    if (findUser(username, password, user.getUserID()) != null) return false;
    // return users.add(user);
    return users.put(user.getUserID(), user) == null;
  }

  public boolean removeUser(String username, String password, User user) {
    if (!validateAdmin(username, password)) return false;
    return users.remove(user.getUserID(), user);
  }

  public User findUser(String username, String password, Integer userID) {
    if (!validateAdmin(username, password)) return null;
    if (userID == null) return null;
    // for (User user : users) {
    //   if (user.getUserID() == userID) return user;
    // }
    // return null;
    return users.get(userID);
  }

  public User findUser(String username, String password, String userUsername) {
    for (User user : users.values()) {
      if (user.getUsername().equals(userUsername)) return user;
    }
    return null;
  }
  
  public boolean validateUser(String username, String password, String userUsername, String userPassword) {
    if (!validateAdmin(username, password)) return false;
    User foundUser = findUser(username, password, username);
    if (foundUser == null) return false;
    return foundUser.validateUser(userUsername, userPassword);
  }

  public boolean addPolicy(String username, String password, int userID, InsurancePolicy policy) {
    if (!validateAdmin(username, password)) return false;
    User user = findUser(username, password, userID);
    if (user == null || user.findPolicy(user.getUsername(), user.getPassword(), policy.getId()) != null) return false;
    return user.addPolicy(user.getUsername(), user.getPassword(), policy);
  }

  public InsurancePolicy findPolicy(String username, String password, int userID ,int policyID) {
    if (!validateAdmin(username, password)) return null;
    User user = findUser(username, password, userID);
    if (user == null) return null;
    return user.findPolicy(user.getUsername(), user.getPassword(), policyID);
  }

  public void printPolicies(String username, String password, int userID) {
    if (!validateAdmin(username, password)) return;
    User user = findUser(username, password, userID);
    if (user == null) System.out.println("User not found!");
    else user.printPolicies(user.getUsername(), user.getPassword(), flatRate);
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

  // ASM2
  public User createUser(String username, String password, String name, String userUsername, String userPassword, Address address, HashMap<Integer, InsurancePolicy> policies, Integer userID) {
    if (!validateAdmin(username, password)) return null;
    if (findUser(username, password, userID) != null) return null;
    if (findUser(username, password, userUsername) != null) return null;
    User user = new User(name, userUsername, userPassword, address, policies, userID);
    if (addUser(username, password, user)) return user;
    return null;
  }

  public boolean createThirdPartyPolicy(String username, String password, int userID, String policyHolderName, int policyId, Car car, int numberOfClaims, MyDate expiryDate, String comments) throws PolicyException, PolicyHolderNameException {
    if (!validateAdmin(username, password)) return false;
    User user = findUser(username, password, userID);
    if (user == null) return false;
    return user.createThirdPartyPolicy(user.getUsername(), user.getPassword(), policyHolderName, policyId, car, numberOfClaims, expiryDate, comments);
  }

  public boolean createComprehensivePolicy(String username, String password, int userID, String policyHolderName, int policyId, Car car, int numberOfClaims, MyDate expiryDate, int driverAge, int level) throws PolicyException, PolicyHolderNameException {
    if (!validateAdmin(username, password)) return false;
    User user = findUser(username, password, userID);
    if (user == null) return false;
    return user.createComprehensivePolicy(user.getUsername(), user.getPassword(), policyHolderName, policyId, car, numberOfClaims, expiryDate, driverAge, level);
  }

  public double calcTotalPayments(String username, String password, int userID) {
    if (!validateAdmin(username, password)) return 0;
    User user = findUser(username, password, userID);
    if (user == null) return 0;
    return user.calcTotalPayments(user.getUsername(), user.getPassword(), flatRate);
  }

  public boolean carPriceRise(String username, String password, int userID, double risePercent) {
    if (!validateAdmin(username, password)) return false;
    User user = findUser(username, password, userID);
    if (user == null) return false;
    user.carRisePriceAll(user.getUsername(), user.getPassword(), risePercent);
    return true;
  }

  public void carPriceRise(String username, String password, double risePercent) {
    if (!validateAdmin(username, password)) return;
    for (User user : users.values()) {
      user.carRisePriceAll(user.getUsername(), user.getPassword(), risePercent);
    }
  }

  // public ArrayList<InsurancePolicy> allPolicies() {
  //   ArrayList<InsurancePolicy> result = new ArrayList<>();
  //   for (User user : users.values()) {
  //     result.addAll(user.getPolicies());
  //   }
  //   return result;
  // }

  public HashMap<Integer, InsurancePolicy> allPolicies(String username, String password) {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    if (!validateAdmin(username, password)) return result;
    for (User user : users.values()) {
      result.putAll(user.getPolicies());
    }
    return result;
  }

  // public ArrayList<InsurancePolicy> filterByCarModel(int userID, String carModel) {
  //   User user = findUser(username, password, userID);
  //   if (user == null) return new ArrayList<>();
  //   return user.filterByCarModel(carModel);
  // }

  public HashMap<Integer, InsurancePolicy> filterByCarModel(String username, String password, int userID, String carModel) {
    if (!validateAdmin(username, password)) return new HashMap<>();
    User user = findUser(username, password, userID);
    if (user == null) return new HashMap<>();
    return user.filterByCarModel(user.getUsername(), user.getPassword(), carModel);
  }

  // public ArrayList<InsurancePolicy> filterByExpiryDate(int userID, MyDate date) {
  //   User user = findUser(username, password, userID);
  //   if (user == null) return new ArrayList<>();
  //   return user.filterByExpiryDate(date);
  // }

  public HashMap<Integer, InsurancePolicy> filterByExpiryDate(String username, String password, int userID, MyDate date) {
    if (!validateAdmin(username, password)) return new HashMap<>();
    User user = findUser(username, password, userID);
    if (user == null) return new HashMap<>();
    return user.filterByExpiryDate(user.getUsername(), user.getPassword(), date);
  }

  // public ArrayList<InsurancePolicy> filterByCarModel(String carModel) {
  //   ArrayList<InsurancePolicy> result = new ArrayList<>();
  //   for (User user : users) {
  //     result.addAll(user.filterByCarModel(carModel));
  //   }
  //   return result;
  // }

  public HashMap<Integer, InsurancePolicy> filterByCarModel(String username, String password, String carModel) {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    if (!validateAdmin(username, password)) return result;
    for (User user : users.values()) {
      result.putAll(user.filterByCarModel(user.getUsername(), user.getPassword(), carModel));
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

  public HashMap<Integer, InsurancePolicy> filterByExpiryDate(String username, String password, MyDate date) {
    HashMap<Integer, InsurancePolicy> result = new HashMap<>();
    if (!validateAdmin(username, password)) return result;
    for (User user : users.values()) {
      result.putAll(user.filterByExpiryDate(user.getUsername(), user.getPassword(), date));
    }
    return result;
  }

  // Assignment 1
  public ArrayList<String> populateDistinctCityNames(String username, String password) {
    ArrayList<String> result = new ArrayList<>();
    if (!validateAdmin(username, password)) return result;
    for (User user : users.values()) {
      Address address = user.getAddress();
      if (!result.contains(address.getCity())) {
        result.add(address.getCity());
      }
    }
    return result;
  }

  public double getTotalPaymentForCity(String username, String password, String city) {
    double result = 0;
    if (!validateAdmin(username, password)) return result;
    for (User user : users.values()) {
      if (user.getAddress().getCity().equals(city)) {
        result += user.calcTotalPayments(user.getUsername(), user.getPassword(), flatRate);
      }
    }
    return result;
  }

  public ArrayList<Double> getTotalPaymentPerCity(String username, String password, ArrayList<String> cities) {
    ArrayList<Double> result = new ArrayList<>();
    if (!validateAdmin(username, password)) return result;
    for (String city : cities) {
      result.add(getTotalPaymentForCity(username, password, city));
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

  public ArrayList<String> populateDistinctCarModels(String username, String password) {
    ArrayList<String> carModels = new ArrayList<>();
    if (!validateAdmin(username, password)) return carModels;
    for (User user : users.values()) {
      for (String carModel : user.populateDistinctCarModels(user.getUsername(), user.getPassword())) {
        if (!carModel.contains(carModel)) {
          carModels.add(carModel);
        }
      }
    }
    return carModels;
  }

  public ArrayList<Integer> getTotalCountPerCarModel(String username, String password, ArrayList<String> carModels) {
    ArrayList<Integer> carModelCounts = new ArrayList<>();
    if (!validateAdmin(username, password)) return carModelCounts;
    for (String carModel : populateDistinctCarModels(username, password)) {
      int carModelCount = 0;
      for (User user : users.values()) {
        carModelCount += user.getTotalCountForCarModel(user.getUsername(), user.getPassword(), carModel);
      }
      carModelCounts.add(carModelCount);
    }
    return carModelCounts;
  }

  public ArrayList<Double> getTotalPaymentPerCarModel(String username, String password, ArrayList<String> carModels) {
    ArrayList<Double> totalPaymentPerCars = new ArrayList<>();
    if (!validateAdmin(username, password)) return totalPaymentPerCars;
    for (String carModel : populateDistinctCarModels(username, password)) {
      double carModelTotalPayment = 0;
      for (User user : users.values()) {
        carModelTotalPayment += user.getTotalPaymentForCarModel(user.getUsername(), user.getPassword(), carModel, flatRate);
      }
      totalPaymentPerCars.add(carModelTotalPayment);
    }
    return totalPaymentPerCars;
  }

  public static void reportPaymentsPerCarModel(ArrayList<String> carModels, ArrayList<Integer>counts, ArrayList<Double> premiumPayments) {
    User.reportPaymentsPerCarModel(carModels, counts, premiumPayments); 
  }

  public static void reportPaymentsPerCarModel(HashMap<String, Integer> counts, HashMap<String, Double> premiumPayments) {
    User.reportPaymentsPerCarModel(counts, premiumPayments); 
  }
  
  //lab5
  public HashMap<String, Double> getTotalPremiumPerCity(String username, String password) {
    HashMap<String, Double> totals = new HashMap<String, Double>();
    if (!validateAdmin(username, password)) return totals;
    for (User user : users.values()) {
      Double total = totals.get(user.getAddress().getCity());
      Double calculatedTotal = user.calcTotalPayments(user.getUsername(), user.getPassword(), flatRate);
      total = total == null ? calculatedTotal : total + calculatedTotal;
      totals.put(user.getAddress().getCity(), total);
    }
    return totals;
  }
  
  public HashMap<String, Integer> getTotalCountPerCarModel(String username, String password) {
    HashMap<String, Integer> counts = new HashMap<String, Integer>();
    if (!validateAdmin(username, password)) return counts;
    for (User user : users.values()) {
      HashMap<String, Integer> userCarModelCounts = user.getTotalCountForCarModel(user.getUsername(), user.getPassword());
      for (String carModel : userCarModelCounts.keySet()) {
        Integer count = counts.get(carModel);
        Integer counted = userCarModelCounts.get(carModel);
        count = count == null ? counted : count + counted;
        counts.put(carModel, count);
      }
    }
    return counts;
  }
  
  public HashMap<String, Double> getTotalPremiumPerCarModel(String username, String password) {
    HashMap<String, Double> totals = new HashMap<String, Double>();
    if (!validateAdmin(username, password)) return totals;
    for (User user : users.values()) {
      HashMap<String, Double> userCarModelTotals = user.getTotalPaymentForCarModel(user.getUsername(), user.getPassword(), flatRate);
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

  // ASM2
  public HashMap<String, ArrayList<User>> getUsersPerCity(String username, String password) {
    HashMap<String, ArrayList<User>> userCityHashMap = new HashMap<>();
    if (!validateAdmin(username, password)) return userCityHashMap;
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
        Object[] row = new Object[] { user.getUserID(), user.getName(), user.getPolicies().size(), "$" + user.calcTotalPayments(user.getUsername(), user.getPassword(), flatRate) };
        System.out.format("%-10s%-15s%10s%15s", row);
        System.out.println();
      }
      System.out.println();
    }
  }

  public HashMap<String, ArrayList<InsurancePolicy>> filterPoliciesByExpiryDate(String username, String password, MyDate expiryDate) {
    HashMap<String, ArrayList<InsurancePolicy>> userCityHashMap = new HashMap<>();
    if (!validateAdmin(username, password)) return userCityHashMap;
    for (User user : users.values()) {
      ArrayList<InsurancePolicy> expiredPolicies = new ArrayList<>(user.filterByExpiryDate(user.getUsername(), user.getPassword(), expiryDate).values());
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
  
  public ArrayList<User> shallowCopyUsers(String username, String password) {
    if (!validateAdmin(username, password)) return new ArrayList<>();
    return User.shallowCopy(users);
  }
  
  public HashMap<Integer, User> shallowCopyUsersHashMap(String username, String password) {
    if (!validateAdmin(username, password)) return new HashMap<>();
    return User.shallowCopyHashMap(users);
  }

	// public static ArrayList<User> deepCopyUsers(ArrayList<User> users) throws CloneNotSupportedException {
  //   return User.deepCopy(users);
  // }

	public ArrayList<User> deepCopyUsers(String username, String password) throws CloneNotSupportedException {
    if (!validateAdmin(username, password)) return new ArrayList<>();
    return User.deepCopy(users);
  }

	public HashMap<Integer, User> deepCopyUsersHashMap(String username, String password) throws CloneNotSupportedException {
    if (!validateAdmin(username, password)) return new HashMap<>();
    return User.deepCopyHashMap(users);
  }

  public ArrayList<User> sortUsers(String username, String password) throws CloneNotSupportedException {
    if (!validateAdmin(username, password)) return new ArrayList<>();
    ArrayList<User> shallowCopyUsers = User.shallowCopy(users);
    Collections.sort(shallowCopyUsers);
    return shallowCopyUsers;
  }

  // ASM2
  class UserTotalPaymentComparator implements Comparator<User> {
    public int compare(User firstUser, User secondUser) {
      return Double.compare(
        firstUser.calcTotalPayments(firstUser.getUsername(), firstUser.getPassword(), flatRate),
        secondUser.calcTotalPayments(secondUser.getUsername(), secondUser.getPassword(), flatRate)
      );
    }
  }
  
  public ArrayList<User> sortUsersByPremium(String username, String password) {
    if (!validateAdmin(username, password)) return new ArrayList<>();
    ArrayList<User> usersShallowCopy = User.shallowCopy(users);
    Collections.sort(usersShallowCopy, new UserTotalPaymentComparator());
    return usersShallowCopy;
  }

  //lab6
  public boolean save(String username, String password, String fileName) {
    if (!validateAdmin(username, password)) return false;
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
  
  public boolean load(String username, String password, String fileName) {
    String errorMessage = "";
    try {
      errorMessage = "Error in create/open the file!";
      ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)));
      errorMessage = "Error in reading company from file!";
      InsuranceCompany company = (InsuranceCompany) inputStream.readObject();
      if (!(
        username.equals(company.getAdminUsername()) &&
        password.equals(company.getAdminPassword())
      )) return false;
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

  public boolean saveTextFile(String username, String password, String fileName) {
    if (!validateAdmin(username, password)) return false;
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

  public boolean loadTextFile(String username, String password, String fileName) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(fileName));
      String line = in.readLine().trim();
      String[] fields = line.split(",");

      String name = fields[1];
      String adminUsername = fields[2];
      String adminPassword = fields[3];
      if (!(
        username.equals(adminUsername) &&
        password.equals(adminPassword)
      )) {
        in.close();
        return false;
      }
      int flatRate = Integer.parseInt(fields[4]);
      int numberOfUsers = Integer.parseInt(fields[5]);
      HashMap<Integer, User> extractedUsers = User.extractUsersFromFields(numberOfUsers, 6, fields);
      InsuranceCompany company = new InsuranceCompany(name, extractedUsers, adminUsername, adminPassword, flatRate);
      initialize(company);
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

  // ASM2
  // proxy pattern

  public void printUsers(String username, String password) {
    if (!validateAdmin(username, password)) return;
    User.printUsers(users);
  }

  public static void printUsers(ArrayList<User> users) {
    User.printUsers(users);
  }

  public static void printUsers(HashMap<Integer, User> users) {
    User.printUsers(users);
  }

  public void printPolicies(String username, String password) {
    if (!validateAdmin(username, password)) return;
    User.printPolicies(allPolicies(username, password));
  }

  public static void printPolicies(ArrayList<InsurancePolicy> policies) {
    User.printPolicies(policies);
  }

  public static void printPolicies(HashMap<Integer, InsurancePolicy> policies) {
    User.printPolicies(policies);
  }

  public static boolean saveUsers(HashMap<Integer, User> users, String fileName) {
    return User.save(users, fileName);
  }

  public static HashMap<Integer, User> loadUsers(String fileName) {
    return User.load(fileName);
  }

  public static boolean saveTextFileUsers(HashMap<Integer, User> users, String fileName) {
    return User.saveTextFile(users, fileName);
  }
  
  public static HashMap<Integer, User> loadTextFileUsers(String fileName) {
    return User.loadTextFile(fileName);
  }

  public static boolean savePolicies(HashMap<Integer, InsurancePolicy> policies, String fileName) {
    return User.savePolicies(policies, fileName);
  }
  
  public static HashMap<Integer, InsurancePolicy> loadPolicies(String fileName) {
    return User.loadPolicies(fileName);
  }
  
  public static boolean saveTextFilePolicies(HashMap<Integer, InsurancePolicy> policies, String fileName) {
    return User.saveTextFilePolicies(policies, fileName);
  }
  
  public static HashMap<Integer, InsurancePolicy> loadTextFilePolicies(String fileName) {
    return User.loadTextFilePolicies(fileName);
  }

  public static CarType validateCarType(String carType) throws IllegalArgumentException {
    return CarType.valueOf(carType);
  }

  public static Address createAddress(int streetNum, String street, String suburb, String city) {
    return User.createAddress(streetNum, street, suburb, city);
  }

  public static Car createCar(String model, CarType type, int manufacturingYear, double price) {
    return User.createCar(model, type, manufacturingYear, price);
  }

  public static MyDate createValidDate(int year, int month, int day) throws Exception {
    return User.createValidDate(year, month, day);
  }

  public static int generateRandomPolicyId() {
    return User.generateRandomPolicyId();
  }
  

}
