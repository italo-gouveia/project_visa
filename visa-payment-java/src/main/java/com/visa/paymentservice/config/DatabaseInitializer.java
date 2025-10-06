package com.visa.paymentservice.config;

import com.visa.paymentservice.model.OperationType;
import com.visa.paymentservice.repository.OperationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    @Autowired
    private OperationTypeRepository operationTypeRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize operation types if they don't exist
        if (operationTypeRepository.count() == 0) {
            operationTypeRepository.save(new OperationType("Normal Purchase"));
            operationTypeRepository.save(new OperationType("Purchase with installments"));
            operationTypeRepository.save(new OperationType("Withdrawal"));
            operationTypeRepository.save(new OperationType("Credit Voucher"));
        }
    }
}
