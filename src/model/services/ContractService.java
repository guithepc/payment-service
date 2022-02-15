package model.services;

import java.util.Calendar;
import java.util.Date;

import model.entities.Contract;
import model.entities.Installments;

public class ContractService {

	private OnlinePaymentService onlinePaymentService;
	
	//dependecy injection
	public ContractService (OnlinePaymentService onlinePaymentService) {
		this.onlinePaymentService = onlinePaymentService;
	}
	
	public void processContract(Contract contract, int months) {
		double basicQuota = contract.getTotalValue() / months;
		for (int i = 1; i <= months; i++) {
			double updatedQuota = basicQuota + onlinePaymentService.interest(basicQuota, i);
			
			double fullQuota = updatedQuota + onlinePaymentService.paymentFee(updatedQuota);
			
			Date dueDate = addMonths(contract.getDate(), i);
			contract.getInstallments().add(new Installments(dueDate, fullQuota));
		}
	}
	
	private Date addMonths(Date date, int n) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		
		cl.add(Calendar.MONTH, n);
		return cl.getTime();
	}
}
