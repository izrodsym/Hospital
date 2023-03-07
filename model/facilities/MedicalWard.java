package Hospital.model.facilities;

import Hospital.model.enums.Gender;
import Hospital.model.enums.Specialization;
import Hospital.model.persons.Nurse;
import Hospital.model.persons.Patient;

import java.util.ArrayList;

/**
 * Class for a medical ward
 * Needs number of rooms, number of beds in the room and specialization.
 * Store patients and their location by room and bed.
 * Store nurses serving patients.
 * Adds patients, only patients of the same gender can be in a room. If the beds are full, the patient is not added.
 * Remove patients if their treatment days are over.
 * Administering medication to patients. Each nurse serves one room, if there are more rooms than nurses, they rotate on a rotational basis.
 */
public class MedicalWard {
    private final Specialization specialization;
    private final Patient[][] patients; //Matrix, rooms and beds for patients
    private final ArrayList<Nurse> nursesList = new ArrayList<>();  // List of nurses in the ward
    private int patientsForDischargingNextDay = 0;

    /**
     * Constructor that sets a medical ward
     * @param specialization of the ward
     * @param rooms number of rooms
     * @param bedsInRoom number of beds in the room
     */
    public MedicalWard(Specialization specialization, int rooms, int bedsInRoom) {
        this.specialization = specialization;
        patients = new Patient[rooms][bedsInRoom];
    }

    /**
     * A method that adds a patient to a medical ward. Checks for available beds in the room and for patients of the same sex.
     *
     * @param patient the patient to be added to the ward.
     * @return true if patient is added to ward and false if there are no free beds in the ward.
     */
    public boolean addPatient(Patient patient) {
        Gender patientGender = patient.getGender();
        int freeRoom = -1;

        // loop for every room in ward
        for (int i = 0; i < patients.length; i++) {
            boolean flagForGender = false; // true if there is other patient in the room with same gender
            int bed = -1;   // index of last free bed, -1 if there are no free beds
            int freeBeds = 0;   // count of free beds in the room

            // loop for every bed in the room
            for (int j = 0; j < patients[i].length; j++) {

                // if bed is empty rise the count of free beds and take index of the bed.
                // else if patient in the bed is with same sex flag for gender become true
                if (patients[i][j] == null) {
                    freeBeds++;
                    bed = j;
                } else if (patients[i][j].getGender() == patientGender) {
                    flagForGender = true;
                }

            }

            // if there are free beds in room and patients inside are with same sex, patient is accepted to room and method returns true
            if (bed > -1 && flagForGender) {
                patients[i][bed] = patient;
                patient.setRoom(i);
                return true;
            }

            // if room is empty, takes its index
            if (freeBeds == patients[i].length) {
                freeRoom = i;
            }

        }
        // if there is free room, patient is accepted there and method returns true
        if (freeRoom > -1) {
            patients[freeRoom][0] = patient;
            patient.setRoom(freeRoom);
            return true;
        }
        // if there are no free beds in rooms method returns false
        return false;
    }

    /**
     * Removing the patient from ward. Check every room in ward for the patient and remove him.
     *
     * @param patient to be removed from ward.
     * @return true if removing is successful, false if there is no such patient.
     */
    public boolean removePatient(Patient patient) {
        // loop for every room in ward
        for (int i = 0; i < patients.length; i++) {
            // loop for every bed in room
            for (int j = 0; j < patients[i].length; j++) {

                // if patient match, remove him from array and return true
                if (patients[i][j] == patient) {
                    patients[i][j] = null;
                    return true;
                }
            }
        }
        System.out.println("No such patient in the ward.");
        return false;
    }

    /**
     * This method must be start at the beginning of every day. For every patient in ward it decrease number of days for treatment. If days are 0 or negative, the patient is discharged from ward.
     * @return list of discharged patients
     */
    public ArrayList<Patient> patientDischarge() {
        patientsForDischargingNextDay = 0;
        ArrayList<Patient> dischargedPatients = new ArrayList<>();
        // loop for every room in ward
        for (int i = 0; i < patients.length; i++) {
            // loop for every bed in room
            for (int j = 0; j < patients[i].length; j++) {
                // check if there is anu patient in bed
                if (patients[i][j] != null) {
                    // if days of treatment are above 0, decrease it by one. Else discharge patient
                    if (patients[i][j].getDaysOfTreatment() > 0) {
                        patients[i][j].setDaysOfTreatment(patients[i][j].getDaysOfTreatment() - 1);
                        // count patients that will be discharged next day
                        if(patients[i][j].getDaysOfTreatment() == 0){
                            patientsForDischargingNextDay ++;
                        }

                    } else {
                        System.out.println("Patient " + patients[i][j].getName() + ", sex: " + patients[i][j].getGender().toString().toLowerCase() + ", specialization: " + patients[i][j].getSpec().toString().toLowerCase() + " is discharged.");
                        dischargedPatients.add(patients[i][j]); // add patient to return list
                        patients[i][j] = null;
                    }
                }
            }
        }
        return dischargedPatients;
    }

    /**
     * Every nurse in the ward gives a medicine to patients in a separate room
     */
    public void patientsTakingPills(){
        int nursesNumber = nursesList.size();   // takes number of nurses in ward
        if (nursesNumber < 1){  // if there's no nurses in ward abort method
            System.out.println("No nurse to give a pills.");
            return;
        }
        int count = 0;  // count for nurses in ward
        // loop for every room in ward
        for (int i = 0; i < patients.length; i++) {
            Nurse nurse = nursesList.get(count%nursesNumber);   // for every room we have different nurse
            // loop for every bed in room
            for (int j = 0; j < patients[i].length; j++) {
                // check if there is any patient in bed
                if (patients[i][j] != null) {
                    System.out.println("Nurse " + nurse.getName() + " give medicine to patient " + patients[i][j].getName() + " in room " + (i+1) + " section " + specialization);
                }
            }
            count ++;
        }


    }

    /**
     * Add new nurse to the array list of nurses
     * @param nurse to be added
     */
    public void assignNewNurse(Nurse nurse){
        this.nursesList.add(nurse);
    }

    /**
     *
     * @return specialization of current medical ward.
     */
    public Specialization getSpecialization() {
        return specialization;
    }

    /**
     * Count every unoccupied bed in the ward
     * @return count of unoccupied beds in the ward
     */
    public int getFreeBeds(){
        int count = 0;
        // loop for every room in ward
        for (Patient[] room : patients) {
            // loop for every bed in room
            for (Patient patient : room) {
                // check if there is patient in the bed
                if (patient == null) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return number of patients for discharging on next day
     */
    public int getPatientsForDischargingNextDay() {
        return patientsForDischargingNextDay;
    }
}
