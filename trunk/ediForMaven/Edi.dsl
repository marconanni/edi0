HighLevelMachine Edi;

Request comandoUserCmd;

Invitation status;

Signal datiSensore;

userCmd demand comandoUserCmd to scontrol;

scontrol grant comandoUserCmd;

scontrol ask status to userCmd;

userCmd accept status;

sensore emit datiSensore;

scontrol sense datiSensore;