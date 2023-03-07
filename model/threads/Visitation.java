package Hospital.model.threads;

import Hospital.model.Hospital;
import Hospital.model.persons.Doctor;
import Hospital.model.persons.Patient;

import java.util.ArrayList;

/**
 * A thread that goes around all the patients of a given doctor and for each patient falls asleep for 1 second.
 * At the end of the execution, it calls a method that checks whether there are patients waiting for an examination.
 */
public class Visitation extends Thread {
    private final Doctor doctor;
    private final ArrayList<Patient> patients = new ArrayList<>();
    private final Hospital hospital;

    /**
     * Constructor
     * @param doctor doctor makes visitation
     * @param patients list of patients for visitation
     * @param hospital hospital
     */
    public Visitation(Doctor doctor, ArrayList<Patient> patients, Hospital hospital) {
        this.doctor = doctor;
        this.hospital = hospital;
        this.patients.addAll(patients);
    }

    @Override
    public void run() {
        for (Patient patient:patients) {    // For every patient in the list
            System.out.println("Dr. " + doctor.getName() + " is visiting " + patient.getName() + " in room " + patient.getRoom());
            try {
                sleep(1000);    // sleep for one second
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        doctor.setFree(true);   // set availability of doctor
        hospital.examinePatients(); // method that examine waiting patients
    }
}
