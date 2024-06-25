
public class Program {
    
  public static void main(String[] args) {
    InsuranceCompany insuranceCompany = new InsuranceCompany("Demo", null, "admin", "admin", 50);
    fillData(insuranceCompany);
    UserInterface UI = new UserInterface(insuranceCompany);
    UI.mainMenu();

    // Lab Codes
    // static Scanner inputReader = new Scanner(System.in);
    // System.out.println("--START--");

    // // 1) migrate insurance company
    // InsuranceCompany mainCompany = new InsuranceCompany("MetLife Inc", users, "admin", "admin123", 40);

    // // 2) login
    // if (!mainCompany.validateAdmin("wrong", "pass12")) {
    //     System.out.println("Login failed.");
    // }
    // if (mainCompany.validateAdmin("admin", "admin123")) {
    //     System.out.println("Login successful.");
    // }
    // System.out.println();

    // // 3) add users
    // System.out.println("Adding users...");
    // for (User user : users) {
    //     if (mainCompany.addUser(user)) {
    //         System.out.println("User " + user.getName() + " with ID " + user.getUserID() + " added.");
    //     } else {
    //         System.out.println("User with ID " + user.getUserID() + " already exists. (" + mainCompany.findUser(user.getUserID()) + ")");
    //     }
    // }
    // if (mainCompany.addUser("Alice", 4, new Address(12, "North Wood", "345r", "New York"))) {
    //     System.out.println("User Alice added.");
    // }
    // if (!mainCompany.addUser("Alice", 3, new Address(12, "North Wood", "345r", "New York"))) {
    //     System.out.println("User with ID 3 already exists. (" + mainCompany.findUser(3).getName() + ")");
    // }
    // System.out.println();

    // // 4) add policies
    // // for user 1
    // System.out.println("Adding policies for user 1...");
    // addPolicyToCompany(mainCompany, 1, compPolicy1);
    // addPolicyToCompany(mainCompany, 1, thirdPolicy1);
    // // for user 2
    // System.out.println("Adding policies for user 2...");
    // addPolicyToCompany(mainCompany, 2, compPolicy2);
    // addPolicyToCompany(mainCompany, 2, thirdPolicy2);
    // // for user 3
    // System.out.println("Adding policies for user 3...");
    // addPolicyToCompany(mainCompany, 3, compPolicy3);
    // addPolicyToCompany(mainCompany, 3, thirdPolicy3);
    // System.out.println();
    // // wrong user
    // addPolicyToCompany(mainCompany, 9999, compPolicy3);
    // // duplicate policy
    // addPolicyToCompany(mainCompany, 1, compPolicy1);
    // System.out.println();

    // // 5) create policies
    // System.out.println("Creating policies...");
    // createComprehensivePolicyInCompany(mainCompany, 1, "New Comprehensive Policy", 7, car1, 2, new MyDate(2024, 5, 1), 27, 3);
    // createThirdPartyPolicyInCompany(mainCompany, 1, "New Third Party Policy", 7, car1, 2, new MyDate(2024, 5, 1), "test comment");
    // System.out.println();
    // // wrong user
    // createComprehensivePolicyInCompany(mainCompany, 9999, "asd", 7, car1, 2, new MyDate(2024, 5, 1), 27, 3);
    // createThirdPartyPolicyInCompany(mainCompany, 9999, "asd", 7, car1, 2, new MyDate(2024, 5, 1), "sdf");
    // // duplicate policy
    // createComprehensivePolicyInCompany(mainCompany, 1, "asd", 7, car1, 2, new MyDate(2024, 5, 1), 27, 3);
    // createThirdPartyPolicyInCompany(mainCompany, 1, "asd", 7, car1, 2, new MyDate(2024, 5, 1), "sdf");
    // System.out.println();

    // // 6) ask for userID
    // System.out.print("To view a user details, please ");
    // int userID = getUserID(mainCompany, inputReader);
    // mainCompany.findUser(userID).print();
    // System.out.println();

    // // 7) find policy
    // System.out.print("To view a policy details of user, please ");
    // int userID2 = getUserID(mainCompany, inputReader);
    // int policyID = getPolicyID(mainCompany.findUser(userID2), inputReader);
    // InsurancePolicy foundPolicy = mainCompany.findPolicy(userID2, policyID);
    // if (foundPolicy == null) System.out.println("Policy not found.");
    // else foundPolicy.print();
    // System.out.println();

    // // 8) print all users
    // System.out.println("All users: ");
    // printAllUsers(mainCompany);
    // System.out.println();

    // // 9) price rise and print all updated users
    // System.out.println("Rising car price...");
    // System.out.println("All users after car price rise: ");
    // mainCompany.carPriceRise(10);
    // printAllUsers(mainCompany);
    // System.out.println();

    // // 10) print total payment of user by id
    // System.out.print("To view total payment of a user, please ");
    // double totalPayment = mainCompany.calcTotalPayments(getUserID(mainCompany, inputReader));
    // System.out.println("Total payment of this user: $" + totalPayment);
    // System.out.println();

    // // 11) print all total payments
    // System.out.println("All users' total payment: ");
    // for (User user : mainCompany.getUsers()) {
    //     System.out.println("User " + user.getName() + " total payment: $" + mainCompany.calcTotalPayments(user.getUserID()));
    // }
    // System.out.println();

    // // 12) print all policies
    // System.out.println("All policies: ");
    // ArrayList<InsurancePolicy> allPolicies = mainCompany.allPolicies();
    // InsurancePolicy.printPolicies(allPolicies);
    // System.out.println();

    // // 13) filter by expiryDate and userID
    // System.out.print("To filter policies of an specific user by expiry date, please ");
    // int userIDForExpiry = getUserID(mainCompany, inputReader);
    // MyDate expiryDate = getDate(inputReader);
    // ArrayList<InsurancePolicy> expiredPolicies = mainCompany.filterByExpiryDate(userIDForExpiry, expiryDate);
    // InsurancePolicy.printPolicies(expiredPolicies);
    // System.out.println();

    // // 14) filter by car model
    // System.out.println("To filter policies, please Enter car model: ");
    // String carModel = inputReader.nextLine();
    // ArrayList<InsurancePolicy> filteredPoliciesByCarModel = mainCompany.filterByCarModel(carModel);
    // System.out.println(filteredPoliciesByCarModel.size() > 0 ? "List of policies containing '" + carModel + "':" : "No policy found!");
    // InsurancePolicy.printPolicies(filteredPoliciesByCarModel);
    // System.out.println();

    // // 15) filter by only expiryDate
    // System.out.print("To filter all policies by expiry date, please ");
    // MyDate onlyExpiryDate = getDate(inputReader);
    // ArrayList<InsurancePolicy> allExpiredPolicies = mainCompany.filterByExpiryDate(onlyExpiryDate);
    // InsurancePolicy.printPolicies(allExpiredPolicies);
    // System.out.println();

    // // 16) update a user address
    // System.out.print("To update a user's address, ");
    // User validUser = mainCompany.findUser(getUserID(mainCompany, inputReader));
    // System.out.print("Enter new Street Number: ");
    // int streetNum = inputReader.nextInt();
    // inputReader.nextLine();
    // System.out.print("Enter new Street: ");
    // String street = inputReader.nextLine();
    // System.out.print("Enter new Suburb: ");
    // String suburb = inputReader.nextLine();
    // System.out.print("Enter new City: ");
    // String city = inputReader.nextLine();
    // Address newAddress = new Address(streetNum, street, suburb, city);
    // validUser.setAddress(newAddress);

    // System.out.println("--END--");

    // inputReader.close();
  }

	public static void fillData(InsuranceCompany insuranceCompany) {
    // migrate cars
    Car car1 = new Car("Benz G Class", CarType.LUX, 1998, 2770);
    Car car2 = new Car("Porsche 911", CarType.SED, 1994, 1433);
    Car car3 = new Car("Lamborghini", CarType.SUV, 2003, 3200);
    Car car4 = new Car("Benz G Class", CarType.LUX, 2021, 4563);
    Car car5 = new Car("Porsche 911", CarType.HATCH, 2018, 2311);
    Car car6 = new Car("Lamborghini", CarType.HATCH, 2005, 5677);
    
    // migrate users
    User user1 = new User("John Doe", "john_doe", "john1234", new Address(21, "Eastern", "J15r", "Manhattan"), null, null);
    User user2 = new User("Foo Bar", "foo_bar", "foo1234", new Address(13, "Golden State", "P21W", "New York"), null, null);
    User user3 = new User("James Bond", "james_bond", "james1234", new Address(15, "Southern", "4DS2", "Toronto"), null, null);
    try {
      // migrate policies
      ComprehensivePolicy compPolicy1 = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), car1, 1, "Simple Policy 1", new MyDate(2024, 6, 19), 24, 1);
      ComprehensivePolicy compPolicy2 = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), car2, 2, "Simple Policy 2", new MyDate(2024, 6, 20), 24, 1);
      ComprehensivePolicy compPolicy3 = new ComprehensivePolicy(InsurancePolicy.generateRandomId(), car3, 4, "Simple Policy 3", new MyDate(2024, 6, 21), 24, 1);
      ThirdPartyPolicy thirdPolicy1 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), car4, 1, "Industrial Insurance 1", new MyDate(2024, 6, 19), "1 year full warranty.");
      ThirdPartyPolicy thirdPolicy2 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), car5, 4, "Industrial Insurance 2", new MyDate(2024, 6, 20), "2 year full warranty.");
      ThirdPartyPolicy thirdPolicy3 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), car6, 2, "Industrial Insurance 3", new MyDate(2024, 6, 21), "3 year full warranty.");
      user1.addPolicy("john_doe", "john1234", compPolicy1);
      user1.addPolicy("john_doe", "john1234", thirdPolicy1);
      user2.addPolicy("foo_bar", "foo1234", compPolicy2);
      user2.addPolicy("foo_bar", "foo1234", thirdPolicy2);
      user3.addPolicy("james_bond", "james1234", compPolicy3);
      user3.addPolicy("james_bond", "james1234", thirdPolicy3);
    } catch (PolicyException e) {
      System.out.println(e);
    } catch (PolicyHolderNameException e) {
      System.out.println(e);
    } 

    insuranceCompany.addUser("admin", "admin", user1);
    insuranceCompany.addUser("admin", "admin", user2);
    insuranceCompany.addUser("admin", "admin", user3);
	}

  // public static void addPolicyToCompany(InsuranceCompany company, int userID, InsurancePolicy policy) {
  //   if (company.addPolicy(userID, policy)) {
  //     System.out.println("Policy '" + policy.getPolicyHolderName() + "' with ID " + policy.getId() + " added.");
  //   } else {
  //     if (company.findUser(userID) == null) {
  //       System.out.println("User with ID " + userID + " not found.");
  //     } else {
  //       System.out.println("Policy with ID " + userID + " already exists. (" + company.findPolicy(userID, policy.getId()).getPolicyHolderName() + ")");
  //     }
  //   }
  // }

  // public static void createComprehensivePolicyInCompany(InsuranceCompany company, int userID, String policyHolderName, Car car, int numberOfClaims, MyDate expiryDate, int driverAge, int level) {
  //   if (company.createComprehensivePolicy(userID, policyHolderName, car, numberOfClaims, expiryDate, driverAge, level)) {
  //     System.out.println("Policy '" + policyHolderName + "' added.");
  //   } else {
  //     if (company.findUser(userID) == null) {
  //       System.out.println("User with ID " + userID + " not found.");
  //     } else {
  //       System.out.println("Policy with this ID already exists.");
  //     }
  //   }
  // }

  // public static void createThirdPartyPolicyInCompany(InsuranceCompany company, int userID, String policyHolderName, Car car, int numberOfClaims, MyDate expiryDate, String comments) {
  //   if (company.createThirdPartyPolicy(userID, policyHolderName, car, numberOfClaims, expiryDate, comments)) {
  //     System.out.println("Policy '" + policyHolderName + "' added.");
  //   } else {
  //     if (company.findUser(userID) == null) {
  //       System.out.println("User with ID " + userID + " not found.");
  //     } else {
  //       System.out.println("Policy with this ID already exists.");
  //     }
  //   }
  // }

  // public static void printAllUsers(InsuranceCompany company) {
  //   System.out.println("\n All Users in Company:");
  //   for (User user : company.getUsers().values()) {
  //     user.print();
  //     System.out.println();
  //   } 
  //   System.out.println();
  // }
}
