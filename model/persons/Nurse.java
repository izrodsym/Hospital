package Hospital.model.persons;

import java.util.Random;

public class Nurse {
    private final String name;
    private final int yearsOfExperience;
    private final String phoneNumber;

    /**
     * Constructor that asks for the name of the nurse and generates random year of experience, random phone number and chooses a random specialization
     * @param name name of the nurse
     */
    public Nurse(String name){
        Random rand = new Random();
        this.name = name;
        this.yearsOfExperience = rand.nextInt(30);
        phoneNumber = "088" + (rand.nextInt(2999999) + 7000000);    // create random phone number
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "nurse " + name + ", years of experience: " + yearsOfExperience + ", phone: " + phoneNumber;
    }
}
