import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
  int activeMenu = 0;
  final String RED = "\u001B[31m";
  final String GREEN = "\u001B[32m";
  final String DEFAULT = "\u001B[0m";
  String[] menus = { "Main Menu", "Admin Menu", "User Menu" };
  public User account;
  Scanner inputReader = new Scanner(System.in);
  InsuranceCompany mainCompany;
  public UserInterface(InsuranceCompany company)
  {
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
      "6.  Filter by Car Model",
      "7.  Filter by Expiry Date",
      "8.  Update Address",
      "9.  Report Total Payment of User per Car",
      "10. Report Total Payment per City",
      "11. Report Payments per Car Model",
      "12. Remove a Policy",
      "13. Remove a User",
      "14. Change Password",
      "15. Logout"
    );
    List<Runnable> actions = Arrays.asList(
      () -> testCode(),
      () -> createUser(),
      () -> createThirdPartyPolicy(null),
      () -> createComprehensivePolicy(null),
      () -> printUserInformation(),
      () -> filterByCarModel(),
      () -> filterByExpiryDate(null),
      () -> updateAddress(null),
      () -> userTotalPaymentPerCarModelAggregation(),
      () -> totalPaymentPerCityAggregation(),
      () -> totalPaymentPerCarModelAggregation(null),
      () -> removePolicy(null),
      () -> removeUser(),
      () -> changePassword(),
      () -> { activeMenu = 1; }
    );
    while(activeMenu > 1) {
      menuHandler(options, actions);
    }
  }
  
  public void testCode() {
    int passedCount = 0;
    int failedCount = 0;

    //lab5
    try {
      // test fills
      Car testCar = new Car("Test Car", CarType.LUX, 2000, 3000);
      Car testCar2 = new Car("Test Car 2", CarType.SED, 2500, 3100);
      ComprehensivePolicy testPolicy = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), testCar, 1, "Test Policy", new MyDate(2004, 6, 15), 29, 1);
      ComprehensivePolicy testPolicy2 = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), testCar2, 2, "Test Policy 2", new MyDate(2008, 5, 11), 29, 1);
      User testUser = new User("Test Only", new Address(0, "Test", "t-test", "Testing"), new HashMap<>() {{
        put(testPolicy.getId(), testPolicy);
        put(testPolicy2.getId(), testPolicy2);
      }}, null);
      mainCompany.addUser(testUser);
      // testing policies sort
      HashMap<Integer, InsurancePolicy> shallowCopyPolicies = User.shallowCopyPoliciesHashMap(testUser.getPolicies());
      HashMap<Integer, InsurancePolicy> deepCopyPolicies = User.deepCopyPoliciesHashMap(testUser.getPolicies());
      // change some fields in user
      testUser.setCity("New York");
      Car testCar3 = new Car("Test Car 3", CarType.HATCH, 1000, 1500);
      ComprehensivePolicy testPolicy3 = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), testCar3, 1, "Test Policy 3", new MyDate(2004, 7, 12), 29, 1);
      testUser.addPolicy(testPolicy3);
      if (testUser.getPolicies().get(testPolicy3.getId()) != null && shallowCopyPolicies.get(testPolicy3.getId()) != null && deepCopyPolicies.get(testPolicy3.getId()) == null) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }
      // sort
      testUser.sortPoliciesByDate();
      printDivider();
      System.out.println("After Sorting Policies");
      System.out.println("Original Policies:");
      InsurancePolicy.printPolicies(testUser.getPolicies());
      System.out.println("Shallow Copied Policies:");
      InsurancePolicy.printPolicies(shallowCopyPolicies);
      System.out.println("Deep Copied Policies:");
      InsurancePolicy.printPolicies(deepCopyPolicies);
      // testing users sort
      HashMap<Integer, User> shallowCopyUsers = InsuranceCompany.shallowCopyUsersHashMap(mainCompany.getUsers());
      HashMap<Integer, User> deepCopyUsers = InsuranceCompany.deepCopyUsersHashMap(mainCompany.getUsers());
      // add new user
      User testUser2 = new User("Test Only 2", new Address(0, "Test 2", "t-test", "Testing"), null, null);
      mainCompany.addUser(testUser2);
      if (mainCompany.getUsers().get(testUser2.getUserID()) != null && shallowCopyUsers.get(testUser2.getUserID()) == null && deepCopyUsers.get(testUser2.getUserID()) == null) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }
      // sort
      mainCompany.sortUsers();
      printDivider();
      System.out.println("After Sorting Users");
      System.out.println("Original Users:");
      User.printUsers(mainCompany.getUsers());
      System.out.println("Shallow Copied Users:");
      User.printUsers(shallowCopyUsers);
      System.out.println("Deep Copied Users:");
      User.printUsers(deepCopyUsers);
      // deep copy company
      printDivider();
      InsuranceCompany deepCopyCompany = mainCompany.clone();
      String storeName = mainCompany.getName();
      mainCompany.setName("Testing Deep Copy");
      System.out.println("Original Company:");
      mainCompany.print();
      System.out.println("Deep Copied Company:");
      deepCopyCompany.print();
      if (mainCompany.getName().equals(deepCopyCompany.getName())) {
        failedCount++;
        printTestStatus(false);
      } else {
        passedCount++;
        printTestStatus(true);
      }
      mainCompany.setName(storeName);

      // lab6
      printDivider();
      System.out.println("TESTING BINARY SERIALIZATION");
      printDivider();
      printDivider();
      String serFileNamePolicies = "policiesTest.ser";
      System.out.println("List of Policies:");
      InsurancePolicy.printPolicies(testUser.getPolicies());
      System.out.println("Saving Policies...");
      InsurancePolicy.save(testUser.getPolicies(), serFileNamePolicies);
      System.out.println("List of Saved Policies:");
      HashMap<Integer, InsurancePolicy> savedPolicies = InsurancePolicy.load(serFileNamePolicies);
      InsurancePolicy.printPolicies(savedPolicies);
      if (savedPolicies.size() > 0) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }
      printDivider();
      String serFileNameUsers = "usersTest.ser";
      System.out.println("List of Users:");
      User.printUsers(mainCompany.getUsers());
      System.out.println("Saving Users...");
      User.save(mainCompany.getUsers(), serFileNameUsers);
      System.out.println("List of Saved Users:");
      HashMap<Integer, User> savedUsers = User.load(serFileNameUsers);
      User.printUsers(savedUsers);
      if (savedUsers.size() > 0) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }
      printDivider();
      String serFileNameCompany = "companyTest.ser";
      System.out.println("Deep Copy of Company:");
      deepCopyCompany.print();
      System.out.println("Saving Deep Copy of Company...");
      deepCopyCompany.save(serFileNameCompany);
      System.out.println("Clone and Re-initialized Company:");
      InsuranceCompany clonedCompany = new InsuranceCompany();
      clonedCompany.load(serFileNameCompany);
      clonedCompany.print();
      if (mainCompany.getName().equals(clonedCompany.getName())) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }

      printDivider();
      System.out.println("TESTING FILES");
      printDivider();
      printDivider();
      String fileNamePolicies = "policies.txt";
      System.out.println("List of Policies:");
      InsurancePolicy.printPolicies(testUser.getPolicies());
      System.out.println("Saving Policies...");
      InsurancePolicy.saveTextFile(testUser.getPolicies(), fileNamePolicies);
      System.out.println("List of Saved Policies:");
      HashMap<Integer, InsurancePolicy> extractedPolicies = InsurancePolicy.loadTextFile(fileNamePolicies);
      InsurancePolicy.printPolicies(extractedPolicies);
      if (extractedPolicies.size() > 0) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }
      printDivider();
      String fileNameUsers = "users.txt";
      System.out.println("List of Users:");
      User.printUsers(mainCompany.getUsers());
      System.out.println("Saving Users...");
      User.saveTextFile(mainCompany.getUsers(), fileNameUsers);
      System.out.println("List of Saved Users:");
      HashMap<Integer, User> extractedUsers = User.loadTextFile(fileNameUsers);
      User.printUsers(extractedUsers);
      if (extractedUsers.size() > 0) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }
      printDivider();
      String fileNameCompany = "company.txt";
      System.out.println("Deep Copy of Company:");
      deepCopyCompany.print();
      System.out.println("Saving Deep Copy of Company...");
      deepCopyCompany.saveTextFile(fileNameCompany);
      System.out.println("Clone and Re-initialized Company:");
      InsuranceCompany clonedCompany2 = new InsuranceCompany();
      clonedCompany2.loadTextFile(fileNameCompany);
      clonedCompany2.print();
      if (mainCompany.getName().equals(clonedCompany2.getName())) {
        passedCount++;
        printTestStatus(true);
      } else {
        failedCount++;
        printTestStatus(false);
      }

    } catch (CloneNotSupportedException e) {
      System.out.println("Cloning not supported! Initial tests Skipped.");
    } catch (PolicyException e) {
      System.out.println("A policy id is out of range! Initial tests Skipped.");
    }

    //lab5
    try {
      System.out.println("Testing out of range policy id");
      new ComprehensivePolicy(1, null, 0, "", null, 0, 0);
      failedCount++;
      printTestStatus(false);
    } catch (PolicyException e) {
      System.out.println(e);
      passedCount++;
      printTestStatus(true);
    }

    try {
      System.out.println("Testing in range policy id");
      new ComprehensivePolicy(InsurancePolicy.generateRandomId(), null, 0, "", null, 0, 0);
      passedCount++;
      printTestStatus(true);
    } catch (PolicyException e) {
      System.out.println(e);
      failedCount++;
      printTestStatus(false);
    }

    System.out.println("Overall Result (" + (passedCount + failedCount) +"): " + GREEN + passedCount + "passed , " + RED + failedCount + "failed" + DEFAULT);
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
  
  public void printUserInformation() {
    System.out.println("\n All Users in Company:");
    for (User user : mainCompany.getUsers().values()) {
      user.print();
      System.out.println();
    } 
    System.out.println();
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
    int policyID = 0;
    System.out.print("Enter policyID: ");
    while (policyID == 0) {
      try {
        policyID = inputReader.nextInt();
        inputReader.nextLine();
        if (user.findPolicy(policyID) == null) {
          System.out.print("Policy not found. Try Again: ");
          policyID = 0;
        }
      } catch (Exception e) {
        System.out.print("Wrong input. Try again: ");
        inputReader.nextLine();
      }
    }
    return policyID;
  }

  public int getRestrictedInt(Integer min, Integer max, String message) {
    int input = 0;
    String range = "(" + min + (max == null ? " or more" : " - " + max) + ")";
    System.out.print(message + range + ": ");
    while (input == 0) {
      try {
        input = inputReader.nextInt();
        inputReader.nextLine();
        if (input < min || (max != null && input > max)) {
          System.out.print("Out of range " + range + ". Try again: ");
          input = 0;
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

  public void printTestStatus(boolean passed) {
    System.out.println((passed ? GREEN + "TEST PASSED" : RED + "TEST FAILED") + DEFAULT);
  }
}