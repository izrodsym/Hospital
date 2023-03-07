# Hospital
To implement a model of a hospital.

Doctors and nurses work in the hospital. Doctors have a name, phone number and specialty. Nurses have a name, years of service and a phone number.

Patients can be admitted to the hospital, and upon admission each patient is issued a card with the hospital data, which contains the name, age and telephone number. Also, the patient is assigned an attending doctor from among those available at the moment. If there is no doctor available, the patient must wait until one becomes available. The attending doctor prepares a treatment plan, which contains a diagnosis and a list of medications that the patient must take. The treatment continues for a random period from 3 to 5 days, with drugs from the plan given every day. Each examine lasts 1 second. Medicines are administered by nurses.

Every day, every patient has a visit from their attending doctor.
Upon admission to the hospital, the patient is accommodated in a room located in a ward. There are 3 wards in the hospital
• Orthopedics,
• Cardiology,
• Virology

There are 10 rooms in one ward, each room has 3 beds. Only patients with the same diagnosis can lie in one ward, and only patients of one gender can be in one room. If there are no free beds in the hospital, patients cannot be accepted.

• To admit 5 patients to the hospital. Upon admission of each patient, the following message should be displayed on the console:
'Patient <firstName> <lastName> of gender <gender> was admitted with diagnosis <diagnoseName>. Treating doctor: Dr. <firstName> <lastName>. '

Make every single day in the hospital 5 seconds long.

A day in the hospital goes like this:
Patients whose treatment has ended are discharged. When this happens on the console it says:
'Patient <firstName> <lastName> of sex <gender> with diagnosis <diagnoseName> was discharged '.
Accordingly, the bed he occupies to free himself.
The nurses go around the rooms in their assigned wards and give medicines to the patients. When this happens, it should be written on the console 'Nurse <firstName> <lastName> gave patient <firstName> <lastName> in room <roomNumber> of department <sectionName> medicine' 
Then the doctors go to visit the patients entrusted to them. When this happens, the console displays: 'Doctor <firstName> <lastName> visited patient <firstName> <lastName> in room <roomNumber> from department <sectionName>' Each visit lasts 1 second.

To realize 10 days of the hospital's work.
The hospital should have methods to query the following data:
• Available beds in the hospital by department
• Number of patients for each doctor
• Patients who will be discharged the next day
