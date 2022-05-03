package com.indeed.iwf.demo.subscription;

import com.indeed.iwf.StateMovement;
import com.indeed.iwf.WorkflowState;
import com.indeed.iwf.WorkflowStateDecision;
import com.indeed.iwf.condition.ActivityCondition;
import com.indeed.iwf.condition.BaseCondition;
import com.indeed.iwf.condition.SignalCondition;
import com.indeed.iwf.condition.TimerCondition;
import com.indeed.iwf.demo.subscription.models.Customer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.indeed.iwf.demo.subscription.SubscriptionWorkflow.QUERY_ATTRIBUTE_CUSTOMER;
import static com.indeed.iwf.demo.subscription.SubscriptionWorkflow.WF_STATE_UPDATE_CHARGE_AMOUNT;

class UpdateChargeAmountState implements WorkflowState<Void> {

    @Override
    public String getStateId() {
        return WF_STATE_UPDATE_CHARGE_AMOUNT;
    }

    @Override
    public Class<Void> getInputType() {
        return Void.class;
    }

    @Override
    public List<BaseCondition> prepare(final Void nothing, final Map<String, Object> searchAttributes, final Map<String, Object> queryAttributes) {
        return Arrays.asList(
                new SignalCondition(SubscriptionWorkflow.SIGNAL_METHOD_UPDATE_BILLING_PERIOD_CHARGE_AMOUNT)
        );
    }

    @Override
    public WorkflowStateDecision decide(final Void nothing, final List<ActivityCondition<?>> activityConditions,
                                        final List<TimerCondition> timerConditions, final List<SignalCondition> signalConditions,
                                        final Map<String, Object> searchAttributes, final Map<String, Object> queryAttributes) {

        final Map<String, Object> attrs = new HashMap<>();
        int newAmount = (int) signalConditions.get(0).getSignalValue();
        final Customer customer = (Customer) queryAttributes.get(QUERY_ATTRIBUTE_CUSTOMER);
        customer.getSubscription().setBillingPeriodCharge(newAmount);
        attrs.put(QUERY_ATTRIBUTE_CUSTOMER, customer);

        return new WorkflowStateDecision(

                Arrays.asList(
                        new StateMovement(WF_STATE_UPDATE_CHARGE_AMOUNT, null) // go to a loop to update the value
                ), null, attrs
        );
    }
}