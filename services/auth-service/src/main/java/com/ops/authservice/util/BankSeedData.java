package com.ops.authservice.util;

import com.ops.authservice.entity.Bank;
import com.ops.authservice.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BankSeedData implements CommandLineRunner {

    private final BankRepository bankRepository;

    @Override
    public void run(String... args) {
        List<Bank> banks = List.of(
                Bank.builder().code("BBL").name("Bangkok Bank").build(),
                Bank.builder().code("KBANK").name("Kasikornbank").build(),
                Bank.builder().code("KTB").name("Krung Thai Bank").build(),
                Bank.builder().code("SCB").name("Siam Commercial Bank").build(),
                Bank.builder().code("BAY").name("Bank of Ayudhya").build()
        );

        for (Bank bank : banks) {
            bankRepository.findById(bank.getCode()).orElseGet(() -> bankRepository.save(bank));
        }
    }
}
