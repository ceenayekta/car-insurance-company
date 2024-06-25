import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
  private int activeMenu = 0;
  private int passedCount = 0;
  private int failedCount = 0;
  private final String RED = "\u001B[31m";
  private final String GREEN = "\u001B[32m";
  private final String DEFAULT = "\u001B[0m";
  private String[] menus = { "Main Menu", "Admin Menu", "User Menu" };
  private User account;
  private Scanner inputReader = new Scanner(System.in);
  private InsuranceCompany mainCompany;
  private boolean adminLoggedIn = false;

  public UserInterface(InsuranceCompany company) {
    mainCompany = company;        
  }
  
//main Menu
  public void mainMenu() {
    activeMenu = 1;
    List<String> options = Arrays.asList(
      "1. Admin Login",
      "2. User Login",
      "3. Exit"
    );
    List<Runnable> actions = Arrays.asList(
      () -> {
        if (login(true)) {
          adminMenu();
          activeMenu = 2;
        }
      },
      () -> {
        if (login(false)) {
          userMenu();
          activeMenu = 3;
        }
      },
      () -> { activeMenu = 0; }
    );
    while(activeMenu > 0) {
      menuHandler(options, actions);
    }
  }

  public boolean login(boolean isAdmin) {
    System.out.print("Username: ");
    String username = inputReader.nextLine();
    System.out.print("Password: ");
    String password = inputReader.nextLine();
    if (isAdmin && mainCompany.validateAdmin(username, password)) {
      System.out.println("Welcome dear Admin!");
      adminLoggedIn = true;
      return true;
    } else if (mainCompany.validateUser(getAdminUsername(), getAdminPassword(), username, password)) {
      account = mainCompany.findUser(getAdminUsername(), getAdminPassword(), username);
      System.out.println("Logged in as " + account.getName() + " successfully!");
      return true;
    } else {
      System.out.println("\nWrong username or password.\n");
      return false;
    }
  }
 
//*************************************************************************************
//Admin menu
  public void adminMenu() {
    if (!adminLoggedIn) return;
    activeMenu = 2;
    List<String> options = Arrays.asList(
      "1.  Test Code",
      "2.  Create User",
      "3.  Create Third-Party Policy",
      "4.  Create Comprehensive Policy",
      "5.  Print User Information",
      "6.  Sort Users by City",
      "7.  Sort Users by Total Payment",
      "8.  Filter Policies by Car Model",
      "9.  Filter Policies by Expiry Date",
      "10. Update a User's Address",
      "11. Report Total Payment of User per Car",
      "12. Report Total Payment per City",
      "13. Report Payments per Car Model",
      "14. Report Users per City",
      "15. Report Policies per Users",
      "16. Remove a Policy",
      "17. Remove a User",
      "18. Change Password",
      "19. Save Company in TextFile",
      "20. Load Company from TextFile",
      "21. Save Company in Binary",
      "22. Load Company from Binary",
      "23. Logout"
    );
    List<Runnable> actions = Arrays.asList(
      () -> testCode(),
      () -> createUser(),
      () -> createThirdPartyPolicy(null),
      () -> createComprehensivePolicy(null),
      () -> printUserInformation(),
      () -> printSortedUsersByCity(),
      () -> printSortedUsersByTotalPayment(),
      () -> filterByCarModel(),
      () -> filterByExpiryDate(null),
      () -> updateAddress(null),
      () -> userTotalPaymentPerCarModelAggregation(),
      () -> totalPaymentPerCityAggregation(),
      () -> totalPaymentPerCarModelAggregation(null),
      () -> usersPerCityAggregation(),
      () -> policiesPerUserAggregation(),
      () -> removePolicy(null),
      () -> removeUser(),
      () -> changePassword(),
      () -> saveCompany("company.txt"),
      () -> loadCompany("company.txt"),
      () -> serializeCompany("company.ser"),
      () -> loadSerializedCompany("company.ser"),
      () -> logoutAdmin()
    );
    while(activeMenu > 1) {
      menuHandler(options, actions);
    }
  }
  
  public void testCode() {
    //lab5
    try {
      // test fills
      Car testCar = new Car("Test Car", CarType.LUX, 2000, 3000);
      Car testCar2 = new Car("Test Car 2", CarType.SED, 2500, 3100);
      ComprehensivePolicy testPolicy = new ComprehensivePolicy(InsuranceCompany.generateRandomPolicyId(), testCar, 1, "James Bond", new MyDate(2004, 6, 15), 29, 1);
      ComprehensivePolicy testPolicy2 = new ComprehensivePolicy(InsuranceCompany.generateRandomPolicyId(), testCar2, 2, "Foo Bar", new MyDate(2008, 5, 11), 29, 1);
      User testUser = mainCompany.createUser(getAdminUsername(), getAdminPassword(), "Test Only", "test_only", "test", new Address(0, "Test", "t-test", "AAAA Testing"), null, null);
      testUser.addPolicy("test_only", "test", testPolicy);
      testUser.addPolicy("test_only", "test", testPolicy2);
      // testing policies sort
      HashMap<Integer, InsurancePolicy> shallowCopyPolicies = testUser.shallowCopyPoliciesHashMap("test_only", "test");
      HashMap<Integer, InsurancePolicy> deepCopyPolicies = testUser.deepCopyPoliciesHashMap("test_only", "test");
      // change some fields in user
      Car testCar3 = new Car("Test Car 3", CarType.HATCH, 1000, 50000);
      ComprehensivePolicy testPolicy3 = new ComprehensivePolicy(InsuranceCompany.generateRandomPolicyId(), testCar3, 1000, "Test Policy", new MyDate(2999, 7, 12), 29, 1);
      testUser.addPolicy("test_only", "test", testPolicy3);
      checkTestStatus(
        "policy " + testPolicy3.getId() + " should only exist in original list, not shallow nor deep copies.",
        testUser.getPolicies().get(testPolicy3.getId()) != null &&
        shallowCopyPolicies.get(testPolicy3.getId()) == null &&
        deepCopyPolicies.get(testPolicy3.getId()) == null
      );
      // sort policies
      ArrayList<InsurancePolicy> sortedPolicies = testUser.sortPoliciesByDate("test_only", "test");
      System.out.println("After Sorting Policies");
      printDivider();
      System.out.println("Sorted Policies:");
      InsuranceCompany.printPolicies(sortedPolicies);
      System.out.println("Original Policies:");
      InsuranceCompany.printPolicies(testUser.getPolicies());
      System.out.println("Shallow Copied Policies:");
      InsuranceCompany.printPolicies(shallowCopyPolicies);
      System.out.println("Deep Copied Policies:");
      InsuranceCompany.printPolicies(deepCopyPolicies);
      checkTestStatus(
        "policy " + testPolicy3.getId() + " should be last item of sorted list.",
        sortedPolicies.getLast().getId() == testPolicy3.getId()
      );
      // testing users sort
      HashMap<Integer, User> shallowCopyUsers = mainCompany.shallowCopyUsersHashMap(getAdminUsername(), getAdminPassword());
      HashMap<Integer, User> deepCopyUsers = mainCompany.deepCopyUsersHashMap(getAdminUsername(), getAdminPassword());
      // add new user
      User testUser2 = mainCompany.createUser(getAdminUsername(), getAdminPassword(), "Test Only 2", "test_only_2", "test", new Address(0, "Test 2", "t-test", "Testing"), null, null);
      checkTestStatus(
        "user " + testUser2.getUserID() + " should only exist in original list, not shallow nor deep copies.",
        mainCompany.getUsers().get(testUser2.getUserID()) != null &&
        shallowCopyUsers.get(testUser2.getUserID()) == null &&
        deepCopyUsers.get(testUser2.getUserID()) == null
      );
      // sort users
      // by city
      ArrayList<User> sortedUsersByCity = mainCompany.sortUsers(getAdminUsername(), getAdminPassword());
      System.out.println("After Sorting Users by City");
      printDivider();
      System.out.println("Sorted Users by City:");
      InsuranceCompany.printUsers(sortedUsersByCity);
      System.out.println("Original Users:");
      mainCompany.printUsers(getAdminUsername(), getAdminPassword());
      System.out.println("Shallow Copied Users:");
      InsuranceCompany.printUsers(shallowCopyUsers);
      System.out.println("Deep Copied Users:");
      InsuranceCompany.printUsers(deepCopyUsers);
      checkTestStatus(
        "user " + testUser.getUserID() + " should be on first of sorted list.",
        sortedUsersByCity.get(0).getUserID() == testUser.getUserID()
      );
      // by city
      ArrayList<User> sortedUsersByTotalPayment = mainCompany.sortUsers(getAdminUsername(), getAdminPassword());
      System.out.println("After Sorting Users by City");
      printDivider();
      System.out.println("Sorted Users by City:");
      InsuranceCompany.printUsers(sortedUsersByTotalPayment);
      System.out.println("Original Users:");
      mainCompany.printUsers(getAdminUsername(), getAdminPassword());
      System.out.println("Shallow Copied Users:");
      InsuranceCompany.printUsers(shallowCopyUsers);
      System.out.println("Deep Copied Users:");
      InsuranceCompany.printUsers(deepCopyUsers);
      checkTestStatus(
        "user " + testUser.getUserID() + " should be on first of sorted list.",
        sortedUsersByCity.get(0).getUserID() == testUser.getUserID()
      );
      // deep copy company
      InsuranceCompany deepCopyCompany = mainCompany.clone();
      testUser.setCity("New York");
      System.out.println("Original Company:");
      mainCompany.print();
      System.out.println("Deep Copied Company:");
      deepCopyCompany.print();
      checkTestStatus(
        "city of user " + testUser.getUserID() + " should be updated to 'New York' only in main company and not in deep copy.",
        mainCompany.findUser(getAdminUsername(), getAdminPassword(), testUser.getUserID()).getAddress().getCity().equals("New York") &&
        !deepCopyCompany.findUser(getAdminUsername(), getAdminPassword(), testUser.getUserID()).getAddress().getCity().equals("New York")  
      );
      // lab6
      // policy serialization
      System.out.println("TESTING BINARY SERIALIZATION");
      String serFileNamePolicies = "policiesTest.ser";
      System.out.println("List of Policies:");
      InsuranceCompany.printPolicies(testUser.getPolicies());
      System.out.println("Saving Policies...");
      boolean isTestUserPoliciesSerialized = InsuranceCompany.savePolicies(testUser.getPolicies(), serFileNamePolicies);
      System.out.println("List of Saved Policies:");
      HashMap<Integer, InsurancePolicy> savedPolicies = InsuranceCompany.loadPolicies(serFileNamePolicies);
      InsuranceCompany.printPolicies(savedPolicies);
      checkTestStatus("policies of user " + testUser.getUserID() + " should be serialized.", isTestUserPoliciesSerialized && savedPolicies.size() > 0);
      // user serialization
      String serFileNameUsers = "usersTest.ser";
      System.out.println("List of Users:");
      mainCompany.printUsers(getAdminUsername(), getAdminPassword());
      System.out.println("Saving Users...");
      boolean isUsersSerialized = InsuranceCompany.saveUsers(mainCompany.getUsers(), serFileNameUsers);
      System.out.println("List of Saved Users:");
      HashMap<Integer, User> savedUsers = InsuranceCompany.loadUsers(serFileNameUsers);
      InsuranceCompany.printUsers(savedUsers);
      checkTestStatus("Company's users should be serialized.", isUsersSerialized && savedUsers.size() > 0);
      // company serialization
      String serFileNameCompany = "companyTest.ser";
      System.out.println("Main Company Details:");
      mainCompany.print();
      System.out.println("Saving Main Company...");
      boolean isMainCompanySerialized = mainCompany.save(getAdminUsername(), getAdminPassword(), serFileNameCompany);
      System.out.println("Clone and Re-initialized Company:");
      InsuranceCompany clonedCompany = new InsuranceCompany();
      clonedCompany.load(getAdminUsername(), getAdminPassword(), serFileNameCompany);
      clonedCompany.print();
      System.out.println("'"+mainCompany.getName()+"' and '" + clonedCompany.getName() + "'");
      checkTestStatus("A clone of company should be serialized.", isMainCompanySerialized && mainCompany.getName().equals(clonedCompany.getName()));
      // policy file storing
      System.out.println("TESTING FILES");
      String fileNamePolicies = "policies.txt";
      System.out.println("List of Policies:");
      InsuranceCompany.printPolicies(testUser.getPolicies());
      System.out.println("Saving Policies...");
      boolean isTestUserPoliciesTextFileSaved = InsuranceCompany.saveTextFilePolicies(testUser.getPolicies(), fileNamePolicies);
      System.out.println("List of Saved Policies:");
      HashMap<Integer, InsurancePolicy> extractedPolicies = InsuranceCompany.loadTextFilePolicies(fileNamePolicies);
      InsuranceCompany.printPolicies(extractedPolicies);
      checkTestStatus("policies of user " + testUser.getUserID() + " should be saved in a file.", isTestUserPoliciesTextFileSaved && extractedPolicies.size() > 0);
      // user file storing
      String fileNameUsers = "users.txt";
      System.out.println("List of Users:");
      mainCompany.printUsers(getAdminUsername(), getAdminPassword());
      System.out.println("Saving Users...");
      boolean isUsersTextFileSaved = InsuranceCompany.saveTextFileUsers(mainCompany.getUsers(), fileNameUsers);
      System.out.println("List of Saved Users:");
      HashMap<Integer, User> extractedUsers = InsuranceCompany.loadTextFileUsers(fileNameUsers);
      InsuranceCompany.printUsers(extractedUsers);
      checkTestStatus("Company's users should be saved in a file.", isUsersTextFileSaved && extractedUsers.size() > 0);
      // company file storing
      String fileNameCompany = "company.txt";
      System.out.println("Main Company:");
      mainCompany.print();
      System.out.println("Saving Main Company...");
      boolean isMainCompanyTextFileSaved = mainCompany.saveTextFile(getAdminUsername(), getAdminPassword(), fileNameCompany);
      System.out.println("Clone and Re-initialized Company:");
      InsuranceCompany clonedCompany2 = new InsuranceCompany();
      clonedCompany2.loadTextFile(getAdminUsername(), getAdminPassword(), fileNameCompany);
      clonedCompany2.print();
      checkTestStatus("A clone of company should be saved in a file.", isMainCompanyTextFileSaved && mainCompany.getName().equals(clonedCompany2.getName()));
    } catch (CloneNotSupportedException e) {
      System.out.println("Cloning not supported! Initial tests Skipped.");
    } catch (PolicyException e) {
      System.out.println("A policy's id is out of range! Initial tests Skipped.");
    } catch (PolicyHolderNameException e) {
      System.out.println("A policy's holder name is out of our rules! Initial tests Skipped.");
    }

    //lab5
    try {
      System.out.println("Testing out of range policy id");
      new ComprehensivePolicy(1, null, 0, "Test Placeholder", null, 0, 0);
      checkTestStatus("PolicyException should be thrown for id '0'.", false);
    } catch (PolicyException e) {
      System.out.println(e);
      checkTestStatus("PolicyException should be thrown for id '0'.", true);
    } catch (PolicyHolderNameException e) {
    }

    int testRandomID = InsuranceCompany.generateRandomPolicyId();
    try {
      System.out.println("Testing in range policy id and Capitalized two-word policyHolderName");
      new ComprehensivePolicy(testRandomID, null, 0, "Test Placeholder", null, 0, 0);
      checkTestStatus("Policy should be created normally without throwing PolicyException for random generated id (" + testRandomID + ").", true);
    } catch (Exception e) {
      System.out.println(e);
      checkTestStatus("Policy should be created normally without throwing PolicyException for random generated id (" + testRandomID + ").", false);
    }

    // ASM2
    try {
      System.out.println("Testing unexpected policy holder-name with 'simpleText'");
      new ComprehensivePolicy(InsuranceCompany.generateRandomPolicyId(), null, 0, "simpleText", null, 0, 0);
      checkTestStatus("PolicyHolderNameException should be thrown for name 'simpleText'.", false);
    } catch (PolicyHolderNameException e) {
      System.out.println(e);
      checkTestStatus("PolicyHolderNameException should be thrown for name 'simpleText'.", true);
    } catch (PolicyException e) {
    }

    System.out.println("Overall Result (" + (passedCount + failedCount) +"): " + GREEN + passedCount + "passed , " + RED + failedCount + "failed" + DEFAULT);
    passedCount = 0;
    failedCount = 0;
  }
  
  public void createUser() {
    System.out.print("Enter user's name: ");
    String name = inputReader.nextLine();
    System.out.print("Enter a unique username: ");
    String username = inputReader.nextLine();
    while (mainCompany.findUser(getAdminUsername(), getAdminPassword(), username) != null) {
      System.out.print("Username exists! Enter again: ");
      username = inputReader.nextLine();
    }
    System.out.print("Enter password: ");
    String password = inputReader.nextLine();
    Address address = getAddress();
    User newUser = mainCompany.createUser(
      getAdminUsername(),
      getAdminPassword(),
      name,
      username,
      password,
      address,
      null,
      null
    );
    if (newUser != null) {
      System.out.println("New user created with ID (" + newUser.getUserID() + ")");
    } else {
      System.out.println("Couldn't add user! Try again later.");
    }
  }
  
  public void createThirdPartyPolicy(Integer userID) {
    Car car = createNewCar();
    if (userID == null) {
      System.out.print("Which user you want to create policy for? ");
      userID = getUserID();
    }
    System.out.print("Enter your policy's holder name: ");
    String policyHolderName = inputReader.nextLine();
    System.out.print("What should expiry date be? ");
    MyDate expiryDate = getDate();
    System.out.print("Want to add any comments? ");
    String comments = inputReader.nextLine();
    int numberOfClaims = getRestrictedInt(0, null, "Enter number of claims ");
    int policyId = InsuranceCompany.generateRandomPolicyId();
    createThirdPartyPolicyInCompany(userID, policyHolderName, policyId, car, numberOfClaims, expiryDate, comments);
  }
  
  public void createComprehensivePolicy(Integer userID) {
    Car car = createNewCar();
    if (userID == null) {
      System.out.print("Which user you want to create policy for? ");
      userID = getUserID();
    }
    System.out.print("Enter your policy's holder name: ");
    String policyHolderName = inputReader.nextLine();
    System.out.print("What should expiry date be? ");
    MyDate expiryDate = getDate();
    int numberOfClaims = getRestrictedInt(0, null, "Enter number of claims ");
    int driverAge = getRestrictedInt(0, null, "Enter driver's age ");
    int level = getRestrictedInt(0, null, "Enter policy level ");
    int policyId = InsuranceCompany.generateRandomPolicyId();
    createComprehensivePolicyInCompany(userID, policyHolderName, policyId, car, numberOfClaims, expiryDate, driverAge, level);
  }
  
  public void printUserInformation() {
    System.out.println("All Users in Company:");
    mainCompany.printUsers(getAdminUsername(), getAdminPassword());
  }
  
  public void printSortedUsersByCity() {
    try {
      System.out.println("All Users in Company, Sorted by Address:");
      InsuranceCompany.printUsers(mainCompany.sortUsers(getAdminUsername(), getAdminPassword()));
    } catch (CloneNotSupportedException e) {
      System.out.println(e);
    }
  }
  
  public void printSortedUsersByTotalPayment() {
    System.out.println("All Users in Company, Sorted by Total Payment:");
    InsuranceCompany.printUsers(mainCompany.sortUsersByPremium(getAdminUsername(), getAdminPassword()));
  }
  
  public void filterByCarModel() {
    System.out.println("To filter policies, please Enter car model: ");
    String carModel = inputReader.nextLine();
    HashMap<Integer, InsurancePolicy> filteredPoliciesByCarModel = mainCompany.filterByCarModel(getAdminUsername(), getAdminPassword(), carModel);
    System.out.println(filteredPoliciesByCarModel.size() > 0 ? "List of policies containing '" + carModel + "':" : "No policy found!");
    InsuranceCompany.printPolicies(filteredPoliciesByCarModel);
    System.out.println();
  }
  
  public void filterByExpiryDate(Integer userID) {
    System.out.print("To filter policies of an specific user by expiry date, please ");
    if (userID == null) {
      userID = getUserID();
    }
    MyDate expiryDate = getDate();
    HashMap<Integer, InsurancePolicy> expiredPolicies = mainCompany.filterByExpiryDate(getAdminUsername(), getAdminPassword(), userID, expiryDate);
    InsuranceCompany.printPolicies(expiredPolicies);
    System.out.println();
  }
  
  public void updateAddress(Integer userID) {
    if (userID == null) {
      System.out.print("To update a user's address, ");
      userID = getUserID();
    }
    User validUser = mainCompany.findUser(getAdminUsername(), getAdminPassword(), userID);
    Address newAddress = getAddress();
    validUser.setAddress(newAddress);
    System.out.println();
  }

  public void userTotalPaymentPerCarModelAggregation() {
    int userID = getUserID();
    User targetUser = mainCompany.findUser(getAdminUsername(), getAdminPassword(), userID);
    totalPaymentPerCarModelAggregation(targetUser);
  }

  public void totalPaymentPerCityAggregation() {
    // ArrayList<String> cities = mainCompany.populateDistinctCityNames();
    // ArrayList<Double> payments = mainCompany.getTotalPaymentPerCity(cities);
    HashMap<String, Double> cityPaymentMap = mainCompany.getTotalPremiumPerCity(getAdminUsername(), getAdminPassword());
    System.out.println("Total payment per city: ");
    // mainCompany.reportPaymentsPerCity(cities, payments);
    InsuranceCompany.reportPaymentsPerCity(cityPaymentMap);
    System.out.println();
  }

  public void removePolicy(Integer userID) {
    System.out.print("To remove a policy, please ");
    if (userID == null) {
      userID = getUserID();
    }
    User targetUser = mainCompany.findUser(getAdminUsername(), getAdminPassword(), userID);
    int policyID = getPolicyID(targetUser);
    InsurancePolicy policy = targetUser.findPolicy(getAdminUsername(), getAdminPassword(), policyID);
    System.out.println("Removing policy (" + policy.getPolicyHolderName() + ") with ID " + policyID + " from (" + targetUser.getName() + ").");
    System.out.println("Confirm? (y/n) ");
    String input = inputReader.nextLine();
    if (input.equals("y")) {
      if (targetUser.removePolicy(getAdminUsername(), getAdminPassword(), policy)) {
        System.out.println("Removed Successfully.");
        return;
      }
    }
    System.out.println("Aborted.");
  }

  public void removeUser() {
    System.out.print("To remove a user, please ");
    int userID = getUserID();
    User targetUser = mainCompany.findUser(getAdminUsername(), getAdminPassword(), userID);
    System.out.println("Removing user (" + targetUser.getName() + ") with ID " + userID + ".");
    System.out.println("Confirm? (y/n) ");
    String input = inputReader.nextLine();
    if (input.equals("y")) {
      if (mainCompany.removeUser(getAdminUsername(), getAdminPassword(), targetUser)) {
        System.out.println("Removed Successfully.");
        return;
      }
    }
    System.out.println("Aborted.");
  }

  public void changePassword() {
    System.out.print("Current Password: ");
    String currentPassword = inputReader.nextLine();
    System.out.print("New Password: ");
    String newPassword = inputReader.nextLine();
    System.out.print("Confirm Password: ");
    String confirmPassword = inputReader.nextLine();
    if (confirmPassword.equals(newPassword)) {
      if (mainCompany.updateAdminPassword(newPassword, currentPassword, newPassword)) {
        System.out.println("Password has been updated.");
      } else {
        System.out.println("Current password is incorrect.");
      }
    } else {
      System.out.println("Confirm password did not match.");
    }
  }

  public void saveCompany(String fileName) {
    System.out.println("Saving Company...");
    if (mainCompany.saveTextFile(getAdminUsername(), getAdminPassword(), fileName)) {
      System.out.println("Successfully Saved!");
    } else {
      System.out.println("Failed Saving!");
    }
    System.out.println();
  }

  public void loadCompany(String fileName) {
    System.out.println("Loading Company...");
    if (mainCompany.loadTextFile(getAdminUsername(), getAdminPassword(), fileName)) {
      System.out.println("Successfully Loaded!");
    } else {
      System.out.println("Failed Loading!");
    }
    System.out.println();
  }

  public void serializeCompany(String fileName) {
    System.out.println("Saving Company...");
    try {
      if (mainCompany.clone().save(getAdminUsername(), getAdminPassword(), fileName)) {
        System.out.println("Successfully Saved!");
      } else {
        System.out.println("Failed Saving!");
      }
      System.out.println();
    } catch (CloneNotSupportedException e) {
      System.out.println("Not Supported!");
    }
  }

  public void loadSerializedCompany(String fileName) {
    System.out.println("Loading Company...");
    if (mainCompany.load(getAdminUsername(), getAdminPassword(), fileName)) {
      System.out.println("Successfully Loaded!");
    } else {
      System.out.println("Failed Loading!");
    }
    System.out.println();
  }

  public void totalPaymentPerCarModelAggregation(User user) {
    // ArrayList<String> carModels = new ArrayList<>();
    // ArrayList<Integer> counts = new ArrayList<>();
    // ArrayList<Double> payments = new ArrayList<>();
    System.out.println("Total payment per car model: ");
    if (user == null) {
      // carModels = mainCompany.populateDistinctCarModels();
      // counts = mainCompany.getTotalCountPerCarModel(carModels);
      // payments = mainCompany.getTotalPaymentPerCarModel(carModels);
      InsuranceCompany.reportPaymentsPerCarModel(
        mainCompany.getTotalCountPerCarModel(getAdminUsername(), getAdminPassword()),
        mainCompany.getTotalPremiumPerCarModel(getAdminUsername(), getAdminPassword())
      );
    } else {
      // carModels = user.populateDistinctCarModels();
      // counts = user.getTotalCountPerCarModel(carModels);
      // payments = user.getTotalPaymentPerCarModel(carModels, mainCompany.getFlatRate());
      InsuranceCompany.reportPaymentsPerCarModel(
        user.getTotalCountForCarModel(getAccountUsername(), getAccountPassword()),
        user.getTotalPaymentForCarModel(getAccountUsername(), getAccountPassword(), mainCompany.getFlatRate())
      );
    }
    // InsuranceCompany.reportPaymentsPerCarModel(carModels, counts, payments);

    System.out.println();
  }

  public void usersPerCityAggregation() {
    HashMap<String, ArrayList<User>> cityUsersMap = mainCompany.getUsersPerCity(getAdminUsername(), getAdminPassword());
    System.out.println("Total payment per city: ");
    InsuranceCompany.reportUsersPerCity(cityUsersMap, mainCompany.getFlatRate());
    System.out.println();
  }

  public void policiesPerUserAggregation() {
    MyDate expiryDate = getDate();
    HashMap<String, ArrayList<InsurancePolicy>> userPoliciesMap = mainCompany.filterPoliciesByExpiryDate(getAdminUsername(), getAdminPassword(), expiryDate);
    System.out.println("Total payment per city: ");
    InsuranceCompany.reportPoliciesPerUser(userPoliciesMap, mainCompany.getFlatRate());
    System.out.println();
  }

  public void logoutAdmin() {
    adminLoggedIn = false;
    activeMenu = 1;
  }

//*************************************************************************************
//User Menu    
  public void userMenu() {
    if (account == null) return;
    activeMenu = 3;
    List<String> options = Arrays.asList(
      "1. Add Third Party Policy",
      "2. Add Comprehensive Policy",
      "3. Change Address",
      "4. Remove a Policy",
      "5. Print a Policy",
      "6. Filter Policies by Expiry Date",
      "7. Report Payments Per Car Model",
      "8. Exit"
    );
    List<Runnable> actions = Arrays.asList(
      () -> createThirdPartyPolicy(account.getUserID()),
      () -> createComprehensivePolicy(account.getUserID()),
      () -> updateAddress(account.getUserID()),
      () -> removePolicy(account.getUserID()),
      () -> printPolicy(),
      () -> filterByExpiryDate(account.getUserID()),
      () -> totalPaymentPerCarModelAggregation(account),
      () -> logoutUser()
    );
    while(activeMenu > 2) {
      menuHandler(options, actions);
    }
  }
  
  public void printPolicy() {
    System.out.print("To view a policy details, ");
    int policyID = getPolicyID(account);
    account.findPolicy(account.getUsername(), account.getPassword(), policyID).print();
  }

  public void logoutUser() {
    account = null;
    activeMenu = 1;
  }

  // ----------------------------------------------------------------------

  public String getAdminUsername() {
    return adminLoggedIn ? mainCompany.getAdminUsername() : "";
  }

  public String getAdminPassword() {
    return adminLoggedIn ? mainCompany.getAdminPassword() : "";
  }

  public String getAccountUsername() {
    return account == null ? "" : account.getUsername();
  }

  public String getAccountPassword() {
    return account == null ? "" : account.getPassword();
  }

  public void printOptions(List<String> options) {
    for (String option : options) {
      System.out.println(option);
    }
  }
  
  public void menuHandler(List<String> options, List<Runnable> actions) {
    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); // clear screen
    System.out.println(menus[activeMenu - 1]);
    printOptions(options);
    int choice = getRestrictedInt(1, options.size(), "What do you mind to do?");
    System.out.println();
    actions.get(choice - 1).run();
    System.out.println();
    System.out.print("Press enter to continue...");
    inputReader.nextLine();
  }

  public int getUserID() {
    int userID = 0;
    System.out.print("Enter userID: ");
    while (userID == 0) {
      try {
        userID = inputReader.nextInt();
        inputReader.nextLine();
        if (mainCompany.findUser(getAdminUsername(), getAdminPassword(), userID) == null) {
          System.out.print("User not found. Try Again: ");
          userID = 0;
        }
      } catch (Exception e) {
        System.out.print("Wrong input. Try again: ");
        inputReader.nextLine();
      }
    }
    return userID;
  }

  public int getPolicyID(User user) {
    int policyID = -1;
    System.out.print("Enter policyID: ");
    while (policyID == -1) {
      try {
        policyID = inputReader.nextInt();
        inputReader.nextLine();
        if (user.findPolicy(getAdminUsername(), getAdminPassword(), policyID) == null) {
          System.out.print("Policy not found. Try Again: ");
          policyID = -1;
        }
      } catch (Exception e) {
        System.out.print("Wrong input. Try again: ");
        inputReader.nextLine();
      }
    }
    return policyID;
  }

  public int getRestrictedInt(Integer min, Integer max, String message) {
    int input = -1;
    String range = "(" + min + (max == null ? " or more" : " - " + max) + ")";
    System.out.print(message + range + ": ");
    while (input == -1) {
      try {
        input = inputReader.nextInt();
        inputReader.nextLine();
        if (input < min || (max != null && input > max)) {
          System.out.print("Out of range " + range + ". Try again: ");
          input = -1;
        }
      } catch (Exception e) {
        System.out.print("Wrong input. Try again: ");
        inputReader.nextLine();
      }
    }
    return input;
  }

  public MyDate getDate() {
    MyDate date = null;
    while (date == null) {
      System.out.print("Enter a Date (yyyy/mm/dd): ");
      String input = inputReader.nextLine();
      try {
        String[] inputs = input.split("/");
        date = InsuranceCompany.createValidDate(
          Integer.parseInt(inputs[0]), 
          Integer.parseInt(inputs[1]), 
          Integer.parseInt(inputs[2])
        );
      } catch (Exception e) {
        date = null;
        System.out.println(e.getMessage());
        System.out.println("Please try again.");
      }
    }
    return date;
  }

  public void createThirdPartyPolicyInCompany(int userID, String policyHolderName, int policyId, Car car, int numberOfClaims, MyDate expiryDate, String comments) {
    try {
      if (mainCompany.createThirdPartyPolicy(getAdminUsername(), getAdminPassword(), userID, policyHolderName, policyId, car, numberOfClaims, expiryDate, comments)) {
        System.out.println("Policy '" + policyHolderName + "' added.");
      } else {
        if (mainCompany.findUser(getAdminUsername(), getAdminPassword(), userID) == null) {
          System.out.println("User with ID " + userID + " not found.");
        } else {
          System.out.println("Policy with this ID already exists.");
        }
      }
    } catch (PolicyException e) {
      System.out.println(e);
    } catch (PolicyHolderNameException e) {
      System.out.println(e);
    }
  }
  
  public void createComprehensivePolicyInCompany(int userID, String policyHolderName, int policyId, Car car, int numberOfClaims, MyDate expiryDate, int driverAge, int level) {
    try {
      if (mainCompany.createComprehensivePolicy(getAdminUsername(), getAdminPassword(), userID, policyHolderName, policyId, car, numberOfClaims, expiryDate, driverAge, level)) {
        System.out.println("Policy '" + policyHolderName + "' added.");
      } else {
        if (mainCompany.findUser(getAdminUsername(), getAdminPassword(), userID) == null) {
          System.out.println("User with ID " + userID + " not found.");
        } else {
          System.out.println("Policy with this ID already exists.");
        }
      }
    } catch (PolicyException e) {
      System.out.println(e);
    } catch (PolicyHolderNameException e) {
      System.out.println(e);
    }
  }

  public CarType getCarModel() {
    System.out.println("Enter car model (" + CarType.values() + "): ");
    while (true) {
      try {
        String input = inputReader.nextLine();
        CarType carModel = InsuranceCompany.validateCarType(input);
        return carModel;
      } catch (IllegalArgumentException ex) {  
        System.out.println("Invalid input. choose from " + CarType.values() + ": ");
      }  
    }
  }

  public Address getAddress() {
    int streetNum = getRestrictedInt(0, null, "Enter new Street Number");
    System.out.print("Enter new Street: ");
    String street = inputReader.nextLine();
    System.out.print("Enter new Suburb: ");
    String suburb = inputReader.nextLine();
    System.out.print("Enter new City: ");
    String city = inputReader.nextLine();
    Address address = InsuranceCompany.createAddress(streetNum, street, suburb, city);
    return address;
  }
  
  public Car createNewCar() {
    System.out.println("For Creating a New Car, enter following details.");
    System.out.println("Enter car model: ");
    String carModel = inputReader.nextLine();
    CarType carType = getCarModel();
    int year = getRestrictedInt(1800, 2024, "Enter manufacturing year ");
    int price = getRestrictedInt(0, null, "Enter car price ");
    Car car = InsuranceCompany.createCar(carModel, carType, year, price);
    return car;
  }

  public void printDivider() {
    System.out.println("__________________________________________________");
  }

  public void checkTestStatus(String testCase, boolean passed) {
    System.out.println((passed ? GREEN + "TEST PASSED" : RED + "TEST FAILED") + "! Test case was: " + testCase + DEFAULT);
    printDivider();
    if (passed) passedCount++;
    else failedCount++;
  }
}