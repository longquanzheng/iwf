package com.iwf.demo.subscription;

import com.iwf.StateDecision;
import com.iwf.StateMovement;
import com.iwf.WorkflowState;
import com.iwf.attributes.QueryAttributesRO;
import com.iwf.attributes.QueryAttributesRW;
import com.iwf.attributes.SearchAttributesRO;
import com.iwf.attributes.SearchAttributesRW;
import com.iwf.command.ActivityCommand;
import com.iwf.command.ActivityOptions;
import com.iwf.command.CommandRequest;
import com.iwf.command.CommandResults;
import com.iwf.demo.subscription.models.Customer;

import static com.iwf.demo.subscription.SubscriptionWorkflow.CHARGE_CUSTOMER_ACTIVITY;
import static com.iwf.demo.subscription.WaitForPeriodState.WF_STATE_WAIT_FOR_NEXT_PERIOD;

class ChargeCurrentPeriodState implements WorkflowState<Void> {

    public static final String WF_STATE_CHARGE_CURRENT_PERIOD = "chargeCurrentPeriod";

    @Override
    public String getStateId() {
        return WF_STATE_CHARGE_CURRENT_PERIOD;
    }

    @Override
    public Class<Void> getInputType() {
        return Void.class;
    }

    @Override
    public CommandRequest execute(final Void nothing, final SearchAttributesRO searchAttributes, final QueryAttributesRO queryAttribute) {
        final Customer customer = searchAttributes.get(SubscriptionWorkflow.QUERY_ATTRIBUTE_CUSTOMER);
        final int currentPeriod = queryAttribute.get(SubscriptionWorkflow.QUERY_ATTRIBUTE_BILLING_PERIOD_NUMBER);
        return CommandRequest.forAllCommandCompleted(
                new ActivityCommand(CHARGE_CUSTOMER_ACTIVITY, new ActivityOptions(30), customer, currentPeriod)
        );
    }

    @Override
    public StateDecision decide(final Void nothing, final CommandResults commandResults,
                                final SearchAttributesRW searchAttributes, final QueryAttributesRW queryAttributes) {
        return new StateDecision(
                new StateMovement(WF_STATE_WAIT_FOR_NEXT_PERIOD)
        );
    }
}
