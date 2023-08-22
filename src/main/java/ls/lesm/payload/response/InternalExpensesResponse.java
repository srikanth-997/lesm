package ls.lesm.payload.response;

import lombok.Data;
@Data
public class InternalExpensesResponse {

	private Integer internalExpId;
	private long benchTenure;
	private String totalSalPaidTillNow;
	private String totalExpenses;
	private String profitOrLoss;
    private String benchPay;
	
	private long daysOnBench;
	
}
