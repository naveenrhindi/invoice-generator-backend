package com.naveen.service;

import com.naveen.entity.Invoice;
import com.naveen.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> fetchInvoices(String clerkId) {
        return invoiceRepository.findByClerkId(clerkId);
    }

    public void removeInvoice(String invoiceId, String clerkId){
        Invoice existingInvoice = invoiceRepository.findByClerkIdAndId(clerkId,invoiceId)
                .orElseThrow(()->new RuntimeException("Invoice not found "+invoiceId+" with this clerk id : "+clerkId));
        invoiceRepository.delete(existingInvoice);
    }

    public void deleteInvoicesByClerkId(String clerkId) {
        invoiceRepository.deleteByClerkId(clerkId);
    }


}
