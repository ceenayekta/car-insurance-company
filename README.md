## Insurance Company Template
A simple Insurance Company Java Template for easier 
> method `testCode` located on `UserInterface`

## Classes Structure
All classes are located in ./src.
You can find classes' relations below:

- `InsuranceCompany`: Each company have multiple `Users` 
- `User`: Each user can have `Address` multiple `Policies` (either `ThirdPartyPolicy` or `ComprehensivePolicy`)
- `Address`: Contains city details of users
- `Policy`: Each policy can have a `Car` and expiry date (`MyDate`) 
- `ThirdPartyPolicy`: Inherits all policy features + comments
- `ComprehensivePolicy`: Inherits all policy features + level and driver's age (effective on payment premium)
- `Car`: Each car can have a `CarType`
- `MyDate`: Contains year/month/day
- `UserInterface`: A simple UI containing menus to work around
- `Program`: 

To contribute, you can clone all classes except `UserInterface` and `Program` add your own UI and program. 

## Purpose
This project is written for college assignment.  
