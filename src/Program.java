
public class Program {
    
  public static void main(String[] args) {
    InsuranceCompany insuranceCompany = new InsuranceCompany("Demo", null, "admin", "admin", 50);
    fillData(insuranceCompany);
    // UserInterface UI = new UserInterface(insuranceCompany);
    // UI.mainMenu();
    new Login(insuranceCompany).setVisible(true);
            
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
    // sample cars
    Car carToyota = new Car("Toyota Camry", CarType.LUX, 1998, 0);
    Car carNissan = new Car("Nissan Dualis", CarType.SED, 1994, 0);
    Car carFord = new Car("Ford Ranger", CarType.SUV, 2003, 0);
    Car carPorsche = new Car("Porsche Cayenne", CarType.LUX, 2021, 0);
    Car carFerrari = new Car("Ferrari 488", CarType.HATCH, 2018, 0);
    
    // migrate users
    User userSara = new User("Sara", "john1234", new Address(21, "xxx", "xxx", "Wollongong"), null, null);
    User userJohn = new User("John", "foo1234", new Address(13, "xxx", "xxx", "Wollongong"), null, null);
    User userRobert = new User("Robert", "james1234", new Address(15, "xxx", "xxx", "Sydney"), null, null);
    User userAlex = new User("Alex", "james1234", new Address(15, "xxx", "xxx", "Melbourne"), null, null);

    // migrate policies
    try {
      // Sara
      Car carToyota200 = new Car(carToyota);
      carToyota200.setPrice(20000);
      Car carToyota500 = new Car(carToyota);
      carToyota500.setPrice(50000);
      Car carToyota300 = new Car(carToyota);
      carToyota300.setPrice(30000);
      Car carNissan1000 = new Car(carNissan);
      carNissan1000.setPrice(100000);
      ThirdPartyPolicy saraPolicy1 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carToyota200, 0, "Asara Policy", new MyDate(2025, 1, 19), "1 year full warranty.");
      ThirdPartyPolicy saraPolicy2 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carToyota500, 0, "Dsara Policy", new MyDate(2026, 2, 20), "2 year full warranty.");
      ThirdPartyPolicy saraPolicy3 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carToyota300, 0, "Csara Policy", new MyDate(2027, 1, 21), "3 year full warranty.");
      ThirdPartyPolicy saraPolicy4 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carNissan1000, 0, "Bsara Policy", new MyDate(2028, 1, 22), "4 year full warranty.");
      userSara.addPolicy(userSara.getUserID(), userSara.getPassword(), saraPolicy1);
      userSara.addPolicy(userSara.getUserID(), userSara.getPassword(), saraPolicy2);
      userSara.addPolicy(userSara.getUserID(), userSara.getPassword(), saraPolicy3);
      userSara.addPolicy(userSara.getUserID(), userSara.getPassword(), saraPolicy4);

      // John
      Car carNissan500 = new Car(carNissan);
      carNissan500.setPrice(50000);
      Car carToyota100 = new Car(carToyota);
      carToyota100.setPrice(10000);
      Car car2Nissan1000 = new Car(carNissan);
      car2Nissan1000.setPrice(100000);
      Car carFord2000 = new Car(carFord);
      carFord2000.setPrice(200000);
      ThirdPartyPolicy johnPolicy1 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carNissan500, 0, "Ajohn Policy", new MyDate(2025, 1, 19), "1 year full warranty.");
      ThirdPartyPolicy johnPolicy2 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carToyota100, 0, "Djohn Policy", new MyDate(2026, 2, 20), "2 year full warranty.");
      ThirdPartyPolicy johnPolicy3 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), car2Nissan1000, 0, "Cjohn Policy", new MyDate(2027, 1, 21), "3 year full warranty.");
      ThirdPartyPolicy johnPolicy4 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carFord2000, 0, "Bjohn Policy", new MyDate(2028, 1, 22), "4 year full warranty.");
      userJohn.addPolicy(userJohn.getUserID(), userJohn.getPassword(), johnPolicy1);
      userJohn.addPolicy(userJohn.getUserID(), userJohn.getPassword(), johnPolicy2);
      userJohn.addPolicy(userJohn.getUserID(), userJohn.getPassword(), johnPolicy3);
      userJohn.addPolicy(userJohn.getUserID(), userJohn.getPassword(), johnPolicy4);

      // Robert
      Car carFord150 = new Car(carFord);
      carFord150.setPrice(15000);
      Car car3Nissan1000 = new Car(carNissan);
      car3Nissan1000.setPrice(100000);
      Car carToyota5000 = new Car(carToyota);
      carToyota5000.setPrice(500000);
      Car carPorsche400 = new Car(carPorsche);
      carPorsche400.setPrice(40000);
      ThirdPartyPolicy robertPolicy1 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carFord150, 0, "Arobert Policy", new MyDate(2025, 1, 19), "1 year full warranty.");
      ThirdPartyPolicy robertPolicy2 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), car3Nissan1000, 0, "Drobert Policy", new MyDate(2026, 2, 20), "2 year full warranty.");
      ThirdPartyPolicy robertPolicy3 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carToyota5000, 0, "Crobert Policy", new MyDate(2027, 1, 21), "3 year full warranty.");
      ThirdPartyPolicy robertPolicy4 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carPorsche400, 0, "Brobert Policy", new MyDate(2028, 1, 22), "4 year full warranty.");
      userRobert.addPolicy(userRobert.getUserID(), userRobert.getPassword(), robertPolicy1);
      userRobert.addPolicy(userRobert.getUserID(), userRobert.getPassword(), robertPolicy2);
      userRobert.addPolicy(userRobert.getUserID(), userRobert.getPassword(), robertPolicy3);
      userRobert.addPolicy(userRobert.getUserID(), userRobert.getPassword(), robertPolicy4);
      
      // Alex
      Car carFerrari500 = new Car(carFerrari);
      carFerrari500.setPrice(50000);
      Car car4Nissan1000 = new Car(carNissan);
      car4Nissan1000.setPrice(100000);
      ThirdPartyPolicy alexPolicy1 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), carFerrari500, 0, "Balex Policy", new MyDate(2027, 1, 21), "3 year full warranty.");
      ThirdPartyPolicy alexPolicy2 = new ThirdPartyPolicy(InsurancePolicy.generateRandomId(), car4Nissan1000, 0, "Aalex Policy", new MyDate(2028, 1, 22), "4 year full warranty.");
      userAlex.addPolicy(userAlex.getUserID(), userAlex.getPassword(), alexPolicy1);
      userAlex.addPolicy(userAlex.getUserID(), userAlex.getPassword(), alexPolicy2);


    } catch (PolicyException e) {
      System.out.println(e);
    } catch (PolicyHolderNameException e) {
      System.out.println(e);
    } 

    insuranceCompany.addUser(insuranceCompany.getAdminUsername(), insuranceCompany.getAdminPassword(), userSara);
    insuranceCompany.addUser(insuranceCompany.getAdminUsername(), insuranceCompany.getAdminPassword(), userJohn);
    insuranceCompany.addUser(insuranceCompany.getAdminUsername(), insuranceCompany.getAdminPassword(), userRobert);
    insuranceCompany.addUser(insuranceCompany.getAdminUsername(), insuranceCompany.getAdminPassword(), userAlex);
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
