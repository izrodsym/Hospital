package Hospital.demo;

import Hospital.model.Hospital;
import Hospital.model.persons.Patient;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class DemoHospital {

    public static void main(String[] args){
        Random rand = new Random();

        Hospital hospital = new Hospital();
        hospital.showDoctors();

        ScheduledExecutorService hospitalDay = Executors.newSingleThreadScheduledExecutor();
        hospitalDay.scheduleAtFixedRate(hospital, 5000, 5000, TimeUnit.MILLISECONDS);    //schedule a starting day in hospital
        Runnable canceller = () -> hospitalDay.shutdown();
        hospitalDay.schedule(canceller, 50000, TimeUnit.MILLISECONDS);  // stop schedule after 10 cycles

        // create patients
        ArrayList<Patient> patients = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            patients.add(new Patient());
        }

        // admits patients to the hospital
        int counter = 1;
        try {
            for (Patient patient : patients) {
                if ((counter%5) == 0){
                    sleep(3000);
                }
                sleep(rand.nextInt(30));
                hospital.enterHospital(patient);
                counter ++;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // shows additional info at some point
        System.out.println("Number of free beds:");
        hospital.showFreeBeds();
        System.out.println();
        System.out.println("Show patients:");
        hospital.showPatients();
        System.out.println();
        System.out.println("Show patients for each doctor:");
        hospital.showPatientsForDischargingNextDay();
    }

}
