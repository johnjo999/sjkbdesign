package com.sjkb.service;

import java.util.List;

import com.sjkb.entities.JobEntity;
import com.sjkb.entities.JobExpenseEntity;
import com.sjkb.models.jobs.AssignExpenseModel;
import com.sjkb.models.jobs.AddBudgetModel;
import com.sjkb.models.jobs.AddInvoiceModel;
import com.sjkb.models.jobs.AddNoteModel;
import com.sjkb.models.jobs.AddPaymentModel;
import com.sjkb.models.jobs.AddQuoteModel;
import com.sjkb.models.jobs.JobAttributeModel;
import com.sjkb.models.jobs.JobCardModel;
import com.sjkb.models.jobs.OutstandingInvoiceModel;
import com.sjkb.models.jobs.PandLExpenseModel;
import com.sjkb.models.jobs.PandLInvoiceModel;

public interface JobService {

	void createJob(String jobid, String designer, String jobPocId);

	public List<JobCardModel> getAllJobsForUser(String empid, String status);

	public JobEntity getJobForFolder(String folder);

	void addExpense(String expense, AssignExpenseModel expenseModel);

	JobAttributeModel getAttributesFor(JobEntity job);

	AssignExpenseModel getCurrentExpenseFor(String jobid, String role);

	String getJobHistory(JobEntity job);

	void addInvoice(AddInvoiceModel invoiceModel, String user);

	void postExpense(JobExpenseEntity expenseModel, String user);

	public List<String> getPaymentMethods();

	void postPayment(AddPaymentModel payment, String user);

	List<PandLExpenseModel> getExpenses(String jobid);

	List<PandLInvoiceModel> getInvoices(String jobid);

	void postNote(AddNoteModel note, String userId);

	String getCustNotes(String uid);

	List<OutstandingInvoiceModel> getOutstandingInvoices(String jobId);

	void postQuote(AddQuoteModel quote, String userId);

	JobAttributeModel getAttributesFor(String jobid);

	void postBudget(AddBudgetModel budget, String userId);

}