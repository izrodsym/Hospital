package Hospital.model.persons;

import Hospital.model.enums.Specialization;

import java.util.Random;

/**
 * Class Doctor
 * Contains the details of the doctor, his name, phone number, specialization
 * and information about whether he is currently busy and whether he has a visitation pending.
 */
public class Doctor {
    private final String name;
    private final String phoneNumber;
    private final Specialization specialization;
    private boolean isFree = true;  // True if doctor is free and don't have visitation or examination
    private boolean waitingVisitation = false;  // True if it's time for visitation, but he is on examination

    /**
     * Constructor that asks for the name of the doctor and generates random phone number and chooses a random specialization
     * @param name name of the doctor
     */
    public Doctor(String name){
        Random rand = new Random();
        this.name = name;
        phoneNumber = "088" + (rand.nextInt(2999999) + 7000000);    // create random phone number
        Specialization[] specs = Specialization.values();
        specialization = specs[rand.nextInt(specs.length)];     // chose random specialization
    }

    /**
     *
      * @return name, specialization and phone of the doctor
     */
    @Override
    public String toString() {
        return "dr. " + name + ", specialization: " + specialization.toString().toLowerCase() + ", phone: " + phoneNumber;
    }

    public String getName() {
        return name;
    }

    /**
     *
     * @return specialization of the doctor
     */
    public Specialization getSpecialization() {
        return specialization;
    }

    public boolean isFree() {
        return isFree;
    }
    public void setFree(boolean isFree){
        this.isFree = isFree;
    }

    public boolean isWaitingVisitation() {
        return waitingVisitation;
    }

    public void setWaitingVisitation(boolean waitingVisitation) {
        this.waitingVisitation = waitingVisitation;
    }
}
