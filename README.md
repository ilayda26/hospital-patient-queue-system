# hospital-patient-queue-simulation
PROJECT OVERVIEW
This project is a Java-based console application simulates a patient tracker and a triage system for a hospital. In order to reflect actual hospital workflows,the system ranks patients according to medical urgency rather than arrival time. Patients are registered, assesed based on symptoms and stress levels, priorities given to them and finally treated by available and qualified physicians.
This project is intended to illustrate object-oriented programming (OOP) concepts and clean architecture design.

KEY FEATURES
-Patient registration (Normal and Emergency patients)
-Priority-based queue management
-Stress score calculation using pain and anxiety levels
-Age-based vulnerability adjustment
-Symptom-based doctor specialization matching
-Multiple doctor specializations
-Doctor availability simulation
-Daily treatment report generation and file saving
-Console-based interactive menu

TECHNOLOGYS USED
-Programming Language: Java
-Java Concepts:
 -Classes and Objects
 -Inheritance
 -Polymorphism
 -Encapsulation
 -Packages
 -Collections (List, Map)
 -File I/O

PROJECT STRUCTURE
app/        → Main application (HospitalSystem)
model/      → Core data models (Patient, Doctor, etc.)
service/    → Business logic (QueueManager, StressCalculator, SymptomMatcher)
util/       → Utility classes (BillingCalculator, StoryGenerator, TipGenerator)

HOW TO RUN THE PROGRAM
1)Open the project in any Java IDE (IntelliJ IDEA, Eclipse, NetBeans)
2)Ensure JDK is installed and configured
3)Run the HospitalSystem.java file located in the app package
4)Follow the console menu instructions to interact with the system

NOTES
-The system is a simulation and does not use real medical data.
-Emergency patients include an additional condition description entered by the user.
-If symptoms do not match any specialization, the patient is assigned to a General doctor.
-The system safely handles unknown symptoms and invalid input.



