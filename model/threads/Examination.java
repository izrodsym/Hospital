package Hospital.model.threads;

import Hospital.model.Hospital;
import Hospital.model.persons.Doctor;
import Hospital.model.persons.Patient;

import java.util.Random;

/**
 * A thread that accepts a patient and a doctor who gives a diagnosis and determines the length of stay in the hospital randomly.
 * At the end of the execution, it checks if there is a visitation during this time and calls the visitation method,
 * after that it calls a method that checks whether there are patients waiting for an examination.
 */
public class Examination extends Thread {

    private final Doctor doctor;
    private final Patient patient;
    private final Hospital hospital;

    /**
     *
     * @param doctor doctor makes examination
     * @param patient patient for examination
     * @param hospital hospital
     */
    public Examination(Doctor doctor, Patient patient, Hospital hospital) {
        this.doctor = doctor;
        this.patient = patient;
        this.hospital = hospital;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int dot = (rand.nextInt(3) + 3);    // random days for treatment
        System.out.println("Dr. " + doctor.getName() + " is examining " + patient);
        patient.setDaysOfTreatment(dot);    // set days for treatment
        try {
            sleep(1000);    // sleep for a second
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (doctor.isWaitingVisitation()) { // if there is a visitation at the same time run visitation
            doctor.setWaitingVisitation(false); // set waiting for visitation as false
            hospital.doctorVisitation(doctor); // run visitation
        }
        doctor.setFree(true);   // set availability of doctor
        hospital.examinePatients(); // method that examine waiting patients
    }
}
