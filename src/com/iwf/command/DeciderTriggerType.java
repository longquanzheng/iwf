package com.iwf.command;

// this trigger type will decide when to invoke decide method based on commands' statues
public enum DeciderTriggerType {
    ALL_CONDITION_COMPLETED, // this will wait for all commands are completed. It will fail the workflow if any command fails(e.g. activity failure)
    ANY_CONDITION_COMPLETED, // this will wait for any command to be completed. It will fail the workflow if any command fails(e.g. activity failure)
    ANY_CONDITION_CLOSED // this will wait for any command to be closed. It won't fail the workflow if any command fails(e.g. activity failure)
}
