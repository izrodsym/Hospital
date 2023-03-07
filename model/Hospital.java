package Hospital.model;

import Hospital.model.enums.Specialization;
import Hospital.model.facilities.MedicalWard;
import Hospital.model.persons.Doctor;
import Hospital.model.persons.Nurse;
import Hospital.model.persons.Patient;
import Hospital.model.threads.Examination;
import Hospital.model.threads.Visitation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class Hospital
 * Generates a set number of doctors and nurses and departments. Assign nurses in the wards.
 * Contains a list of doctors, a list of each doctor's patients, a list of nurses, a list of hospital departments, a list of waiting patients.
 * ------------
 * When a patient enters the hospital, the patient is placed on the waiting list and an examination method is initiated.
 * Discharges patients from the hospital. Administers medication to patients.
 * ------------
 * Shows patients for discharge.
 * Shows the doctors.
 * Shows the doctors with their patients.
 * Shows available beds in the wards.
 * ------------
 * Starts a thread about discharging people, giving medicine and visiting the doctors.
 */

public class Hospital implements Runnable {
    private static final int DOCTORS_NUMBER = 5;    // Number of doctors to be created
    private static final int NURSE_NUMBER = 8;  // Number of nurses to be created
    // Predefined doctors names
    private final String[] doctorsNames = {"Ivanov", "Petrov", "Kostadinov", "Stoynev", "Goranov", "Dimitrov", "Blagoev", "Bukliev"};
    // Predefined nurses names
    private final String[] nursesNames = {"Kamburova", "Stankova", "Kiriakova", "Asenova", "Tsankova", "Dineva", "Trichkova", "Kaunova"};
    private final MedicalWard[] medicalWards = new MedicalWard[3];  // Array for medical wards
    private final HashMap<Specialization, ArrayList<Doctor>> doctorsList = new HashMap<>(); // Array list for doctors depends on their specifications
    private final ArrayList<Nurse> nursesList = new ArrayList<>();  // Array list for nurses
    private final HashMap<Doctor, ArrayList<Patient>> patientsList = new HashMap<>();   // Array list for patients for each doctor
    private final Queue<Patient> waitingPatients = new ConcurrentLinkedQueue<>();   // List for all patients that are waiting for examination
    private volatile boolean isRunningExamination = false;  // flag for running examination
    private int workingDays = 1;

    /**
     * Constructor
     * Creates predefined number of doctors with random names and specifications
     * Creates predefined numbers of nurses
     * Creates 3 wards and assign nurses to them
     */
    public Hospital() {
        // creating random doctors
        for (int i = 0; i < DOCTORS_NUMBER; i++) {
            Doctor doc = new Doctor(doctorsNames[i % DOCTORS_NUMBER]);   // create a new doctor
            ArrayList<Doctor> newDoctorsList = doctorsList.get(doc.getSpecialization());    //create an array list with all doctors with same specialization
            if (newDoctorsList == null) {   // if list is null create a new one
                newDoctorsList = new ArrayList<>();
            }
            newDoctorsList.add(doc);    // add doctor to new list of existing doctors
            doctorsList.put(doc.getSpecialization(), newDoctorsList);   // replace the list with new one
        }
        // creating random nurses
        for (int i = 0; i < NURSE_NUMBER; i++) {
            nursesList.add(new Nurse(nursesNames[i]));
        }
        // create 3 different medical wards, one for every specialization
        medicalWards[0] = new MedicalWard(Specialization.ORTHOPEDY, 10, 3);
        medicalWards[1] = new MedicalWard(Specialization.CARDIOLOGY, 10, 3);
        medicalWards[2] = new MedicalWard(Specialization.VIROLOGY, 10, 3);

        assignNursesToWards();
    }

    /**
     * Every patient that is waiting for an examination is added to queue, and calling method for examination.
     *
     * @param patient patient for examination.
     */
    public void enterHospital(Patient patient) {
        waitingPatients.add(patient);
        examinePatients();
    }

    /**
     * Each patient who is on the patient waiting list is processed and assigned a doctor according to his complaints.
     * If there is a free doctor, an examination is started. If there is no available doctor, the patient is returned to the waiting queue.
     * In the absence of a doctor for the given complaint, the patient is discharged from the hospital.
     * In the absence of free beds, the patient is discharged from the hospital.
     */
    public void examinePatients() {
        if (waitingPatients.isEmpty() || isRunningExamination) {    // if doctor is busy or no waiting patients terminate method
            return;
        }
        isRunningExamination = true;
        Queue<Patient> newWaitingPatients = new ConcurrentLinkedQueue<>();  // new list of waiting patients who have not been accepted by a doctor

        while (!waitingPatients.isEmpty()) {    // while there is waiting patients
            Patient patient = waitingPatients.poll();   // take the first patient end remove it from list of waiting patients
            ArrayList<Doctor> doctors = doctorsList.get(patient.getSpec()); // take list of doctors with same specification patient needed
            if (doctors == null) {  // if there's no doctor for this specification take out the patient from hospital
                System.out.println("No doctor for this specialization!");
            } else {    // if there is a doctor
                boolean flag = false;   // flag if patient is examined by doctor
                for (Doctor doctor : doctors) { // for every doctor in the list of doctors
                    if (doctor.isFree()) {  // if doctor is free
                        doctor.setFree(false);  // set doctor to busy
                        Examination examination = new Examination(doctor, patient, this); // creates new examination thread
                        examination.start();    // start examination thread

                        // Add patient to a medical ward
                        for (MedicalWard mw : medicalWards) {
                            // check that the medical ward has the same specialty as the patient
                            if (mw.getSpecialization() == patient.getSpec()) {
                                // checks for an available bed and if available admits the patient
                                if (!mw.addPatient(patient)) {  // if no available bed remove patient from hospital
                                    System.out.println("There is no available bed for patient: " + patient);
                                    patient.setDaysOfTreatment(0);
                                } else {     // if patient accepted in ward add it to patient list for every doctor
                                    ArrayList<Patient> newPatientsList = patientsList.get(doctor);  // take doctor's current list of patients

                                    if (newPatientsList == null) {   // if list is null create new one
                                        newPatientsList = new ArrayList<>();
                                    }
                                    newPatientsList.add(patient);   // add patient to the new list
                                    patientsList.put(doctor, newPatientsList);  // add the list to the doctor
                                }
                                break;
                            }
                        }

                        flag = true;    // patient was examined by doctor
                        break;
                    }
                }
                if (!flag) {    // if patient is not examined by doctor
                    newWaitingPatients.add(patient);    // add patient to new list of waiting patients
//                    System.out.println("Patient: " + patient.getName() + " waiting for a doctor.");
                }
            }
        }
        waitingPatients.addAll(newWaitingPatients);   // add new list of waiting patients to the existing one
        isRunningExamination = false;
    }

    /**
     * Call method for every medical ward to discharge patients with zero days left for treatment
     * Adds discharged patients to a list and remove patients from doctors lists
     */
    public void patientsDischarge() {
        ArrayList<Patient> dischargedPatients = new ArrayList<>();  // list for discharged patients
        for (MedicalWard mw : medicalWards) {   // for every medical ward discharge patients and add it to a list
            dischargedPatients.addAll(mw.patientDischarge());
        }

        for (Patient patient : dischargedPatients) {    // for every patient in discharged list
            for (ArrayList<Patient> patients : patientsList.values()) { // each doctor's patient list
                if (patients.contains(patient)) {   // if the patient is present in the doctor's list
                    patients.remove(patient);   // remove it from list
//                    System.out.println("Removed patient: " + patient.getName());
                    break;
                }
            }
        }

    }

    /**
     * Calls patientTakingPills method for every medical ward
     */
    public void patientsTakingPills() {
        for (MedicalWard mw : medicalWards) {
            mw.patientsTakingPills();
        }
    }

    /**
     * Calls doctorVisitation for each doctor, if he is busy set the flag for waiting visitation to true
     */
    public void patientsVisitation() {
        for (Doctor doctor : patientsList.keySet()) {   // Each doctor with patients list
            if (doctor.isFree()) {  // check if doctor is free
                doctor.setFree(false);  // set doctor to busy
                doctorVisitation(doctor);

            } else {
                doctor.setWaitingVisitation(true);
            }
        }

    }

    /**
     * Starts visitation thread for a doctor
     *
     * @param doctor Doctor
     */
    public void doctorVisitation(Doctor doctor) {
        Visitation visitation = new Visitation(doctor, patientsList.get(doctor), this);
        visitation.start();
    }

    /**
     * Assign every nurse in different medical ward
     */
    private void assignNursesToWards() {
        int count = 0;
        for (Nurse nurse : nursesList) {
            medicalWards[count % 3].assignNewNurse(nurse);
            count++;
        }
    }

    /**
     * Shows list of patients to be discharged next day
     */
    public void showPatientsForDischargingNextDay() {
        for (MedicalWard mw : medicalWards) {
            System.out.println("Ward " + mw.getSpecialization() + " have " + mw.getPatientsForDischargingNextDay() + " patients for discharge next day.");
        }
    }

    /**
     * Shows doctors list
     */
    public void showDoctors() {
        for (ArrayList<Doctor> doctors : doctorsList.values()) {
            for (Doctor doctor : doctors) {
                System.out.println(doctor);
            }
        }
    }

    /**
     * Shows number of patients for each doctor and each patient name
     */
    public void showPatients() {
        for (Doctor doctor : patientsList.keySet()) {   // for every doctor with patients
            System.out.println("Dr. " + doctor.getName() + " have " + patientsList.get(doctor).size() + " patients.");
            for (Patient patient : patientsList.get(doctor)) {
                System.out.println("    patient: " + patient.getName());
            }
        }
    }

    /**
     * Shows free beds in every medical ward
     */
    public void showFreeBeds() {
        for (MedicalWard mw : medicalWards) {
            System.out.println("Ward " + mw.getSpecialization() + " have " + mw.getFreeBeds() + " free beds.");
        }
    }

    @Override
    public void run(){
        System.out.println("\n----------------------------------------");
        System.out.println("Work day in hospital: " + workingDays);
        System.out.println("----------------------------------------");
        System.out.printf("\nDischarged patients:\n");
        patientsDischarge();
        System.out.printf("\nMedicine time.\n");
        patientsTakingPills();
        System.out.printf("\nVisitation time.\n");
        patientsVisitation();
        workingDays ++;
    }


}
