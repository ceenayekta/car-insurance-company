import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
  int activeMenu = 0;
  int passedCount = 0;
  int failedCount = 0;
  final String RED = "\u001B[31m";
  final String GREEN = "\u001B[32m";
  final String DEFAULT = "\u001B[0m";
  String[] menus = { "Main Menu", "Admin Menu", "User Menu" };
  public User account;
  Scanner inputReader = new Scanner(System.in);
  InsuranceCompany mainCompany;
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
        if (adminLogin()) {
          adminMenu();
          activeMenu = 2;
        }
      },
      () -> {
        if (userLogin()) {
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
  public boolean adminLogin() {
    System.out.print("Username: ");
    String username = inputReader.nextLine();
    System.out.print("Password: ");
    String password = inputReader.nextLine();
    if (mainCompany.validateAdmin(username, password)) {
      System.out.println("Logged in as " + username + " successfully!");
      return true;
    } else {
      System.out.println("\nWrong username or password.\n");
      return false;
    }
  }
  
  private boolean userLogin() {
    int userId = getRestrictedInt(0, null, "Enter user id: ");
    account = mainCompany.findUser(userId);
    if (account != null) {
      System.out.println("Logged in as " + account.getName() + " successfully!");
      return true;
    } else {
      System.out.println("\nID doesn't exist.\n");
      return false;
    }
  }  
//*************************************************************************************
//Admin menu
  public void adminMenu() {
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
      () -> { activeMenu = 1; }
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
      ComprehensivePolicy testPolicy = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), testCar, 1, "James Bond", new MyDate(2004, 6, 15), 29, 1);
      ComprehensivePolicy testPolicy2 = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), testCar2, 2, "Foo Bar", new MyDate(2008, 5, 11), 29, 1);
      User testUser = new User("Test Only", new Address(0, "Test", "t-test", "AAAA Testing"), new HashMap<>() {{
        put(testPolicy.getId(), testPolicy);
        put(testPolicy2.getId(), testPolicy2);
      }}, null);
      mainCompany.addUser(testUser);
      // testing policies sort
      HashMap<Integer, InsurancePolicy> shallowCopyPolicies = testUser.shallowCopyPoliciesHashMap();
      HashMap<Integer, InsurancePolicy> deepCopyPolicies = testUser.deepCopyPoliciesHashMap();
      // change some fields in user
      Car testCar3 = new Car("Test Car 3", CarType.HATCH, 1000, 50000);
      ComprehensivePolicy testPolicy3 = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), testCar3, 1000, "Test Policy", new MyDate(2999, 7, 12), 29, 1);
      testUser.addPolicy(testPolicy3);
      checkTestStatus(
        "testPolicy3 should only exist in original list, not shallow nor deep copies.",
        testUser.getPolicies().get(testPolicy3.getId()) != null &&
        shallowCopyPolicies.get(testPolicy3.getId()) == null &&
        deepCopyPolicies.get(testPolicy3.getId()) == null
      );
      // sort policies
      ArrayList<InsurancePolicy> sortedPolicies = testUser.sortPoliciesByDate();
      System.out.println("After Sorting Policies");
      printDivider();
      System.out.println("Sorted Policies:");
      InsurancePolicy.printPolicies(sortedPolicies);
      System.out.println("Original Policies:");
      InsurancePolicy.printPolicies(testUser.getPolicies());
      System.out.println("Shallow Copied Policies:");
      InsurancePolicy.printPolicies(shallowCopyPolicies);
      System.out.println("Deep Copied Policies:");
      InsurancePolicy.printPolicies(deepCopyPolicies);
      checkTestStatus(
        "testPolicy3 should be last item of sorted list.",
        sortedPolicies.getLast().getId() == testPolicy3.getId()
      );
      // testing users sort
      HashMap<Integer, User> shallowCopyUsers = mainCompany.shallowCopyUsersHashMap();
      HashMap<Integer, User> deepCopyUsers = mainCompany.deepCopyUsersHashMap();
      // add new user
      User testUser2 = new User("Test Only 2", new Address(0, "Test 2", "t-test", "Testing"), null, null);
      mainCompany.addUser(testUser2);
      checkTestStatus(
        "testUser2 should only exist in original list, not shallow nor deep copies.",
        mainCompany.getUsers().get(testUser2.getUserID()) != null &&
        shallowCopyUsers.get(testUser2.getUserID()) == null &&
        deepCopyUsers.get(testUser2.getUserID()) == null
      );
      // sort users
      // by city
      ArrayList<User> sortedUsersByCity = mainCompany.sortUsers();
      System.out.println("After Sorting Users by City");
      printDivider();
      System.out.println("Sorted Users by City:");
      User.printUsers(sortedUsersByCity);
      System.out.println("Original Users:");
      User.printUsers(mainCompany.getUsers());
      System.out.println("Shallow Copied Users:");
      User.printUsers(shallowCopyUsers);
      System.out.println("Deep Copied Users:");
      User.printUsers(deepCopyUsers);
      checkTestStatus(
        "testUser should be on first of sorted list.",
        sortedUsersByCity.get(0).getUserID() == testUser.getUserID()
      );
      // by city
      ArrayList<User> sortedUsersByTotalPayment = mainCompany.sortUsers();
      System.out.println("After Sorting Users by City");
      printDivider();
      System.out.println("Sorted Users by City:");
      User.printUsers(sortedUsersByTotalPayment);
      System.out.println("Original Users:");
      User.printUsers(mainCompany.getUsers());
      System.out.println("Shallow Copied Users:");
      User.printUsers(shallowCopyUsers);
      System.out.println("Deep Copied Users:");
      User.printUsers(deepCopyUsers);
      checkTestStatus(
        "testUser should be on first of sorted list.",
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
        "testUser city should be updated to 'New York' only in main company and not in deep copy.",
        mainCompany.findUser(testUser.getUserID()).getAddress().getCity().equals("New York") &&
        !deepCopyCompany.findUser(testUser.getUserID()).getAddress().getCity().equals("New York")  
      );
      // lab6
      // policy serialization
      System.out.println("TESTING BINARY SERIALIZATION");
      String serFileNamePolicies = "policiesTest.ser";
      System.out.println("List of Policies:");
      InsurancePolicy.printPolicies(testUser.getPolicies());
      System.out.println("Saving Policies...");
      InsurancePolicy.save(testUser.getPolicies(), serFileNamePolicies);
      System.out.println("List of Saved Policies:");
      HashMap<Integer, InsurancePolicy> savedPolicies = InsurancePolicy.load(serFileNamePolicies);
      InsurancePolicy.printPolicies(savedPolicies);
      checkTestStatus("testUser's policies should be serialized.", savedPolicies.size() > 0);
      // user serialization
      String serFileNameUsers = "usersTest.ser";
      System.out.println("List of Users:");
      User.printUsers(mainCompany.getUsers());
      System.out.println("Saving Users...");
      User.save(mainCompany.getUsers(), serFileNameUsers);
      System.out.println("List of Saved Users:");
      HashMap<Integer, User> savedUsers = User.load(serFileNameUsers);
      User.printUsers(savedUsers);
      checkTestStatus("Company's users should be serialized.", savedUsers.size() > 0);
      // company serialization
      String serFileNameCompany = "companyTest.ser";
      System.out.println("Deep Copy of Company:");
      deepCopyCompany.print();
      System.out.println("Saving Deep Copy of Company...");
      deepCopyCompany.save(serFileNameCompany);
      System.out.println("Clone and Re-initialized Company:");
      InsuranceCompany clonedCompany = new InsuranceCompany();
      clonedCompany.load(serFileNameCompany);
      clonedCompany.print();
      System.out.println("'"+mainCompany.getName()+"' and '" + clonedCompany.getName() + "'");
      checkTestStatus("A clone of company should be serialized.", mainCompany.getName().equals(clonedCompany.getName()));
      // policy file storing
      System.out.println("TESTING FILES");
      String fileNamePolicies = "policies.txt";
      System.out.println("List of Policies:");
      InsurancePolicy.printPolicies(testUser.getPolicies());
      System.out.println("Saving Policies...");
      InsurancePolicy.saveTextFile(testUser.getPolicies(), fileNamePolicies);
      System.out.println("List of Saved Policies:");
      HashMap<Integer, InsurancePolicy> extractedPolicies = InsurancePolicy.loadTextFile(fileNamePolicies);
      InsurancePolicy.printPolicies(extractedPolicies);
      checkTestStatus("testUser's policies should be saved in a file.", extractedPolicies.size() > 0);
      // user file storing
      String fileNameUsers = "users.txt";
      System.out.println("List of Users:");
      User.printUsers(mainCompany.getUsers());
      System.out.println("Saving Users...");
      User.saveTextFile(mainCompany.getUsers(), fileNameUsers);
      System.out.println("List of Saved Users:");
      HashMap<Integer, User> extractedUsers = User.loadTextFile(fileNameUsers);
      User.printUsers(extractedUsers);
      checkTestStatus("Company's users should be saved in a file.", extractedUsers.size() > 0);
      // company file storing
      String fileNameCompany = "company.txt";
      System.out.println("Deep Copy of Company:");
      mainCompany.print();
      System.out.println("Saving Deep Copy of Company...");
      mainCompany.saveTextFile(fileNameCompany);
      System.out.println("Clone and Re-initialized Company:");
      InsuranceCompany clonedCompany2 = new InsuranceCompany();
      clonedCompany2.loadTextFile(fileNameCompany);
      clonedCompany2.print();
      checkTestStatus("A clone of company should be saved in a file.", mainCompany.getName().equals(clonedCompany2.getName()));
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

    int testRandomID = InsurancePolicy.generateRandomId();
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
      new ComprehensivePolicy(InsurancePolicy.generateRandomId(), null, 0, "simpleText", null, 0, 0);
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
    Address address = getAddress();
    User newUser = new User(name, address, null, null);
    mainCompany.addUser(newUser);
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
    int policyId = InsurancePolicy.generateRandomId();
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
    int policyId = InsurancePolicy.generateRandomId();
    createComprehensivePolicyInCompany(userID, policyHolderName, policyId, car, numberOfClaims, expiryDate, driverAge, level);
  }

  public void printUsers(String message, ArrayList<User> users) {
    System.out.println(message);
    for (User user : users) {
      user.print();
      System.out.println();
    }
    System.out.println();
  }
  
  public void printUserInformation() {
    printUsers("All Users in Company:", new ArrayList<User>(mainCompany.getUsers().values()));
  }
  
  public void printSortedUsersByCity() {
    try {
      printUsers("All Users in Company, Sorted by Address:", mainCompany.sortUsers());
    } catch (CloneNotSupportedException e) {
      System.out.println(e);
    }
  }
  
  public void printSortedUsersByTotalPayment() {
    printUsers("All Users in Company, Sorted by Total Payment:", mainCompany.sortUsersByPremium());
  }
  
  public void filterByCarModel() {
    System.out.println("To filter policies, please Enter car model: ");
    String carModel = inputReader.nextLine();
    HashMap<Integer, InsurancePolicy> filteredPoliciesByCarModel = mainCompany.filterByCarModel(carModel);
    System.out.println(filteredPoliciesByCarModel.size() > 0 ? "List of policies containing '" + carModel + "':" : "No policy found!");
    InsurancePolicy.printPolicies(filteredPoliciesByCarModel);
    System.out.println();
  }
  
  public void filterByExpiryDate(Integer userID) {
    System.out.print("To filter policies of an specific user by expiry date, please ");
    if (userID == null) {
      userID = getUserID();
    }
    MyDate expiryDate = getDate();
    HashMap<Integer, InsurancePolicy> expiredPolicies = mainCompany.filterByExpiryDate(userID, expiryDate);
    InsurancePolicy.printPolicies(expiredPolicies);
    System.out.println();
  }
  
  public void updateAddress(Integer userID) {
    if (userID == null) {
      System.out.print("To update a user's address, ");
      userID = getUserID();
    }
    User validUser = mainCompany.findUser(userID);
    Address newAddress = getAddress();
    validUser.setAddress(newAddress);
    System.out.println();
  }

  public void userTotalPaymentPerCarModelAggregation() {
    int userID = getUserID();
    User targetUser = mainCompany.findUser(userID);
    totalPaymentPerCarModelAggregation(targetUser);
  }

  public void totalPaymentPerCityAggregation() {
    // ArrayList<String> cities = mainCompany.populateDistinctCityNames();
    // ArrayList<Double> payments = mainCompany.getTotalPaymentPerCity(cities);
    HashMap<String, Double> cityPaymentMap = mainCompany.getTotalPremiumPerCity();
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
    User targetUser = mainCompany.findUser(userID);
    int policyID = getPolicyID(targetUser);
    InsurancePolicy policy = targetUser.findPolicy(policyID);
    System.out.println("Removing policy (" + policy.getPolicyHolderName() + ") with ID " + policyID + " from (" + targetUser.getName() + ").");
    System.out.println("Confirm? (y/n) ");
    String input = inputReader.nextLine();
    if (input.equals("y")) {
      if (targetUser.removePolicy(policy)) {
        System.out.println("Removed Successfully.");
        return;
      }
    }
    System.out.println("Aborted.");
  }

  public void removeUser() {
    System.out.print("To remove a user, please ");
    int userID = getUserID();
    User targetUser = mainCompany.findUser(userID);
    System.out.println("Removing user (" + targetUser.getName() + ") with ID " + userID + ".");
    System.out.println("Confirm? (y/n) ");
    String input = inputReader.nextLine();
    if (input.equals("y")) {
      if (mainCompany.removeUser(targetUser)) {
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
    if (mainCompany.saveTextFile(fileName)) {
      System.out.println("Successfully Saved!");
    } else {
      System.out.println("Failed Saving!");
    }
    System.out.println();
  }

  public void loadCompany(String fileName) {
    System.out.println("Loading Company...");
    if (mainCompany.loadTextFile(fileName)) {
      System.out.println("Successfully Loaded!");
    } else {
      System.out.println("Failed Loading!");
    }
    System.out.println();
  }

  public void serializeCompany(String fileName) {
    System.out.println("Saving Company...");
    try {
      if (mainCompany.clone().save(fileName)) {
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
    if (mainCompany.load(fileName)) {
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
      HashMap<String, Double> paymentsMap = mainCompany.getTotalPremiumPerCarModel();
      HashMap<String, Integer> countsMap = mainCompany.getTotalCountPerCarModel();
      InsuranceCompany.reportPaymentsPerCarModel(countsMap, paymentsMap);
    } else {
      // carModels = user.populateDistinctCarModels();
      // counts = user.getTotalCountPerCarModel(carModels);
      // payments = user.getTotalPaymentPerCarModel(carModels, mainCompany.getFlatRate());
      HashMap<String, Double> paymentsMap = user.getTotalPaymentForCarModel(mainCompany.getFlatRate());
      HashMap<String, Integer> countsMap = user.getTotalCountForCarModel();
      User.reportPaymentsPerCarModel(countsMap, paymentsMap);
    }
    // InsuranceCompany.reportPaymentsPerCarModel(carModels, counts, payments);

    System.out.println();
  }

  public void usersPerCityAggregation() {
    HashMap<String, ArrayList<User>> cityUsersMap = mainCompany.getUsersPerCity();
    System.out.println("Total payment per city: ");
    InsuranceCompany.reportUsersPerCity(cityUsersMap, mainCompany.getFlatRate());
    System.out.println();
  }

  public void policiesPerUserAggregation() {
    MyDate expiryDate = getDate();
    HashMap<String, ArrayList<InsurancePolicy>> userPoliciesMap = mainCompany.filterPoliciesByExpiryDate(expiryDate);
    System.out.println("Total payment per city: ");
    InsuranceCompany.reportPoliciesPerUser(userPoliciesMap, mainCompany.getFlatRate());
    System.out.println();
  }

//*************************************************************************************
//User Menu    
  public void userMenu() {
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
      () -> { activeMenu = 0; }
    );
    while(activeMenu > 2) {
      menuHandler(options, actions);
    }
  }
  
  public void printPolicy() {
    System.out.print("To view a policy details, ");
    int policyID = getPolicyID(account);
    account.findPolicy(policyID).print();
  }

  // ----------------------------------------------------------------------

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
        if (mainCompany.findUser(userID) == null) {
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
        if (user.findPolicy(policyID) == null) {
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
        date = MyDate.createValidDate(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
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
      if (mainCompany.createThirdPartyPolicy(userID, policyHolderName, policyId, car, numberOfClaims, expiryDate, comments)) {
        System.out.println("Policy '" + policyHolderName + "' added.");
      } else {
        if (mainCompany.findUser(userID) == null) {
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
      if (mainCompany.createComprehensivePolicy(userID, policyHolderName, policyId, car, numberOfClaims, expiryDate, driverAge, level)) {
        System.out.println("Policy '" + policyHolderName + "' added.");
      } else {
        if (mainCompany.findUser(userID) == null) {
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
        CarType carModel = CarType.valueOf(input);
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
    Address address = new Address(streetNum, street, suburb, city);
    return address;
  }
  
  public Car createNewCar() {
    System.out.println("For Creating a New Car, enter following details.");
    System.out.println("Enter car model: ");
    String carModel = inputReader.nextLine();
    CarType carType = getCarModel();
    int year = getRestrictedInt(1800, 2024, "Enter manufacturing year ");
    int price = getRestrictedInt(0, null, "Enter car price ");
    Car car = new Car(carModel, carType, year, price);
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