HighLevelMachine Edi;

Request comandoUserCmd;

Invitation comandoScontrol;

Invitation status;

Signal datiSensore;

userCmd demand comandoUserCmd to scontrol;

scontrol grant comandoUserCmd;

scontrol ask comandoScontrol to interruttore;

interruttore accept comandoScontrol;

sensore emit datiSensore;

scontrol sense datiSensore;