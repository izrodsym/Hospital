package Hospital.model.persons;

import Hospital.model.enums.Gender;
import Hospital.model.enums.Specialization;

import java.util.Random;

/**
 * Class patient
 * It stores the patient's data, name, age, gender, phone number, illness, remaining days of treatment and the room in which he is accommodated.
 */
public class Patient {
    // Predefined names for mens
    private final String[] namesM = {"Peter", "Georg", "Asen", "Ivan", "Martin", "Anton", "Stanislav", "Pavel", "Naum", "Kiril", "Milan", "Kalin", "Alexander"};
    // predefined names for women
    private final String[] namesF = {"Sara", "Anastasia", "Maria", "Martina", "Isabela", "Antonia", "Alexandra", "Veselina", "Kalina", "Stefka", "Milena", "Ina", "Penka"};
    private final String name;
    private final String phoneNumber;
    private final int age;
    private final Gender gender;
    private final Specialization spec;  //specialization patient needs
    private int daysOfTreatment = 0;
    private int room;   //room number in hospital

    /**
     * Constructor that generates random gander, name depends on gender, phone number, age and specialization patient needs.
     */
    public Patient() {
        Random rand = new Random();
        Gender[] genders = Gender.values();
        gender = genders[rand.nextInt(genders.length)];     // chose random gender
        if (gender == Gender.MALE) {     // chose random name depends on gender
            this.name = namesM[rand.nextInt(namesM.length)];
        } else {
            this.name = namesF[rand.nextInt(namesF.length)];
        }
        phoneNumber = "088" + (rand.nextInt(2999999) + 7000000);    // create random phone number
        age = rand.nextInt(99);
        Specialization[] specs = Specialization.values();
        spec = specs[rand.nextInt(specs.length)];     // chose random specialization
    }

    public Gender getGender() {
        return this.gender;
    }

    @Override
    public String toString() {
        return "Patient: " + name + ", phone number: " + phoneNumber + ", age: " + age + ", sex: " + gender.toString().toLowerCase();
    }

    public String getName() {
        return name;
    }

    /**
     * Days patient to be in hospital
     *
     * @param daysOfTreatment number of remaining days in hospital
     */
    public void setDaysOfTreatment(int daysOfTreatment) {
        this.daysOfTreatment = daysOfTreatment;
    }

    /**
     * @return remaining days in hospital
     */
    public int getDaysOfTreatment() {
        return daysOfTreatment;
    }

    public Specialization getSpec() {
        return spec;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getRoom() {
        return room;
    }
}
