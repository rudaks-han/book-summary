package rudaks;

import rudaks.types.Notification;
import rudaks.types.PosInvoice;

class Notifications {

    static Notification getNotificationFrom(PosInvoice invoice) {
        return new Notification()
            .withInvoiceNumber(invoice.getInvoiceNumber())
            .withCustomerCardNo(invoice.getCustomerCardNo())
            .withTotalAmount(invoice.getTotalAmount())
            .withEarnedLoyaltyPoints(invoice.getTotalAmount() * AppConfigs.LOYALTY_FACTOR)
            .withTotalLoyaltyPoints(invoice.getTotalAmount() * AppConfigs.LOYALTY_FACTOR);
    }
}
