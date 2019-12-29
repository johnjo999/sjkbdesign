package com.sjkb.models.jobs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sjkb.repositores.JobExpenseInvoiceInterface;

public class AddInvoiceModel {
    private Long id;
    private String folder;
    private LocalDate created;
    List<JobInvoiceRowModel> rows;

    public AddInvoiceModel() {
        created = LocalDate.now();
        rows = new ArrayList<>();
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public List<JobInvoiceRowModel> getRows() {
        return rows;
    }

    public void setRows(List<JobInvoiceRowModel> rows) {
        this.rows = rows;
    }

	public void createBlankRows(int count) {
        for (int i = 0; i<count; i++) {
            rows.add(new JobInvoiceRowModel());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public void addAllExpenses(List<JobExpenseInvoiceInterface> expenses) {
        for (JobExpenseInvoiceInterface expense : expenses) {
            rows.add(new JobInvoiceRowModel(expense));
        }

	}

}