/*
 * Copyright (c) 2018. Prashant Kumar Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package rudaks;

import rudaks.types.HadoopRecord;
import rudaks.types.LineItem;
import rudaks.types.Notification;
import rudaks.types.PosInvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform Invoice to other Objects
 *
 * @author prashant
 * @author www.learningjournal.guru
 */

class RecordBuilder {
    /**
     * Create a flattened List of HadoopRecords from Invoice
     * Each Line Item is denormalized into an independent record
     *
     * @param invoice PosInvoice object
     * @return List of HadoopRecord
     */
    static List<HadoopRecord> getHadoopRecords(PosInvoice invoice) {
        List<HadoopRecord> records = new ArrayList<>();
        for (LineItem i : invoice.getInvoiceLineItems()) {
            HadoopRecord record = new HadoopRecord()
                    .withInvoiceNumber(invoice.getInvoiceNumber())
                    .withCreatedTime(invoice.getCreatedTime())
                    .withStoreID(invoice.getStoreID())
                    .withPosID(invoice.getPosID())
                    .withCustomerType(invoice.getCustomerType())
                    .withPaymentMethod(invoice.getPaymentMethod())
                    .withDeliveryType(invoice.getDeliveryType())
                    .withItemCode(i.getItemCode())
                    .withItemDescription(i.getItemDescription())
                    .withItemPrice(i.getItemPrice())
                    .withItemQty(i.getItemQty())
                    .withTotalValue(i.getTotalValue());
            if (invoice.getDeliveryType().equalsIgnoreCase(AppConfigs.DELIVERY_TYPE_HOME_DELIVERY)) {
                record.setCity(invoice.getDeliveryAddress().getCity());
                record.setState(invoice.getDeliveryAddress().getState());
                record.setPinCode(invoice.getDeliveryAddress().getPinCode());
            }
            records.add(record);
        }
        return records;
    }

    /**
     * Set personally identifiable values to null
     *
     * @param invoice PosInvoice object
     * @return masked PosInvoice object
     */
    static PosInvoice getMaskedInvoice(PosInvoice invoice) {
        invoice.setCustomerCardNo(null);
        if (invoice.getDeliveryType().equalsIgnoreCase(AppConfigs.DELIVERY_TYPE_HOME_DELIVERY)) {
            invoice.getDeliveryAddress().setAddressLine(null);
            invoice.getDeliveryAddress().setContactNumber(null);
        }
        return invoice;
    }

    /**
     * Transform PosInvoice to Notification
     *
     * @param invoice PosInvoice Object
     * @return Notification Object
     */
    static Notification getNotification(PosInvoice invoice) {
        return new Notification()
                .withInvoiceNumber(invoice.getInvoiceNumber())
                .withCustomerCardNo(invoice.getCustomerCardNo())
                .withTotalAmount(invoice.getTotalAmount())
                .withEarnedLoyaltyPoints(invoice.getTotalAmount() * AppConfigs.LOYALTY_FACTOR);
    }
}
