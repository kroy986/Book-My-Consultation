package com.doctor.book.consultant.payment.service.repository;

import com.doctor.book.consultant.payment.service.collection.PaymentDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends MongoRepository<PaymentDetails, String> {
}
