package com.book.consultant.appointment.service.service;

import com.book.consultant.appointment.service.collection.Availability;
import com.book.consultant.appointment.service.repository.AvailabilityRepo;
import com.book.consultant.appointment.service.requestmodel.AvailabilityMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    @Autowired
    AvailabilityRepo availabilityRepo;

    public Availability saveDoctorAvailability(AvailabilityMap availabilityMap, String doctorId) {
        Availability availability = null;
        if(availabilityRepo.findByDoctorId(doctorId).isPresent()){
            availability = availabilityRepo.findByDoctorId(doctorId).get();
            availability.setAvailabilityMap(availabilityMap.getAvailabilityMap());
        }else{
            availability = Availability.builder()
                    .doctorId(doctorId)
                    .availabilityMap(availabilityMap.getAvailabilityMap())
                    .build();
        }

        return availabilityRepo.save(availability);
    }

    public Availability getDoctorAvailability(String doctorId) {
        Availability availability = null;
        if(availabilityRepo.findByDoctorId(doctorId).isPresent()){
            availability = availabilityRepo.findByDoctorId(doctorId).get();
        }
        return availability;
    }
}
